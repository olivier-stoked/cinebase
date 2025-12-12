package com.wiss.cinebase.entity;

// Quelle: Block 03A - ORM Mapping
// Importiert JPA-Annotationen.
import jakarta.persistence.*;

// Import für Zeitstempel.
import java.time.LocalDateTime;

/**
 * Repräsentiert eine Bewertung eines Films durch einen User.
 * Quelle: Block 03A & Projektscope
 * Technische Analogie: Entspricht der 'GameSession' aus Block 05A (User-Interaktion).
 * Es ist die Verbindungstabelle zwischen User und Film mit Zusatzdaten (Rating, Kommentar).
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Beziehung 1: Der Verfasser der Review.
    // ! FetchType.EAGER: Autor-Daten werden oft direkt mit der Review benötigt (Anzeige im Frontend).
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    // Beziehung 2: Der bewertete Film.
    // FetchType.LAZY: Filmdaten nur laden, wenn explizit angefordert.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // Bewertungsskala 1-10 (gemäß DataInitializer und Anforderung).
    @Column(nullable = false)
    private int rating;

    // Optionaler Text-Kommentar (länger als Standard).
    @Column(length = 2000)
    private String comment;

    // Zeitstempel der Erstellung (wichtig für Sortierung/Feeds).
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Default Konstruktor für JPA.
    public Review() {
    }

    // Konstruktor passend zum DataInitializer.
    public Review(AppUser user, Movie movie, int rating, String comment) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now(); // Setzt aktuellen Zeitstempel automatisch.
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