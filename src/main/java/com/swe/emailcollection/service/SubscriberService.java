package com.swe.emailcollection.service;

import com.swe.emailcollection.model.Subscriber;
import com.swe.emailcollection.repository.SubscriberRepository;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    public ResponseEntity<Object> subscribe(@Email String email, String clientIp) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "Bad Request", "message", "Email is required."));
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "Bad Request", "message", "Invalid email format."));
        }

        Optional<Subscriber> existingSubscriber = subscriberRepository.findByEmail(email);
        if (existingSubscriber.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", "Conflict", "message", "Email already exists."));
        }

        if (clientIp == null) {
            clientIp = "127.0.0.1";  // Localhost fallback
        }

        Subscriber newSubscriber = new Subscriber(email, clientIp);
        subscriberRepository.save(newSubscriber);

        String formattedDate = newSubscriber.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Subscription successful!",
                "email", email,
                "ip", clientIp,
                "timestamp", formattedDate
        ));
    }
}