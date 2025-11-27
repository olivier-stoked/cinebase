







package com.wiss.cinebase.controller;

import com.wiss.cinebase.dto.MovieDTO;
import com.wiss.cinebase.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ! Benötigt noch mehr Erklärungen zum Verständnis.Check geeksforgeeks !!!


    // Alle URLs beginnen mit /api/movies.
    @RestController
    @RequestMapping("/api/movies")

    @Tag(name = "Movies", description = "Verwaltung der Filme (CRUD)")

    // Der "Koch" der App
    // Security: ADMIN dürfen schreiben, USER nur lesen
    // Dokumentation: Swagger/OpenAPI - jeder Endpoint wird beschrieben, sodass er im Browser getestet werden kann
    public class MovieController {

        private final MovieService movieService;

        public MovieController(MovieService movieService) {
            this.movieService = movieService;
        }

        // Rückgabe aller Filme, entweder durch ADMIN oder USER
        @GetMapping
        @Operation(summary = "Alle Filme abrufen", description = "Gibt eine Liste aller verfügbaren Filme zurück.")
        @ApiResponse(responseCode = "200", description = "Erfolgreich abgerufen")
        @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
        public ResponseEntity<List<MovieDTO>> getAllMovies() {
            return ResponseEntity.ok(movieService.getAllMovies());
        }

        // Rückgabe eines Films, entweder durch ADMIN oder USER
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

        // Erstellung eines Films, nur durch ADMIN
        @PostMapping
        @Operation(summary = "Neuen Film erstellen", description = "Fügt einen neuen Film zur Datenbank hinzu.")
        @ApiResponse(responseCode = "201", description = "Film erfolgreich erstellt")
        @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten")
        @PreAuthorize("hasRole('ADMIN')") // Nur Admins dürfen erstellen!
        public ResponseEntity<MovieDTO> createMovie(
                @Valid @RequestBody MovieDTO movieDTO) {
            MovieDTO createdMovie = movieService.createMovie(movieDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
        }

        // Aktualisierung eines Films, nur durch ADMIN
        @PutMapping("/{id}")
        @Operation(summary = "Film aktualisieren", description = "Aktualisiert die Daten eines bestehenden Films.")
        @ApiResponse(responseCode = "200", description = "Film erfolgreich aktualisiert")
        @ApiResponse(responseCode = "404", description = "Film nicht gefunden")
        @PreAuthorize("hasRole('ADMIN')") // Nur Admins dürfen ändern!
        public ResponseEntity<MovieDTO> updateMovie(
                @Parameter(description = "ID des Films", required = true)
                @PathVariable Long id,
                @Valid @RequestBody MovieDTO movieDTO) {
            return ResponseEntity.ok(movieService.updateMovie(id, movieDTO));
        }

        // Löschung eines Films, nur durch ADMIN
        @DeleteMapping("/{id}")
        @Operation(summary = "Film löschen", description = "Entfernt einen Film unwiderruflich aus der Datenbank.")
        @ApiResponse(responseCode = "204", description = "Film erfolgreich gelöscht")
        @ApiResponse(responseCode = "404", description = "Film nicht gefunden")
        @PreAuthorize("hasRole('ADMIN')") // Nur Admins dürfen löschen!
        public ResponseEntity<Void> deleteMovie(
                @Parameter(description = "ID des Films", required = true)
                @PathVariable Long id) {
            movieService.deleteMovie(id);
            return ResponseEntity.noContent().build();
        }
    }
