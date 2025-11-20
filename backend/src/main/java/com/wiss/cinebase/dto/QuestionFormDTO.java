package com.wiss.cinebase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Data Transfer Object für Frontend-Formulare (Create UND Update).
 * <p>
 * Wird sowohl für das Erstellen neuer Fragen als auch für das Bearbeiten
 * bestehender Fragen verwendet. Die ID wird nur bei Updates über die URL übergeben,
 * nicht im Request Body.
 * </p>
 *
 * <p><strong>Verwendung:</strong></p>
 * <ul>
 *   <li>POST /api/questions/create - ID wird ignoriert (neue Frage)</li>
 *   <li>PUT /api/questions/{id}/update - ID kommt aus URL (Update)</li>
 * </ul>
 *
 * @author Johnny Krup
 * @version 1.0
 * @since 2025-07-01
 * @see QuestionDTO DTO für die Anzeige von Fragen im Quiz
 */
@Schema(description = "DTO für Frontend-Formulare (Create/Update)")
public class QuestionFormDTO {

    /**
     * Eindeutige ID der Frage.
     * <p>
     * Wird automatisch von der Datenbank generiert beim Speichern.
     * Ist null bei neuen Fragen, die noch nicht gespeichert wurden.
     * </p>
     *
     * @apiNote Wird bei POST-Requests ignoriert, bei PUT-Requests verwendet
     */
    @Schema(description = "Eindeutige ID der Frage", example = "1")
    private Long id;

    /**
     * Der Text der Quiz-Frage.
     * <p>
     * Muss zwischen 5 und 500 Zeichen lang sein und darf nicht leer sein.
     * </p>
     */
    @Schema(description = "Der Frage-Text", example = "Was ist die Hauptstadt der Schweiz?")
    @NotBlank(message = "Frage Text ist erforderlich")
    @Size(min = 5, max = 500, message = "Frage muss zwischen 5 und 500 Zeichen sein")
    private String question;

    /**
     * Die korrekte Antwort auf die Frage.
     * <p>
     * Wird automatisch in die Antworten-Liste eingefügt beim Speichern.
     * </p>
     */
    @Schema(description = "Die korrekte Antwort", example = "Bern")
    @NotBlank(message = "Richtige Antwort ist erforderlich")
    @Size(max = 100, message = "Antwort darf maximal 100 Zeichen haben")
    private String correctAnswer;

    /**
     * Liste der falschen Antworten.
     * <p>
     * Muss genau 3 falsche Antworten enthalten. Diese werden zusammen
     * mit der korrekten Antwort zu einer 4-Antworten-Liste kombiniert.
     * </p>
     */
    @Schema(description = "Die 3 falschen Antworten",
            example = "[\"Zürich\", \"Basel\", \"Genf\"]")
    @NotEmpty(message = "Falsche Antworten sind erforderlich")
    @Size(min = 3, max = 3, message = "Es müssen genau 3 falsche Antworten vorhanden sein")
    private List<@NotBlank(message = "Falsche Antworten dürfen nicht leer sein")
        @Size(max = 100, message = "Antwort darf maximal 100 Zeichen haben") String> incorrectAnswers;

    /**
     * Die Kategorie der Frage.
     */
    @Schema(description = "Die Kategorie der Frage", example = "geography")
    @NotBlank(message = "Kategorie ist erforderlich")
    @Pattern(regexp = "sports|games|movies|geography|science|history|biology|mathematics",
            message = "Kategorie muss eine der folgenden sein: sports, games, movies, geography, science, history, biology, mathematics")
    private String category;

    /**
     * Der Schwierigkeitsgrad der Frage.
     */
    @Schema(description = "Der Schwierigkeitsgrad", example = "medium")
    @NotBlank(message = "Schwierigkeitsgrad ist erforderlich")
    @Pattern(regexp = "easy|medium|hard",
            message = "Schwierigkeitsgrad muss einer der folgenden sein: easy, medium, hard")
    private String difficulty;

    /**
     * Standard-Konstruktor für JSON-Deserialisierung.
     */
    public QuestionFormDTO() {}

    /**
     * Vollständiger Konstruktor für das Erstellen eines QuestionFormDTO.
     *
     * @param question Der Fragetext
     * @param correctAnswer Die korrekte Antwort
     * @param incorrectAnswers Liste mit genau 3 falschen Antworten
     * @param category Die Kategorie
     * @param difficulty Der Schwierigkeitsgrad
     */
    public QuestionFormDTO(String question, String correctAnswer,
                           List<String> incorrectAnswers, String category, String difficulty) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.category = category;
        this.difficulty = difficulty;
    }

    /**
     *  Vollständiger Konstruktor für das Bearbeiten eines QuestionFormDTO.
     *
     * @param question Der Fragetext
     * @param correctAnswer Die korrekte Antwort
     * @param incorrectAnswers Liste mit genau 3 falschen Antworten
     * @param category Die Kategorie
     * @param difficulty Der Schwierigkeitsgrad
     */
    public QuestionFormDTO(Long id, String question, String correctAnswer,
                           List<String> incorrectAnswers, String category, String difficulty) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.category = category;
        this.difficulty = difficulty;
    }

    // Getter und Setter

    /**
     * Gibt die eindeutige ID der Frage zurück.
     *
     * @return Die Frage-ID oder null bei neuen Fragen
     */
    public Long getId() { return id; }
    /**
     * Setzt die eindeutige ID der Frage.
     *
     * @param id Die neue ID (kann null sein für neue Fragen)
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gibt den Fragetext zurück.
     *
     * @return Der Text der Frage
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Setzt den Fragetext.
     *
     * @param question Der neue Fragetext (5-500 Zeichen, nicht leer)
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Gibt die korrekte Antwort zurück.
     *
     * @return Die korrekte Antwort
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Setzt die korrekte Antwort.
     *
     * @param correctAnswer Die neue korrekte Antwort (max. 100 Zeichen, nicht leer)
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Gibt die Liste der falschen Antworten zurück.
     *
     * @return Liste mit genau 3 falschen Antworten
     */
    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    /**
     * Setzt die Liste der falschen Antworten.
     *
     * @param incorrectAnswers Liste mit genau 3 falschen Antworten, keine leer
     */
    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    /**
     * Gibt die Kategorie zurück.
     *
     * @return Die Kategorie der Frage
     */
    public String getCategory() {
        return category;
    }

    /**
     * Setzt die Kategorie.
     *
     * @param category Die neue Kategorie (aus vordefinierten Werten)
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gibt den Schwierigkeitsgrad zurück.
     *
     * @return Der Schwierigkeitsgrad (easy, medium, hard)
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Setzt den Schwierigkeitsgrad.
     *
     * @param difficulty Der neue Schwierigkeitsgrad (easy, medium, hard)
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Konvertiert das FormDTO zu einem QuestionDTO für die Frontend-Anzeige.
     * <p>
     * Kombiniert correctAnswer und incorrectAnswers zu einer gemischten answers-Liste.
     * </p>
     *
     * @param id Die ID der Frage (für Updates)
     * @return QuestionDTO für Frontend-Anzeige
     * @since 1.1
     */
    public QuestionDTO toQuestionDTO(Long id) {
        List<String> allAnswers = new java.util.ArrayList<>(this.incorrectAnswers);
        allAnswers.add(this.correctAnswer);
        java.util.Collections.shuffle(allAnswers);

        return new QuestionDTO(id, this.question, this.correctAnswer,
                allAnswers, this.category, this.difficulty);
    }

    /**
     * Erstellt ein FormDTO aus einem bestehenden QuestionDTO.
     * <p>
     * Nützlich für Edit-Forms, um bestehende Daten zu laden.
     * </p>
     *
     * @param questionDTO Das QuestionDTO zum Konvertieren
     * @return FormDTO mit separaten incorrect answers
     * @since 1.1
     */
    public static QuestionFormDTO fromQuestionDTO(QuestionDTO questionDTO) {
        List<String> incorrectAnswers = questionDTO.getAnswers().stream()
                .filter(answer -> !answer.equals(questionDTO.getCorrectAnswer()))
                .collect(java.util.stream.Collectors.toList());

        return new QuestionFormDTO(
                questionDTO.getQuestion(),
                questionDTO.getCorrectAnswer(),
                incorrectAnswers,
                questionDTO.getCategory(),
                questionDTO.getDifficulty()
        );
    }

    /**
     * Erstellt eine String-Repräsentation des DTOs.
     *
     * @return String-Darstellung für Debugging
     */
    @Override
    public String toString() {
        return "QuestionFormDTO{" +
                "question='" + question + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", incorrectAnswers=" + incorrectAnswers +
                ", category='" + category + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
