package com.wiss.cinebase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiss.cinebase.dto.QuestionDTO;
import com.wiss.cinebase.entity.Question;
import com.wiss.cinebase.exception.QuestionNotFoundException;
import com.wiss.cinebase.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test-Klasse für QuestionController
 *
 * @WebMvcTest - Lädt nur Web-Layer (Controller, nicht Service/Repository)
 * @MockBean - Erstellt Mock-Beans für Spring Context
 * MockMvc - Simuliert HTTP-Requests
 */
@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test 1: GET /api/questions - Alle Fragen abrufen
     */
    @Test
    public void whenGetAllQuestions_thenReturnJsonArray() throws Exception {
        // Arrange
        QuestionDTO q1 = createTestQuestionDTO("Frage 1", "Antwort 1");
        QuestionDTO q2 = createTestQuestionDTO("Frage 2", "Antwort 2");
        List<QuestionDTO> allQuestions = Arrays.asList(q1, q2);

        when(questionService.getAllQuestionsAsDTO()).thenReturn(allQuestions);

        // Act & Assert
        mockMvc.perform(get("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].question", is("Frage 1")))
                .andExpect(jsonPath("$[1].question", is("Frage 2")));
    }

    /**
     * Test 2: GET /api/questions/{id} - Frage nach ID
     */
    @Test
    public void whenGetQuestionById_thenReturnJson() throws Exception {
        // Arrange
        QuestionDTO question = createTestQuestionDTOWithId(1L, "Test Frage", "Test Antwort");

        when(questionService.getQuestionByIdAsDTO(1L)).thenReturn(question);

        // Act & Assert
        mockMvc.perform(get("/api/questions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.question", is("Test Frage")))
                .andExpect(jsonPath("$.correctAnswer", is("Test Antwort")));
    }

    /**
     * Test 3: POST /api/questions - Neue Frage erstellen
     */
    @Test
    public void whenCreateQuestion_thenReturnCreatedQuestion() throws Exception {
        // Arrange - DTO für Input und Output
        QuestionDTO inputDTO = createTestQuestionDTO(
                "Neue Frage",
                "Neue Antwort"
        );

        QuestionDTO savedDTO = createTestQuestionDTOWithId(
                1L,    // ID für gespeicherte Frage
                "Neue Frage",
                "Neue Antwort"
        );


        when(questionService.createQuestion(any(QuestionDTO.class))).thenReturn(savedDTO);

        // Act & Assert
        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.question", is("Neue Frage")));
    }

    /**
     * Test 4: PUT /api/questions/{id} - Frage aktualisieren
     */
    @Test
    public void whenUpdateQuestion_thenReturnUpdatedQuestion() throws Exception {
        // Arrange
        QuestionDTO updatedQuestion = createTestQuestionDTOWithId(
                1L,
                "Aktualisierte Frage",
                "Aktualisierte Antwort");

        when(questionService.updateQuestion(anyLong(), any(QuestionDTO.class))).thenReturn(updatedQuestion);

        // Act & Assert
        mockMvc.perform(put("/api/questions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedQuestion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question", is("Aktualisierte Frage")));
    }

    /**
     * Test 5: DELETE /api/questions/{id} - Frage löschen
     */
    @Test
    public void whenDeleteQuestion_thenReturnNoContent() throws Exception {
        // Arrange
        // Service-Mock braucht keine Rückgabe für void-Methoden

        // Act & Assert
        mockMvc.perform(delete("/api/questions/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Test 6: GET /api/questions/category/{category} - Fragen nach Kategorie
     */
    @Test
    public void whenGetQuestionsByCategory_thenReturnFilteredQuestions() throws Exception {
        // Arrange
        QuestionDTO sportQuestion = createTestQuestionDTO("Sport Frage", "Sport Antwort");
        List<QuestionDTO> sportQuestions = List.of(sportQuestion);

        when(questionService.getQuestionsByCategoryAsDTO("sports")).thenReturn(sportQuestions);

        // Act & Assert
        mockMvc.perform(get("/api/questions/category/sports")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].category", is("sports")));
    }

    /**
     * Test 7: POST /api/questions - Ungültige Daten (Validation Test)
     */
    @Test
    public void whenCreateQuestionWithInvalidData_thenReturnBadRequest() throws Exception {
        // Arrange - Frage ohne required fields
        Question invalidQuestion = new Question();
        // Nur question setzen, correctAnswer fehlt
        invalidQuestion.setQuestion("Unvollständige Frage");

        // Act & Assert
        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidQuestion)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test 8: GET /api/questions/999 - Nicht existierende Frage
     */
    @Test
    public void whenGetNonExistingQuestion_thenReturnNotFound() throws Exception {
        // Arrange
        when(questionService.getQuestionByIdAsDTO(999L))
                .thenThrow(new QuestionNotFoundException(999L));

        // Act & Assert
        mockMvc.perform(get("/api/questions/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Oder isNotFound(), je nach Exception Handler
    }

    /**
     * Helper-Methode zum Erstellen von Test-Fragen
     */
    private Question createTestQuestion(String questionText, String correctAnswer) {
        return new Question(
                questionText,
                correctAnswer,
                Arrays.asList("Falsch 1", "Falsch 2", "Falsch 3"),
                "sports",
                "medium"
        );
    }

    private QuestionDTO createTestQuestionDTO(String questionText, String correctAnswer) {
        return new QuestionDTO(
                null,  // ID für neue DTOs
                questionText,
                correctAnswer,
                Arrays.asList("Falsch 1", "Falsch 2", "Falsch 3", correctAnswer),
                "sports",
                "medium"
        );
    }

    /**
     * Helper-Methode für DTOs mit ID
     */
    private QuestionDTO createTestQuestionDTOWithId(Long id, String questionText, String correctAnswer) {
        return new QuestionDTO(
                id,
                questionText,
                correctAnswer,
                Arrays.asList("Falsch 1", "Falsch 2", "Falsch 3", correctAnswer),
                "sports",
                "medium"
        );
    }
}