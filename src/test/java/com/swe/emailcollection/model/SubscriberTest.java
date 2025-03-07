package com.swe.emailcollection.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.swe.emailcollection.repository.SubscriberRepository;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class SubscriberTest {

    @Autowired
    private SubscriberRepository subscriberRepository;

    // Clears existing data before each test
    @BeforeEach
    void setup() {
        assertNotNull(subscriberRepository, "SubscriberRepository should not be null!");
        subscriberRepository.deleteAll();
    }

    // Should creation new subscriber
    @Test
    void testSubscriberCreation() {
        // Given
        String email = "test@example.com";
        String ipAddress = "127.0.0.1";

        // When
        Subscriber subscriber = new Subscriber(email, ipAddress);

        // Then
        assertEquals(email, subscriber.getEmail());
        assertEquals(ipAddress, subscriber.getIpAddress());
        assertNotNull(subscriber.getCreatedAt());
    }

    // Should get email
    @Test
    void testGetEmail() {
        // Given
        Subscriber subscriber1 = new Subscriber("user1@example.com", "1.1.1.1");
        Subscriber subscriber2 = new Subscriber("user2@example.com", "1.1.1.1");
        subscriberRepository.save(subscriber1);
        subscriberRepository.save(subscriber2);

        // When
        List<Subscriber> subscribers = subscriberRepository.findAll();

        // Extract emails
        List<String> emails = subscribers.stream()
                .map(Subscriber::getEmail)
                .collect(Collectors.toList());

        // Then
        assertTrue(emails.contains("user1@example.com"));
        assertTrue(emails.contains("user2@example.com"));
    }
}