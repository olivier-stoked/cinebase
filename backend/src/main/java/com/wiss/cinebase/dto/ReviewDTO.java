package com.wiss.cinebase.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DTO für Bewertungen (Reviews).
 * Dient dem Datentransfer zwischen API und Service.
 *
 * Quelle: Block 04A - DTOs
 * Validation: Block 06A - Bean Validation
 */
public class ReviewDTO {

    private Long id;

    // Nur für die Anzeige (Output): Wer hat die Review geschrieben?
    private String username;

    // Für die Erstellung (Input): Zu welchem Film gehört die Review?
    @NotNull(message = "Film ID ist erforderlich")
    private Long movieId;

    // Bewertungsskala 1-10 (entspricht 0.5 bis 5 Sterne)
    @Min(value = 1, message = "Bewertung muss mindestens 1 sein")
    @Max(value = 10, message = "Bewertung darf maximal 10 sein")
    private int rating;

    @Size(max = 1000, message = "Kommentar darf maximal 1000 Zeichen lang sein")
    private String comment;

    // Nur für die Anzeige: Wann wurde sie erstellt?
    private LocalDateTime createdAt;

    // --- Konstruktoren ---

    public ReviewDTO() {}

    public ReviewDTO(Long id, String username, Long movieId, int rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.movieId = movieId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // --- Getter & Setter ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
