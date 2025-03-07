package com.swe.emailcollection.controller;

import com.swe.emailcollection.model.Subscriber;
import com.swe.emailcollection.repository.SubscriberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailAPIControllerTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private EmailAPIController emailAPIController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Should return "emailcollection"
    @Test
    void should_ReturnEmailCollectionTemplate_When_HomePageRequested() {
        // When: The home() method is called
        String viewName = emailAPIController.home();

        // Then: It should return "emailcollection"
        assertEquals("emailcollection", viewName);
    }

    // Should return 400 if email is missing
    @Test
    void should_Return400_When_EmailIsMissing() {
        // Given: No email in request body
        Map<String, String> request = Map.of();

        // When: The user tries to subscribe
        ResponseEntity<Object> response = emailAPIController.subscribe(request, "127.0.0.1");

        // Then: It should return 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Email is required"));
    }

    //  Should return 400 for invalid email format
    @Test
    void should_Return400_When_EmailFormatIsInvalid() {
        // Given: Invalid email
        Map<String, String> request = Map.of("email", "invalidemail");

        // When: The user tries to subscribe
        ResponseEntity<Object> response = emailAPIController.subscribe(request, "127.0.0.1");

        // Then: It should return 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Invalid email format"));
    }

    // Should return 409 if email already exists
    @Test
    void should_Return409_When_EmailAlreadyExists() {
        // Given: An email that already exists
        String email = "test@example.com";
        Map<String, String> request = Map.of("email", email);
        when(subscriberRepository.findByEmail(email)).thenReturn(Optional.of(new Subscriber(email, "127.0.0.1")));

        // When: The user tries to subscribe
        ResponseEntity<Object> response = emailAPIController.subscribe(request, "127.0.0.1");

        // Then: It should return 409 Conflict
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Email already exists"));
    }

    // Should return 201 for successful subscription
    @Test
    void should_Return201_When_SubscribingSuccessfully() {
        // Given: A new email that is not in the system
        String email = "new@example.com";
        Map<String, String> request = Map.of("email", email);
        when(subscriberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(subscriberRepository.save(any())).thenReturn(new Subscriber(email, "127.0.0.1"));

        // When: The user subscribes successfully
        ResponseEntity<Object> response = emailAPIController.subscribe(request, "127.0.0.1");

        // Then: It should return 201 Created
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Subscription successful"));
    }

}