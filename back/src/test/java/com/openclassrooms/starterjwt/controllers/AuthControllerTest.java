package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.properties")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Test case for successful user registration
    @Test
    @Order(1)
    public void shouldRegisterUserSuccessfully() throws Exception {
        SignupRequest signupRequest = createSignupRequest("will.smith@gmail.com", "will", "smith", "test123");
        String signupRequestJson = objectMapper.writeValueAsString(signupRequest);

        // Perform the registration and expect a successful response
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // Perform registration again with the same email to trigger "Email taken" error
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }

    // Test case for successful login with valid credentials
    @Test
    @Order(2)
    public void shouldLoginSuccessfullyWithValidCredentials() throws Exception {
        LoginRequest loginRequest = createLoginRequest("yoga@studio.com", "test!1234");
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // Verify login success and check response details
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("yoga@studio.com"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    // Test case for failed login with incorrect credentials
    @Test
    @Order(3)
    public void shouldFailLoginWithInvalidCredentials() throws Exception {
        LoginRequest invalidLoginRequest = createLoginRequest("smith.will@gmail.com", "wrongPassword");
        String loginRequestJson = objectMapper.writeValueAsString(invalidLoginRequest);

        // Verify login failure and expect "Bad credentials" message
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    @Order(4)
    public void shouldFailLoginWhenUserNotFound() throws Exception {
        // Crée une requête de connexion avec un email qui n'existe pas dans la base de données
        LoginRequest loginRequest = createLoginRequest("nonexistent@domain.com", "somePassword");
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // Simuler un utilisateur non trouvé
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }


    // Helper method to create a signup request with provided details
    private SignupRequest createSignupRequest(String email, String firstName, String lastName, String password) {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setFirstName(firstName);
        signupRequest.setLastName(lastName);
        signupRequest.setPassword(password);
        return signupRequest;
    }

    // Helper method to create a login request with provided email and password
    private LoginRequest createLoginRequest(String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        return loginRequest;
    }
}
