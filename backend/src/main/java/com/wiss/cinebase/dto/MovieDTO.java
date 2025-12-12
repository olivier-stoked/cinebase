package com.wiss.cinebase.dto;

// Importiert Validierungs-Annotationen für numerische Grenzen (Min/Max) und Textregeln.
// Quelle: Block 06A - Form Validation Basics
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object für Filme.
 * Dient als Transportschicht zwischen Backend und Frontend.
 * Quelle: Block 04A - DTO Pattern
 * Validation: Block 06A - Form Validation Basics
 * Besonderheit:
 * Enthält das Feld 'averageRating', welches nicht direkt in der Movie-Tabelle gespeichert,
 * sondern zur Laufzeit aus den Reviews berechnet wird.
 */
public class MovieDTO {

    private Long id;

    // Validierung: Verhindert das Speichern unvollständiger Datensätze.
    @NotBlank(message = "Der Filmtitel darf nicht leer sein")
    private String title;

    @Size(max = 1000, message = "Die Beschreibung darf maximal 1000 Zeichen lang sein")
    private String description;

    @NotBlank(message = "Genre ist erforderlich")
    private String genre;

    @Min(value = 1888, message = "Filme können erst ab 1888 existieren")
    private int releaseYear;

    @NotBlank(message = "Regisseur ist erforderlich")
    private String director;

    // Basis-Rating (z.B. Jury-Wertung)
    @Min(value = 0, message = "Rating muss mindestens 0 sein")
    @Max(value = 10, message = "Rating darf maximal 10 sein")
    private double rating;

    // Berechneter Durchschnitt der User-Reviews (wird vom Service gesetzt)
    private Double averageRating;

    // Default Konstruktor für Frameworks (Jackson)
    public MovieDTO() {
    }

    public MovieDTO(Long id, String title, String description, String genre, int releaseYear, String director, double rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.director = director;
        this.rating = rating;
        this.averageRating = 0.0; // Standard-Initialisierung
    }

    // Getter und Setter

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
}