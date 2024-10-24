package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long createdSessionId;

    private String obtainAccessToken() throws Exception {
        LoginRequest loginRequest = createLoginRequest("yoga@studio.com", "test!1234");
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return JsonPath.read(response, "$.token");
    }

    @Test
    @Order(1)
    public void shouldCreateSessionSuccessfully() throws Exception {
        String token = obtainAccessToken();

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga Session");
        sessionDto.setDescription("A relaxing yoga session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L); // Assurez-vous que ce professeur existe

        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        MvcResult result = mockMvc.perform(post("/api/session")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Session"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        createdSessionId = ((Number) JsonPath.read(response, "$.id")).longValue();
    }

    @Test
    @Order(2)
    public void shouldGetSessionByIdSuccessfully() throws Exception {
        String token = obtainAccessToken();

        mockMvc.perform(get("/api/session/" + createdSessionId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdSessionId));
    }

    @Test
    @Order(3)
    public void shouldUpdateSessionSuccessfully() throws Exception {
        String token = obtainAccessToken();

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Yoga Session");
        sessionDto.setDescription("An updated relaxing yoga session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);

        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(put("/api/session/" + createdSessionId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Yoga Session"));
    }

    @Test
    @Order(4)
    public void shouldParticipateInSessionSuccessfully() throws Exception {
        String token = obtainAccessToken();

        // S'assurer que l'utilisateur 2 participe d'abord
        mockMvc.perform(post("/api/session/" + createdSessionId + "/participate/2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void shouldNoLongerParticipateInSessionSuccessfully() throws Exception {
        String token = obtainAccessToken();

        // S'assurer que l'utilisateur 2 est participant
        mockMvc.perform(delete("/api/session/" + createdSessionId + "/participate/2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void shouldDeleteSessionSuccessfully() throws Exception {
        String token = obtainAccessToken();

        mockMvc.perform(delete("/api/session/" + createdSessionId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private LoginRequest createLoginRequest(String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        return loginRequest;
    }
}
