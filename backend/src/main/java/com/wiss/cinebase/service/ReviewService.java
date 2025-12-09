package com.wiss.cinebase.service;

import com.wiss.cinebase.dto.ReviewDTO;
import com.wiss.cinebase.entity.AppUser;
import com.wiss.cinebase.entity.Movie;
import com.wiss.cinebase.entity.Review;
import com.wiss.cinebase.exception.MovieNotFoundException;
import com.wiss.cinebase.mapper.ReviewMapper;
import com.wiss.cinebase.repository.AppUserRepository;
import com.wiss.cinebase.repository.MovieRepository;
import com.wiss.cinebase.repository.ReviewRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // NEU: Wichtig fuer das Erstellungsdatum
import java.util.List;

/**
 * Service für die Verwaltung von Bewertungen.
 * Enthält die Business-Logik für das Erstellen und Abrufen von Reviews.
 * Konzepte:
 * - @Transactional (Block 05A): Garantiert Datenkonsistenz
 * - SecurityContext (Block 02B): Ermittlung des aktuellen Users
 */

// ! Hier implementieren wir zwei wichtige fachliche Regeln:
// ! Security: Der Autor der Bewertung ist immer der aktuell eingeloggte User (aus dem SecurityContext). Man kann nicht im Namen anderer posten.
// ! Business Rule: Ein User darf jeden Film nur einmal bewerten. Das verhindern wir hier im Service.

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final AppUserRepository appUserRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         MovieRepository movieRepository,
                         AppUserRepository appUserRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * Fügt eine neue Bewertung hinzu.
     * @param reviewDTO Die Daten der Bewertung
     * @return Die gespeicherte Bewertung als DTO
     * @throws IllegalArgumentException wenn der User den Film bereits bewertet hat
     */
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        // 1. Eingeloggten User ermitteln (Security Context)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User nicht gefunden: " + username));

        // 2. Film laden
        Movie movie = movieRepository.findById(reviewDTO.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException(reviewDTO.getMovieId()));

        // 3. Business Rule: Darf der User diesen Film noch bewerten?
        // Quelle: Block 05B - Logik-Checks vor dem Speichern
        if (reviewRepository.existsByUserIdAndMovieId(currentUser.getId(), movie.getId())) {
            throw new IllegalArgumentException("Sie haben diesen Film bereits bewertet!");
        }

        // 4. Mapping und Verknüpfung
        Review review = ReviewMapper.toEntity(reviewDTO);
        review.setUser(currentUser);
        review.setMovie(movie);

        // --- FEHLERBEHEBUNG ---
        // Das Datum muss gesetzt werden, da es in der Datenbank Pflichtfeld ist (nullable = false)
        review.setCreatedAt(LocalDateTime.now());
        // ---------------------

        // 5. Speichern
        Review savedReview = reviewRepository.save(review);

        return ReviewMapper.toDTO(savedReview);
    }

    /**
     * Lädt alle Bewertungen zu einem Film.
     */
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByMovie(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException(movieId);
        }
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        return ReviewMapper.toDTOList(reviews);
    }

    /**
     * Berechnet die Durchschnittsbewertung für einen Film.
     */
    @Transactional(readOnly = true)
    public Double getAverageRating(Long movieId) {
        // Nutzt die Custom Query aus dem Repository (Block 05B)
        Double average = reviewRepository.getAverageRatingForMovie(movieId);
        // Wenn keine Bewertungen da sind, geben wir 0.0 zurück
        return average != null ? average : 0.0;
    }
}
