package com.wiss.cinebase.service;

import com.wiss.cinebase.dto.QuestionDTO;
import com.wiss.cinebase.entity.Question;
import com.wiss.cinebase.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    /**
     * Test 1: Alle Fragen abrufen
     */
    @Test
    public void whenGetAllQuestions_thenReturnAllQuestions() {
        // Arrange - Mock-Verhalten definieren
        Question q1 = createTestQuestion("Frage 1", "Antwort 1");
        Question q2 = createTestQuestion("Frage 2", "Antwort 2");
        List<Question> mockQuestions = Arrays.asList(q1, q2);

        when(questionRepository.findAll()).thenReturn(mockQuestions);

        // Act - Service-Methode aufrufen
        List<Question> result = questionService.getAllQuestions();

        // Assert - Ergebnis prüfen
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getQuestion()).isEqualTo("Frage 1");
        assertThat(result.get(1).getQuestion()).isEqualTo("Frage 2");

        // Verify - Repository-Aufruf prüfen
        verify(questionRepository, times(1)).findAll();
    }

    /**
     * Test 2: Frage nach ID finden
     */
    @Test
    public void whenGetQuestionById_thenReturnQuestion() {
        // Arrange
        Long questionId = 1L;
        Question mockQuestion = createTestQuestion("Test Frage", "Test Antwort");
        mockQuestion.setId(questionId);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(mockQuestion));

        // Act
        Question result = questionService.getQuestionById(questionId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(questionId);
        assertThat(result.getQuestion()).isEqualTo("Test Frage");

        verify(questionRepository, times(1)).findById(questionId);
    }

    /**
     * Test 3: Frage mit ungültiger ID - Exception werfen
     */
    @Test
    public void whenGetQuestionByInvalidId_thenThrowException() {
        // Arrange
        Long invalidId = 999L;
        when(questionRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert - Exception erwarten
        assertThatThrownBy(() -> questionService.getQuestionById(invalidId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Question with ID: "+ invalidId +" not found");

        verify(questionRepository, times(1)).findById(invalidId);
    }

    /**
     * Test 4: Neue Frage erstellen
     */
    @Test
    public void whenCreateQuestion_thenReturnSavedQuestion() {
        // Arrange
        Question newQuestion = createTestQuestion("Neue Frage", "Neue Antwort");
        Question savedQuestion = createTestQuestion("Neue Frage", "Neue Antwort");
        savedQuestion.setId(1L);

        QuestionDTO newQuestionDTO = new QuestionDTO(
                null,
                "Neue Frage",
                "Neue Antwort",
                Arrays.asList("Falsch 1", "Falsch 2", "Falsch 3"),
                "geography",
                "hard"
        );

        when(questionRepository.save(any(Question.class))).thenReturn(savedQuestion);

        // Act
        QuestionDTO result = questionService.createQuestion(newQuestionDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getQuestion()).isEqualTo("Neue Frage");

        verify(questionRepository, times(1)).save(any(Question.class));
    }

    /**
     * Test 5: Frage löschen
     */
    @Test
    public void whenDeleteQuestion_thenQuestionIsDeleted() {
        // Arrange
        Long questionId = 1L;
        Question existingQuestion = createTestQuestion("Zu löschende Frage", "Antwort");
        existingQuestion.setId(questionId);

        when(questionRepository.existsById(questionId)).thenReturn(true);
        doNothing().when(questionRepository).deleteById(questionId);

        // Act
        questionService.deleteQuestion(questionId);

        // Assert - Verify dass deleteById aufgerufen wurde
        verify(questionRepository, times(1)).existsById(questionId);
        verify(questionRepository, times(1)).deleteById(questionId);
    }

    /**
     * Test 6: Fragen nach Kategorie finden
     */
    @Test
    public void whenGetQuestionsByCategory_thenReturnFilteredQuestions() {
        // Arrange
        String category = "sports";
        Question sportQuestion = createTestQuestion("Sport Frage", "Sport Antwort");
        sportQuestion.setCategory(category);

        List<Question> mockQuestions = Arrays.asList(sportQuestion);
        when(questionRepository.findByCategory(category)).thenReturn(mockQuestions);

        // Act
        List<Question> result = questionService.getQuestionsByCategory(category);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo(category);

        verify(questionRepository, times(1)).findByCategory(category);
    }

    /**
     * Test 7: Frage aktualisieren
     */
    @Test
    public void whenUpdateQuestion_thenReturnUpdatedQuestion() {
        // Arrange
        Long questionId = 1L;
        QuestionDTO updatedDTO = new QuestionDTO(
                questionId,
                "Neue Frage",
                "Neue Antwort",
                Arrays.asList("Falsch 1", "Falsch 2", "Falsch 3"),
                "geography",
                "hard"
        );

        Question existingQuestion = createTestQuestion("Alte Frage", "Alte Antwort");
        existingQuestion.setId(questionId);

        Question updatedQuestion = createTestQuestion("Neue Frage", "Neue Antwort");
        updatedQuestion.setId(questionId);
        updatedQuestion.setCategory("geography");
        updatedQuestion.setDifficulty("hard");

        when(questionRepository.existsById(questionId)).thenReturn(true);
        when(questionRepository.save(any(Question.class))).thenReturn(updatedQuestion);

        // Act
        QuestionDTO result = questionService.updateQuestion(questionId, updatedDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getQuestion()).isEqualTo("Neue Frage");
        assertThat(result.getCorrectAnswer()).isEqualTo("Neue Antwort");

        verify(questionRepository, times(1)).existsById(questionId);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    /**
     * Helper-Methode zum Erstellen von Test-Fragen
     */
    private Question createTestQuestion(String questionText, String correctAnswer) {
        Question question = new Question(
                questionText,
                correctAnswer,
                Arrays.asList("Falsch 1", "Falsch 2", "Falsch 3"),
                "sports",
                "medium"
        );
        return question;
    }
}
