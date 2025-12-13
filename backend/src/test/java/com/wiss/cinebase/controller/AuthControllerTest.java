package com.wiss.cinebase.controller;

// Importiert Jackson für die JSON-Konvertierung.
import com.fasterxml.jackson.databind.ObjectMapper;
// Importiert das DTO für den Login-Request.
import com.wiss.cinebase.dto.LoginRequestDTO;
// Importiert JUnit-Annotationen.
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
// Importiert Spring Boot Test-Annotationen.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// Importiert statische Methoden für Request-Builder und Result-Matcher.
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integrationstests für den Authentifizierungs-Controller.
 * Überprüft den kompletten Durchstich vom HTTP-Request bis zur Datenbank (via DataInitializer).
 * Quelle: Block 06B - Backend Testing & Security Verification
 */
@SpringBootTest
@AutoConfigureMockMvc // Konfiguriert MockMvc für simulierte HTTP-Anfragen.
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simuliert den HTTP-Client (z. B. Browser/Frontend).

    @Autowired
    private ObjectMapper objectMapper; // Wandelt Java-Objekte in JSON-Strings um.

    @Test
    @DisplayName("Sollte JWT-Token zurückgeben, wenn Login-Daten korrekt sind")
    void testLoginSuccess() throws Exception {
        // 1. Arrange: Nutzung des Admin-Accounts, der durch den DataInitializer erstellt wurde.
        LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "admin123");

        // 2. Act & Assert: POST-Request an /api/auth/login senden.
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // Erwartung: HTTP Status 200 OK.
                .andExpect(status().isOk())
                // Erwartung: JSON-Antwort enthält ein Feld "token".
                .andExpect(jsonPath("$.token").exists())
                // Erwartung: Die zurückgegebene Rolle ist "ADMIN".
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @DisplayName("Sollte 401 Unauthorized zurückgeben, wenn das Passwort falsch ist")
    void testLoginFailure() throws Exception {
        // 1. Arrange: Erstellung eines Requests mit falschem Passwort.
        LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "falschesPasswort");

        // 2. Act & Assert: Senden des fehlerhaften Requests.
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // Erwartung: HTTP Status 401 Unauthorized (gemäß GlobalExceptionHandler).
                .andExpect(status().isUnauthorized());
    }
}