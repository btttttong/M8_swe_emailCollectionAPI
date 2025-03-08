package com.swe.emailcollection;

import com.swe.emailcollection.model.Subscriber;
import com.swe.emailcollection.repository.SubscriberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        subscriberRepository.deleteAll(); // Ensure a clean database before each test
    }

    @Test
    void should_SubscribeUser_And_VerifyInAdminPanel() throws Exception {
        //Step 1: Subscribe a user
        String email = "test@example.com";
        String requestBody = objectMapper.writeValueAsString(Map.of("email", email));

        mockMvc.perform(post("/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))

        //Step 2: Verify response is 201 Created
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Subscription successful!"))
                .andExpect(jsonPath("$.email").value(email));

        //Step 3: Verify subscription appears in admin panel
        mockMvc.perform(get("/admin?page=0&size=1")
                .contentType(MediaType.TEXT_HTML))

        //Step 4: Ensure subscriber data is present in the response
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subscribers"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void should_Return400_When_EmailIsInvalid() throws Exception {
        //Step 1: Attempt to subscribe with an invalid email
        String requestBody = objectMapper.writeValueAsString(Map.of("email", "invalidemail"));

        mockMvc.perform(post("/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))

        //Step 2: Expect 400 Bad Request
                .andExpect(status().isBadRequest())
                .andDo(result -> System.out.println("Actual Response: " + result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void should_Return409_When_EmailAlreadyExists() throws Exception {
        //Step 1: Insert a duplicate subscriber manually
        String email = "duplicate@example.com";
        subscriberRepository.save(new Subscriber(email, "127.0.0.1")); 

        String requestBody = objectMapper.writeValueAsString(Map.of("email", email));

        //Step 2: Attempt to subscribe with the same email
        mockMvc.perform(post("/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))

        //Step 3: Expect 409 Conflict
                .andExpect(status().isConflict())
                .andDo(result -> System.out.println("Actual Response: " + result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.error").value("Conflict"));
    }
}