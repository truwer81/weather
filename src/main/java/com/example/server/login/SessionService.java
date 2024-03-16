package com.example.server.login;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public void saveSession(UUID sessionId, Long userId, Instant expiresAt) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        var session = Session.builder()
                .sessionId(sessionId)
                .user(user)
                .expiresAt(expiresAt)
                .build();
        sessionRepository.save(session);
    }

}
