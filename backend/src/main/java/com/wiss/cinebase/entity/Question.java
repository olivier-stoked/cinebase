package com.wiss.cinebase.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity                                     // ← "Das wird eine Datenbank-Tabelle"
@Table(name = "questions")                  // ← "Tabelle soll 'questions' heissen (Optional)"
public class Question {

    @Id                                    // ← "Das ist der Primary Key"
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ← "PostgreSQL macht Auto-Increment"
    private Long id;

    @Column(nullable = false, length = 128)  // ← "Spalte darf nicht NULL sein, max 128 Zeichen"
    private String question;

    @Column(name = "correct_answer", nullable = false)  // ← "Spalte heisst 'correct_answer'"
    private String correctAnswer;

    @ElementCollection                     // ← "Liste wird als separate Tabelle gespeichert"
    @CollectionTable(name = "question_incorrect_answers", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "incorrect_answer")
    private List<String> incorrectAnswers;

    @Column(nullable = false, length = 64)
    private String category;

    @Column(nullable = false, length = 32)
    private String difficulty;

    // ✅ DEFAULT CONSTRUCTOR hinzufügen (für JPA/Hibernate):
    public Question(){}

    /**
     * Konstruktor ohne ID für neue Fragen
     * @param question
     * @param correctAnswer
     * @param incorrectAnswers
     * @param category
     * @param difficulty
     */
    public Question(String question, String correctAnswer,
                    List<String> incorrectAnswers, String category,
                    String difficulty) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.category = category;
        this.difficulty = difficulty;
    }

    /**
     * Konstruktor mit ID um bestehende Fragen zu aktualisieren oder löschen
     * @param id
     * @param question
     * @param correctAnswer
     * @param incorrectAnswers
     * @param category
     * @param difficulty
     */
    public Question(Long id, String question, String correctAnswer,
                    List<String> incorrectAnswers, String category,
                    String difficulty) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.category = category;
        this.difficulty = difficulty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
