package com.wiss.cinebase.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {


    // ! Movie steht und ist mit User verknüpft. !!!!!


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
    // ! fetch = FetchType.LAZY: Performance Schalter - Java lädt nur die Filmdaten und lässt den User (das Feld
    // ! createdby) frei. Erst wenn die Funktion .getCreatedBy() aufgerufen wird, lädt Java die Daten. Vorteil: ressourcenarm,
    // ! weniger Befehle, die App läuft schneller, der Server hat weniger zu tun, die Website lädt schneller.
    // * Gegenteil EAGER: bei EAGER würde Java beim Laden eines Films eine zweite DB-Abfrage starten, um den User zu laden (Film Details).
    // FetchType.LAZY: Der User wird erst geladen, wenn er explizit angefragt wird.
    @ManyToOne(fetch = FetchType.LAZY)

    // ! Verbindung innerhalb der DB. In der Tabelle "movies" wird die Spalte created_by_user_id angelegt. Die kreierte Zahl
    // ! ist der Foreign Key (der auf die id-Spalte der Tabelle "app_users" zeigt)
    // Verknüpfung in der DB-Tabelle ist sauber benannt. SQL ist tabellenartig und enthält keine Objekte. User wird via ID mit dem Film
    // verbunden. Erleichtert die DB-Struktur und die SQL-Inspektion der DB via DBeaver/Postman.
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
