package com.wiss.cinebase.controller;

// Importiert das DTO für den Film-Datentransfer.
import com.wiss.cinebase.dto.MovieDTO;
// Importiert den Service für die Geschäftslogik.
import com.wiss.cinebase.service.MovieService;

// Importiert Swagger-Annotationen für die API-Dokumentation (Block 05B).
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

// Importiert Validierungs-Annotationen.
import jakarta.validation.Valid;

// Importiert Spring Web Annotationen.
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// Importiert Security Annotationen für Method Security (Block 02B/06B).
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller für die Verwaltung von Filmen (CRUD).
 * Quellen:
 * - Controller Basics: Block 03B
 * - Method Security: Block 02B & 06B
 * - API Dokumentation: Block 05B (Swagger)
 */
@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies", description = "Verwaltung der Filme (Katalog)")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Ruft eine Liste aller verfügbaren Filme ab.
     * Zugriff: Sowohl ADMIN als auch USER (akkreditierte Journalisten).
     */
    @GetMapping
    @Operation(summary = "Alle Filme abrufen", description = "Gibt eine Liste aller verfügbaren Filme zurück.")
    @ApiResponse(responseCode = "200", description = "Erfolgreich abgerufen")
    // ! Security: Erzwingt Login. Auch wenn SecurityConfig "permitAll" sagt, gewinnt hier die Annotation.
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    /**
     * Ruft Details zu einem spezifischen Film ab.
     * Zugriff: ADMIN und USER.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Film nach ID abrufen", description = "Gibt die Details eines einzelnen Films zurück.")
    @ApiResponse(responseCode = "200", description = "Film gefunden")
    @ApiResponse(responseCode = "404", description = "Film nicht gefunden")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<MovieDTO> getMovieById(
            @Parameter(description = "ID des Films", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    /**
     * Erstellt einen neuen Film.
     * Zugriff: Nur ADMIN (Festivalleitung).
     */
    @PostMapping
    @Operation(summary = "Neuen Film erstellen", description = "Fügt einen neuen Film zur Datenbank hinzu.")
    @ApiResponse(responseCode = "201", description = "Film erfolgreich erstellt")
    @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten")
    // ! Security: Strikte Trennung – Journalisten dürfen keine Filme anlegen.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> createMovie(
            @Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO createdMovie = movieService.createMovie(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    /**
     * Aktualisiert einen bestehenden Film.
     * Zugriff: Nur ADMIN.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Film aktualisieren", description = "Aktualisiert die Daten eines bestehenden Films.")
    @ApiResponse(responseCode = "200", description = "Film erfolgreich aktualisiert")
    @ApiResponse(responseCode = "404", description = "Film nicht gefunden")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> updateMovie(
            @Parameter(description = "ID des Films", required = true)
            @PathVariable Long id,
            @Valid @RequestBody MovieDTO movieDTO) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDTO));
    }

    /**
     * Löscht einen Film.
     * Zugriff: Nur ADMIN.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Film löschen", description = "Entfernt einen Film unwiderruflich aus der Datenbank.")
    @ApiResponse(responseCode = "204", description = "Film erfolgreich gelöscht")
    @ApiResponse(responseCode = "404", description = "Film nicht gefunden")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "ID des Films", required = true)
            @PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}