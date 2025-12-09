package com.wiss.cinebase.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Repräsentiert eine Bewertung eines Users für einen Film.
 * Technische Analogie: Entspricht der GameSession aus Block 05A.
 * Es ist die Verbindungstabelle zwischen User und Film mit Zusatzdaten (Rating, Kommentar).
 * Quelle: Block 05A - GameSession Entity
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Beziehung 1: Der User, der die Bewertung schreibt
    // Quelle: Block 01A - ManyToOne Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    // Beziehung 2: Der Film, der bewertet wird
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // Die Bewertung (1-10 Sterne, wie in User Story definiert)
    @Column(nullable = false)
    private int rating;

    // Optionaler Text-Kommentar
    @Column(length = 1000)
    private String comment;

    // Zeitstempel der Erstellung
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // --- Konstruktoren ---

    public Review() {}

    public Review(AppUser user, Movie movie, int rating, String comment) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getter & Setter ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }
    public void setUser(AppUser user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
