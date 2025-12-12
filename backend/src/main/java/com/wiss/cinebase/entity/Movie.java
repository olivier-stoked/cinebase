package com.wiss.cinebase.entity;

// Verhindert Zirkelbezüge beim Senden von Objekten als JSON.
import com.fasterxml.jackson.annotation.JsonIgnore;

// Quelle: Block 03A - ORM Mapping
// JPA-Annotationen für das Datenbank-Mapping.
import jakarta.persistence.*;

// Import für Listen-Strukturen (OneToMany Beziehungen).
import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert einen Film im Festivalkatalog.
 * Quelle: Block 03A & Projektscope
 * Multi-User Aspekt:
 * - Das Feld 'createdBy' verknüpft den Film mit dem Admin, der ihn erstellt hat.
 * - Dies ermöglicht Rückverfolgbarkeit und rollenbasierte Datenfilterung.
 */
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // Längere Beschreibung für den Filminhalt. Besser 1000 statt 255 (Standard VARCHAR).
    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String genre;

    // Mapping in der Datenbank auf 'release_year' (Snake-Case) von Java-Variable 'releaseYear' (Camel-Case).
    @Column(name = "release_year", nullable = false)
    private int releaseYear;

    @Column(nullable = false)
    private String director;

    // Basis-Rating oder Durchschnittswert.
    private double rating;

    // ! Multi-User Erweiterung
    // Kardinalität: Viele Filme (Many) gehören zu einem User (One).
    // ! fetch = FetchType.LAZY: Performance Schalter - Java lädt nur die Filmdaten und lässt den User (das Feld
    // ! createdBy) frei. Erst wenn die Funktion .getCreatedBy() aufgerufen wird, lädt Java die Daten.
    // Vorteil: Ressourcenschonend, App läuft schneller, Website lädt schneller.
    // Gegenteil EAGER: Bei EAGER würde Java beim Laden eines Films sofort eine zweite DB-Abfrage starten.
    @ManyToOne(fetch = FetchType.LAZY)
    // ! Verbindung innerhalb der DB. In der Tabelle "movies" wird die Spalte 'created_by_user_id' angelegt.
    // ! Diese ist der Foreign Key auf die 'id'-Spalte der Tabelle "app_users".
    @JoinColumn(name = "created_by_user_id")
    @JsonIgnore // ! User-Details nicht bei jedem Film-Abruf mitsenden (Datenschutz & Performance).
    private AppUser createdBy;

    // ! Definition der Reviews-Beziehung
    // Ein Film hat viele Reviews.
    // cascade = CascadeType.ALL: Löscht man den Film, werden automatisch alle Reviews mitgelöscht.
    // orphanRemoval = true: Entfernt man ein Review aus der Liste, verschwindet es auch aus der DB.
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // ! Reviews separat laden (Lazy Loading), nicht automatisch mitsenden.
    private List<Review> reviews = new ArrayList<>();

    // Leerer Konstruktor (Pflicht für JPA).
    public Movie() {
    }

    // ! Konstruktor ohne ID (DB erstellt diese für den Film automatisch).
    public Movie(String title, String description, String genre, int releaseYear, String director, double rating, AppUser createdBy) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.director = director;
        this.rating = rating;
        this.createdBy = createdBy;
    }

    // --- Getter & Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}