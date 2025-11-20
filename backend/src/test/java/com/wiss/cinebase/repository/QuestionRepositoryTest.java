package com.wiss.cinebase.repository;

import com.wiss.cinebase.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class QuestionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Test 1: Frage speichern und finden
     */
    @Test
    public void whenSaveQuestion_thenCanFindById() {
        // Arrange - Test-Daten vorbereiten
        Question question = new Question(
                "Was ist die Hauptstadt von Deutschland?",
                "Berlin",
                Arrays.asList("München", "Hamburg", "Köln"),
                "geography",
                "easy"
        );


        // Act - Aktion ausführen
        Question saved = entityManager.persistAndFlush(question);
        Optional<Question> found = questionRepository.findById(saved.getId());

        // Assert - Ergebnis prüfen
        assertThat(found).isPresent();
        assertThat(found.get().getQuestion()).isEqualTo("Was ist die Hauptstadt von Deutschland?");
        assertThat(found.get().getCorrectAnswer()).isEqualTo("Berlin");
    }

    /**
     * Test 2: Fragen nach Kategorie finden
     */
    @Test
    public void whenFindByCategory_thenReturnCorrectQuestions() {
        // Arrange - Mehrere Fragen verschiedener Kategorien
        Question sportQuestion = new Question(
                "Wer gewann die WM 2014?",
                "Deutschland",
                Arrays.asList("Brasilien", "Argentinien", "Spanien"),
                "sports",
                "medium"
        );


        Question geoQuestion = new Question(
                "Wie heisst der längste Fluss Europas?",
                "Wolga",
                Arrays.asList("Donau", "Rhein", "Elbe"),
                "geography",
                "hard"
        );

        entityManager.persistAndFlush(sportQuestion);
        entityManager.persistAndFlush(geoQuestion);

        // Act - Nach Kategorie suchen
        List<Question> sportQuestions = questionRepository.findByCategory("sports");
        List<Question> geoQuestions = questionRepository.findByCategory("geography");

        // Assert - Ergebnisse prüfen
        assertThat(sportQuestions).hasSize(1);
        assertThat(sportQuestions.getFirst().getQuestion()).contains("WM 2014");

        assertThat(geoQuestions).hasSize(1);
        assertThat(geoQuestions.getFirst().getQuestion()).contains("Fluss");
    }

    /**
     * Test 3: Fragen nach Schwierigkeit finden
     */
    @Test
    public void whenFindByDifficulty_thenReturnCorrectQuestions() {
        // Arrange
        Question easyQuestion = new Question(
                "Wie viele Beine hat eine Spinne?",
                "8",
                Arrays.asList("6", "10", "4"),
                "biology",
                "easy"
        );

        Question hardQuestion = new Question(
                "Was ist die Quadratwurzel von 144?",
                "12",
                Arrays.asList("11", "13", "14"),
                "mathematics",
                "hard"
        );
        entityManager.persistAndFlush(easyQuestion);
        entityManager.persistAndFlush(hardQuestion);

        // Act
        List<Question> easyQuestions = questionRepository.findByDifficulty("easy");
        List<Question> hardQuestions = questionRepository.findByDifficulty("hard");

        // Assert
        assertThat(easyQuestions).hasSize(1);
        assertThat(easyQuestions.getFirst().getCorrectAnswer()).isEqualTo("8");

        assertThat(hardQuestions).hasSize(1);
        assertThat(hardQuestions.getFirst().getCorrectAnswer()).isEqualTo("12");
    }

    /**
     * Test 4: Alle Fragen löschen
     */
    @Test
    public void whenDeleteAll_thenRepositoryIsEmpty() {
        // Arrange - Ein paar Fragen hinzufügen
        Question q1 = new Question(
                "Test 1",
                "Answer 1",
                Arrays.asList("Wrong 1", "Wrong 2", "Wrong 3"),
                "movies",
                "easy"
        );


        Question q2 = new Question(
                "Test 2",
                "Answer 2",
                Arrays.asList("Wrong 1", "Wrong 2", "Wrong 3"),
                "movies",
                "medium"
        );

        entityManager.persistAndFlush(q1);
        entityManager.persistAndFlush(q2);

        // Act - Alle löschen
        questionRepository.deleteAll();

        // Assert - Repository ist leer
        assertThat(questionRepository.findAll()).isEmpty();
    }

    /**
     * Test 5: Frage mit ungültiger ID nicht gefunden
     */
    @Test
    public void whenFindByInvalidId_thenReturnEmpty() {
        // Act - Nach nicht existierender ID suchen
        Optional<Question> found = questionRepository.findById(999L);

        // Assert - Sollte leer sein
        assertThat(found).isEmpty();
    }
}
