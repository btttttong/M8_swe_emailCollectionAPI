package com.swe.emailcollection.controller;

import com.swe.emailcollection.service.SubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailAPIControllerTest {

    @Mock
    private SubscriberService subscriberService;

    @InjectMocks
    private EmailAPIController emailAPIController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Should return "emailcollection"
    @Test
    void should_ReturnEmailCollectionTemplate_When_HomePageRequested() {
        // Given (No special setup needed)

        // When: The home method is called
        String viewName = emailAPIController.home();

        // Then: It should return "emailcollection"
        assertEquals("emailcollection", viewName);
    }

    //Should return 400 if email is missing
    @Test
    void should_Return400_When_EmailIsMissing() {
        // Given: An empty request (no email)
        Map<String, String> request = Map.of();
        when(subscriberService.subscribe(null, "127.0.0.1"))
            .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Bad Request", "message", "Email is required.")));

        // When: The user tries to subscribe
        ResponseEntity<Object> response = emailAPIController.subscribe(request, "127.0.0.1");

        // Then: It should return 400 Bad Request with a relevant error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Email is required."));
    }

    //Should return 400 for invalid email format
    @Test
    void should_Return400_When_EmailFormatIsInvalid() {
        // Given: A request with an invalid email
        Map<String, String> request = Map.of("email", "invalidemail");
        when(subscriberService.subscribe("invalidemail", "127.0.0.1"))
            .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Bad Request", "message", "Invalid email format.")));

        // When: The user tries to subscribe
        ResponseEntity<Object> response = emailAPIController.subscribe(request, "127.0.0.1");

        // Then: It should return 400 Bad Request with a relevant error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Invalid email format."));
    }

    //Should return 409 if email already exists
    @Test
    void should_Return409_When_EmailAlreadyExists() {
        // Given: An email that is already in the system
        String email = "test@example.com";
        Map<String, String> request = Map.of("email", email);
        when(subscriberService.subscribe(email, "127.0.0.1"))
            .thenReturn(ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Conflict", "message", "Email already exists.")));

        // When: The user tries to subscribe with the same email
        ResponseEntity<Object> response = emailAPIController.subscribe(request, "127.0.0.1");

        // Then: It should return 409 Conflict with a relevant error message
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Email already exists."));
    }

    //Should return 201 for successful subscription
    @Test
    void should_Return201_When_SubscribingSuccessfully() {
        // Given: A new valid email
        String email = "new@example.com";
        Map<String, String> request = Map.of("email", email);
        when(subscriberService.subscribe(email, "127.0.0.1"))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Subscription successful!", "email", email, "ip", "127.0.0.1")));

        // When: The user subscribes successfully
        ResponseEntity<Object> response = emailAPIController.subscribe(request, "127.0.0.1");

        // Then: It should return 201 Created with a success message
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("Subscription successful"));
    }
}