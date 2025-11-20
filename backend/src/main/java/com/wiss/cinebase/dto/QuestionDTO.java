package com.wiss.cinebase.dto;

import com.wiss.cinebase.controller.QuestionController;
import com.wiss.cinebase.entity.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

/**
 * Data Transfer Object für Quiz-Fragen.
 * <p>
 * Wird für die Kommunikation zwischen Frontend und Backend verwendet.
 * Enthält Validation-Regeln für alle Eingabefelder und Swagger-Dokumentation
 * für die API-Beschreibung.
 * </p>
 *
 * <p><strong>Validation-Regeln:</strong></p>
 * <ul>
 *   <li>Frage: 5-128 Zeichen, nicht leer</li>
 *   <li>Korrekte Antwort: maximal 32 Zeichen, nicht leer</li>
 *   <li>Antworten: genau 4 Antworten, keine leer</li>
 *   <li>Kategorie: muss aus vordefinierten Werten stammen</li>
 *   <li>Schwierigkeit: easy, medium oder hard</li>
 * </ul>
 *
 * <p><strong>Beispiel:</strong></p>
 *  <pre>{@code
 *  QuestionDTO question = new QuestionDTO(
 *      "Was ist die Hauptstadt der Schweiz?",
 *      "Bern",
 *      Arrays.asList("Genf", "Zürich", "Zug"),
 *      "geography",
 *      "easy"
 *  );
 *  }</pre>
 *
 * @author Johnny Krup
 * @version 1.0
 * @since 2025-06-01
 * @see Question Entity-Klasse für die Datenbank
 * @see QuestionController REST-Controller für API-Endpoints
 */
public class QuestionDTO {

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
     * Muss zwischen 5 und 128 Zeichen lang sein und darf nicht leer sein.
     * Wird in der Benutzeroberfläche als Hauptfrage angezeigt.
     * </p>
     *
     * @apiNote HTML-Tags werden nicht unterstützt, nur reiner Text
     */
    @Schema(description = "Der Frage-Text", example = "Was ist die Hauptstadt der Schweiz?")
    @NotBlank(message = "Frage Text ist erforderlich")
    @Size(min = 5, max = 128, message = "Frage muss zwischen 5 und 128 Zeichen sein")
    private String question;

    /**
     * Die korrekte Antwort auf die Frage.
     * <p>
     * Muss in der answers-Liste enthalten sein. Maximal 32 Zeichen lang
     * und darf nicht leer sein. Case-sensitive Vergleich.
     * </p>
     *
     * @apiNote Muss exakt mit einem Eintrag in der answers-Liste übereinstimmen
     */
    @Schema(description = "Die korrekte Antwort", example = "Bern")
    @NotBlank(message = "Richtige Antwort ist erforderlich")
    @Size(max = 32, message = "Antwort darf maximal 32 Zeichen haben")
    private String correctAnswer;

    /**
     * Liste aller Antwortmöglichkeiten inklusive der korrekten Antwort.
     * <p>
     * Muss genau 4 Antworten enthalten, keine darf leer sein.
     * Die korrekte Antwort muss in dieser Liste enthalten sein.
     * Reihenfolge wird für die Anzeige im Frontend verwendet.
     * </p>
     *
     * @implNote Die Liste wird vom Frontend gemischt angezeigt
     * @apiNote Duplikate sind technisch möglich, aber nicht empfohlen
     */
    @Schema(description = "Alle Antwortmöglichkeiten (inkl. korrekte)",
            example = "[\"Bern\", \"Zürich\", \"Basel\", \"Genf\"]")
    @NotEmpty(message = "Antworten-Liste darf nicht leer sein")
    @Size(min = 4, max = 4, message = "Es müssen genau 4 Antworten vorhanden sein")
    private List<@NotBlank(message = "Antworten dürfen nicht leer sein") String> answers;

    /**
     * Die Kategorie der Frage.
     * <p>
     * Muss einer der vordefinierten Kategorien entsprechen:
     * sports, games, movies, geography, science, history, biology, mathematics.
     * Wird für Filterung und Kategorisierung verwendet.
     * </p>
     *
     * @apiNote Case-sensitive - nur Kleinbuchstaben verwenden
     */
    @Schema(description = "Die Kategorie der Frage", example = "sports")
    @NotBlank(message = "Kategorie ist erforderlich")
    @Pattern(regexp = "sports|games|movies|geography|science|history|biology|mathemathics",
            message = "Kategorie muss eine der folgenden sein: sports, games, movies, geography, science, history")
    private String category;

    /**
     * Der Schwierigkeitsgrad der Frage.
     * <p>
     * Kann einen der folgenden Werte haben:
     * </p>
     * <ul>
     *   <li>easy - einfache Fragen</li>
     *   <li>medium - mittlere Schwierigkeit</li>
     *   <li>hard - schwierige Fragen</li>
     * </ul>
     *
     * @apiNote Wird für Filterung und Schwierigkeitsanpassung verwendet
     */
    @Schema(description = "Der Schwierigkeitsgrad der Frage", example = "hard")
    @NotBlank(message = "Schwierigkeitsgrad ist erforderlich")
    @Pattern(regexp = "easy|medium|hard",
            message = "Schwierigkeitsgrad muss einer der folgenden sein: easy, medium, hard")
    private String difficulty;

    /**
     * Standard-Konstruktor für JSON-Deserialisierung.
     * <p>
     * Wird von Jackson/Spring für die Konvertierung von JSON zu DTO verwendet.
     * Erstellt ein leeres DTO-Objekt, alle Felder sind null.
     * </p>
     */
    public QuestionDTO() {}

    /**
     * Vollständiger Konstruktor für die Erstellung eines QuestionDTO.
     * <p>
     * Erstellt ein vollständig initialisiertes DTO-Objekt mit allen Feldern.
     * Nützlich für Tests und interne Konvertierungen.
     * </p>
     *
     * @param id Die eindeutige ID (kann null für neue Fragen sein)
     * @param question Der Fragetext (5-128 Zeichen)
     * @param correctAnswer Die korrekte Antwort (max. 32 Zeichen)
     * @param answers Liste mit genau 4 Antworten
     * @param category Kategorie aus den vordefinierten Werten
     * @param difficulty Schwierigkeitsgrad (easy, medium, hard)
     *
     * @apiNote Validation findet erst bei der Verwendung statt, nicht im Konstruktor
     */
    public QuestionDTO(Long id, String question, String correctAnswer, List<String> answers,
                       String category, String difficulty) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
        this.category = category;
        this.difficulty = difficulty;
    }

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
    public String getQuestion() { return question; }
    /**
     * Setzt den Fragetext.
     *
     * @param question Der neue Fragetext (5-128 Zeichen, nicht leer)
     * @throws MethodArgumentNotValidException bei ungültigen Daten (bei Validation)
     */
    public void setQuestion(String question) { this.question = question; }

    /**
     * Gibt die korrekte Antwort zurück.
     *
     * @return Die korrekte Antwort als Text
     */
    public String getCorrectAnswer() { return correctAnswer; }

    /**
     * Setzt die korrekte Antwort
     *
     * @param correctAnswer Die korrekte Antwort als Text
     */
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    /**
     * Gibt die Liste aller Antwortmöglichkeiten zurück.
     *
     * @return Unveränderbare Liste mit allen Antworten
     * @apiNote Die zurückgegebene Liste sollte nicht direkt modifiziert werden
     */
    public List<String> getAnswers() { return answers; }

    /**
     * Setzt die Liste aller Antwortmöglichkeiten.
     *
     * @param answers Die Liste der Antwortmöglichkeiten.
     */
    public void setAnswers(List<String> answers) { this.answers = answers; }

    /**
     * Gibt die Kategorie der Frage zurück.
     *
     * @return Kategorie der Frage als Text.
     */
    public String getCategory() { return category; }

    /**
     * Setzt die Kategorie der Frage.
     *
     * @param category Kategorie der Frage als Text.
     */
    public void setCategory(String category) { this.category = category; }

    /**
     * Gibt den Schwierigkeitsgrad der Frage zurück.
     *
     * @return Der Schwierigkeitsgrad der Frage als Text.
     */
    public String getDifficulty() { return difficulty; }

    /**
     * Setzt den Schwierigkeitsgrad der Frage.
     *
     * @param difficulty Der Schwierigkeitsgrad der Frage als Text.
     */
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}
