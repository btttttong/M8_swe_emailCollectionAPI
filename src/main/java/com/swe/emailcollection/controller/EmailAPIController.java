package com.swe.emailcollection.controller;

import com.swe.emailcollection.model.Subscriber;
import com.swe.emailcollection.repository.SubscriberRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class EmailAPIController {
    
    private final SubscriberRepository subscriberRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmailAPIController.class);

    public EmailAPIController(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @GetMapping("/")
    public String home() {
        return "emailcollection";
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Object> subscribe(@Valid @RequestBody Map<String, String> requestBody, 
                                            @RequestHeader(value = "X-Forwarded-For", required = false) String clientIp) {
        String email = requestBody.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email is required."));
        }

        Optional<Subscriber> existingSubscriber = subscriberRepository.findByEmail(email);
        if (existingSubscriber.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already exists."));
        }

        // Get client IP (Fallback to local address if missing)
        if (clientIp == null) {
            clientIp = "127.0.0.1";  // Localhost fallback
        }
        

        Subscriber newSubscriber = new Subscriber(email, clientIp);
        subscriberRepository.save(newSubscriber);

        logger.info("Successfully registered email: {} from IP: {} at {}", email, clientIp, newSubscriber.getCreatedAt());
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