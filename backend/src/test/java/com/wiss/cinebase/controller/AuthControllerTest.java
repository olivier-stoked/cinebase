package com.wiss.cinebase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiss.cinebase.dto.LoginRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Test für die Authentifizierung.
 * Prüfungsanforderung: Sicherheitstest (JWT & Authentication).
 * Quelle: Block 06B - Backend Testing
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simuliert den HTTP-Client (Browser)

    @Autowired
    private ObjectMapper objectMapper; // Wandelt Java-Objekte in JSON um

    @Test
    @DisplayName("Sollte JWT-Token zurückgeben, wenn Login-Daten korrekt sind")
    void testLoginSuccess() throws Exception {
        // 1. Arrange: Wir nutzen den Admin, den der DataInitializer erstellt hat
        LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "admin123");

        // 2. Act & Assert: POST-Request an /api/auth/login senden
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // Erwartung: Status 200 OK
                .andExpect(status().isOk())
                // Erwartung: JSON-Antwort enthält ein Feld "token"
                .andExpect(jsonPath("$.token").exists())
                // Erwartung: Rolle ist "ADMIN"
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @DisplayName("Sollte 401 Unauthorized zurückgeben, wenn Passwort falsch ist")
    void testLoginFailure() throws Exception {
        // 1. Arrange: Falsches Passwort
        LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "falschesPasswort");

        // 2. Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // Erwartung: Status 401 oder 403 (je nach Config)
                .andExpect(status().isUnauthorized());
    }
}