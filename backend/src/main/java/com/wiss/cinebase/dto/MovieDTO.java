package com.wiss.cinebase.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


// ! Enthält die Validierung: Block 6A
// Annotationen wie @NotBlank und @Min, um Regeln festzulegen.



// DB-Entity Movie wird in ein DTO "verpackt" - das DTO für die Film-Daten
// DTO dient als Schutzschicht vor der Datenbank-Entity: Verbindung zwischen Movie und AppUser (inkl. Passwort)
// ! BESSERES VERSTÄNDNIS: Wenn JSON Daten direkt versendet werden, könnte das Passwort enthalten sein.
// @NotBlank: Validierung - Bestimmung, was der User eingeben darf, ohne die DB zu verzerren
public class MovieDTO {

    private Long id;

    // Hinzufügen der Validierungsregeln für den Controller
    // (@NotBlank) - Film kann nicht ohne Titel gespeichert werden
    @NotBlank(message = "Der Filmtitel darf nicht leer sein")
    private String title;

    @Size(max = 1000, message = "Die Beschreibung darf maximal 1000 Zeichen lang sein")
    private String description;

    @NotBlank(message = "Genre ist erforderlichj")
    private String genre;

    @Min(value = 1888, message = "Filme erst ab 1888")
    private int releaseYear;

    @NotBlank(message = "Regisseur is erforderlich")
    private String director;

    @Min(value = 0, message = "Rating muss mindestens 0 sein")
    @Max(value = 10, message = "Rating darf maximal 10 sein")
    private double rating;






    // --- NEU HINZUGEFÜGT ---
    // Dynamischer Durchschnitt der User-Reviews (wird live berechnet)

// ! Damit der Admin auch sieht, wie gut ein Film ankommt, müssen wir die Durchschnittsnote in das DTO im Backend
// ! aufnehmen. Wir fügen das Feld averageRating hinzu, damit wir den Wert transportieren können.



    private Double averageRating;
    // -----------------------

    // Konstruktoren

    public MovieDTO() {
    }

    // ! Warum dieser leere Konstruktor ???
    // Antwort: Frameworks wie Jackson (für JSON) brauchen ihn, um eine leere Instanz zu erzeugen,
    // bevor sie die Daten über Setter einfüllen. Ohne ihn gibt es Fehler beim Deserialisieren.
    public MovieDTO(Long id, String title, String description, String genre, int releaseYear, String director, double rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.director = director;
        this.rating = rating;
        // Neu: Standardwert setzen
        this.averageRating = 0.0;
    }

    // Getter und Setter

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
        this.director= director;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    // --- NEUE GETTER & SETTER ---
    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
