package com.wiss.cinebase.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // Längere Beschreibung für den Filminhalt. Besser 1000 statt 255 (VARCHAR).
    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String genre;

    // Mapping in der Datenbank auf release_year von Java Schreibweise releaseYear.
    @Column(name = "release_year", nullable = false)
    private int releaseYear;

    @Column(nullable = false)
    private String director;

    private double rating;

    // ! Multi-User Erweiterung
    // Kardinalität: Viele Filme (Many) gehören zu einem User (One).
    // ! fetch = FetchType.LAZY: Performance Schalter.
    // * EAGER
    // FetchType.LAZY: Der User wird erst geladen, wenn er explizit angefragt wird.
    @ManyToOne(fetch = FetchType.LAZY)
    // ! Verbindung innerhalb der DB
    @JoinColumn(name = "created_by_user_id")
    private AppUser createdBy;


    // Leerer Konstruktor (Pflicht für JPA).
    public Movie() {
    }

    // ! Konstruktor ohne ID (DB erstellt diese für die Film automatisch)
    public Movie(String title, String description, String genre, int releaseYear, String director, double rating, AppUser createdBy) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.director = director;
        this.rating = rating;
        this.createdBy = createdBy;
    }

    // Getter und Setter.
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
}
