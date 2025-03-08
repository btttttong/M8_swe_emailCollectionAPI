package com.swe.emailcollection;

import com.swe.emailcollection.controller.EmailAPIController;
import com.swe.emailcollection.controller.AdminController;
import com.swe.emailcollection.service.SubscriberService;
import com.swe.emailcollection.repository.SubscriberRepository;
import com.swe.emailcollection.model.Subscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EmailCollectionApplicationTests {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmailAPIController emailAPIController;

    @Autowired
    private AdminController adminController;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ObjectMapper objectMapper;

	@BeforeEach
    void setup() {
        subscriberRepository.deleteAll(); // Ensure a clean database before each test
    }

    //Check the application starts successfully
    @Test
    void contextLoads() {
        // Given (No setup needed)

        // When: The application starts

        // Then: Controllers and services should be loaded
        assertThat(emailAPIController).isNotNull();
        assertThat(adminController).isNotNull();
        assertThat(subscriberService).isNotNull();
        assertThat(subscriberRepository).isNotNull();
    }

    //Check Home page (`/`) returns the subscription form HTML
    @Test
    void should_ReturnSubscriptionForm_When_AccessingHomePage() throws Exception {
        // Given (No setup needed)

        // When: A user visits the home page (`/`)
        mockMvc.perform(get("/"))

        // Then: It should return 200 OK and serve the `emailcollection.html` view
                .andExpect(status().isOk())
                .andExpect(view().name("emailcollection"));
    }

    //Check admin page (`/admin`) returns subscribers
    @Test
    void should_ReturnAdminPage_When_AccessingAdmin() throws Exception {
        // Given: Insert test subscribers
        subscriberRepository.save(new Subscriber("admin1@example.com", "192.168.1.1"));
        subscriberRepository.save(new Subscriber("admin2@example.com", "192.168.1.2"));

        // When: The admin visits `/admin`
        mockMvc.perform(get("/admin"))

        // Then: It should return 200 OK and serve the `admin.html` view
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("subscribers"));
    }

    //Check API (`POST /subscribe`) works
    @Test
    void should_SubscribeUser_When_APIIsCalled() throws Exception {
        // Given: A valid email input
        String email = "test@example.com";
        String requestBody = objectMapper.writeValueAsString(Map.of("email", email));

        // When: The user subscribes
        mockMvc.perform(post("/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))

        // Then: The response should be 201 Created with success message
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Subscription successful!"))
                .andExpect(jsonPath("$.email").value(email));
    }
}