package com.wiss.cinebase.controller;

// Importiert das Review DTO.
import com.wiss.cinebase.dto.ReviewDTO;
// Importiert den Review Service.
import com.wiss.cinebase.service.ReviewService;

// Importiert Swagger Annotationen.
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

// Importiert Validierung.
import jakarta.validation.Valid;

// Importiert Spring Web & Security.
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller für Bewertungen (Reviews).
 * Stellt Endpunkte bereit, um Reviews zu schreiben und zu lesen.
 *
 * Quellen:
 * - Controller Basics: Block 03B
 * - Security: Block 02B (isAuthenticated Check)
 * - Swagger: Block 05B
 */
@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Bewertungen und Kommentare")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Fügt eine neue Bewertung hinzu.
     * Zugriff: Jeder eingeloggte User (Journalisten & Admins).
     * Das Review wird automatisch mit dem User verknüpft (siehe ReviewService).
     */
    @PostMapping
    @Operation(summary = "Neue Bewertung abgeben", description = "Erstellt eine Review für einen Film. Ein User kann jeden Film nur einmal bewerten.")
    @PreAuthorize("isAuthenticated()") // ! Security: Anonyme Gäste dürfen nicht bewerten.
    public ResponseEntity<ReviewDTO> addReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.addReview(reviewDTO);
        return ResponseEntity.ok(createdReview);
    }

    /**
     * Lädt alle Bewertungen für einen bestimmten Film.
     * Zugriff: Jeder eingeloggte User.
     */
    @GetMapping("/movie/{movieId}")
    @Operation(summary = "Bewertungen eines Films laden")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(reviewService.getReviewsByMovie(movieId));
    }

    /**
     * Lädt die Durchschnittsbewertung für einen Film.
     * Zugriff: Jeder eingeloggte User.
     */
    @GetMapping("/movie/{movieId}/average")
    @Operation(summary = "Durchschnittsbewertung abrufen", description = "Gibt den Durchschnitt (1-10) aller Bewertungen zurück.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long movieId) {
        return ResponseEntity.ok(reviewService.getAverageRating(movieId));
    }
}