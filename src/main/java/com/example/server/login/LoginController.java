package com.example.server.login;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class LoginController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request) {
        log.info("Received login request");
        var session = request.getSession();
        var authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Basic")) {
            log.warn("Authorization header missing or not starting with 'Basic'.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or not starting with 'Basic'.");
        }

        log.info("Authorization header found and seems correct.");

        // Usuwanie "Basic " i zdekodowanie
        var base64Credentials = authorization.substring("Basic".length()).trim();
        var credentials = BasicAuthUtils.decode(base64Credentials);

        var username = credentials[0];
        var password = credentials[1];
        log.info("Credentials decoded: username = {}", credentials[0]);
        if (!userService.authenticate(username, password)) {
            log.warn("Authentication failed for username: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for username: " + username);
        }
        var user = userService.getUser(username);
        log.info("User authenticated: {}", username);
        session.setAttribute("user", user);
        return ResponseEntity.ok().build();
    }
}
