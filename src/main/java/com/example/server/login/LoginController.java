package com.example.server.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class LoginController {
    private final SessionService sessionService;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);


    @GetMapping("/login")
    public Object getSession(HttpServletResponse httpServletResponse) {
        UUID sessionId = UUID.randomUUID();
        Cookie sessionCookie = new Cookie("SESSIONID", sessionId.toString());
        sessionCookie.setHttpOnly(true); // Zwiększa bezpieczeństwo poprzez zapobieganie dostępu przez JavaScript
        sessionCookie.setPath("/"); // Dostępne dla całej domeny
        sessionCookie.setMaxAge(60 * 60); // MAksymalny czas życia ciasteczka (60*60 = 1 godzina)
        // Dodanie ciasteczka do odpowiedzi
        httpServletResponse.addCookie(sessionCookie);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request) {
        log.info("Received login request");

        // Pobranie UUID z ciasteczka
        UUID sessionId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    try {
                        sessionId = UUID.fromString(cookie.getValue());
                    } catch (IllegalArgumentException e) {
                        log.error("Invalid UUID format", e);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session ID.");
                    }
                    break;
                }
            }
        }

        if (sessionId == null) {
            log.warn("No SESSIONID cookie found.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No session found.");
        }

        // weryfikacja użytkownika
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            log.info("Authorization header found and seems correct.");

            // Usuwanie "Basic " i zdekodowanie
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String[] credentials = BasicAuthUtils.decode(base64Credentials);

            String username = credentials[0];
            String password = credentials[1];
            log.info("Credentials decoded: username = {}", credentials[0]);
            if (userService.authenticate(username, password)) {
                Long userId = userService.getUserId(username, password);
                log.info("User authenticated: {}", username);
                if (userId != null) {
                    Instant now = Instant.now();
                    Instant expiry = now.plus(1, ChronoUnit.HOURS); // 1 godzina ważności sesji
                    log.info("Saving session for user ID: {}", userId);
                    sessionService.saveSession(sessionId, userId, expiry);
                    return ResponseEntity.ok().build();

                }
            } else {
                log.warn("Authentication failed for username: {}", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for username: " + username);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for username: " + username);
        } else {
            log.warn("Authorization header missing or not starting with 'Basic'.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or not starting with 'Basic'.");
        }
    }
}
