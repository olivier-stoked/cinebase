package com.wiss.cinebase.service;

// Importiert das Datentransferobjekt für Reviews (Entkopplung von der API).
import com.wiss.cinebase.dto.ReviewDTO;

// Importiert die JPA-Entities für Datenbankoperationen.
// Quelle: Block 03A - Entities
import com.wiss.cinebase.entity.AppUser;
import com.wiss.cinebase.entity.Movie;
import com.wiss.cinebase.entity.Review;

// Importiert spezifische Exceptions für Fehlerfälle (z.B. 404 Not Found).
import com.wiss.cinebase.exception.MovieNotFoundException;

// Importiert den Mapper zur Umwandlung zwischen Entity und DTO.
import com.wiss.cinebase.mapper.ReviewMapper;

// Importiert die Repositories für den Datenzugriff.
// Quelle: Block 04A - Repositories
import com.wiss.cinebase.repository.AppUserRepository;
import com.wiss.cinebase.repository.MovieRepository;
import com.wiss.cinebase.repository.ReviewRepository;

// Importiert den SecurityContext, um den aktuell eingeloggten Benutzer zu ermitteln.
// Quelle: Block 02B - Security Context
import org.springframework.security.core.context.SecurityContextHolder;

// Importiert Exceptions aus dem Security-Paket.
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Importiert Spring-Annotationen für die Service-Definition und Transaktionssteuerung.
// Quelle: Block 05A - Transaktionen
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Importiert Klassen für Zeitstempel.
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service für die Verwaltung von Bewertungen.
 * Enthält die Business-Logik für das Erstellen und Abrufen von Reviews.
 * Konzepte:
 * - @Transactional (Block 05A): Garantiert Datenkonsistenz (ACID).
 * - SecurityContext (Block 02B): Ermittlung des aktuellen Users ohne Frontend-Parameter.
 */
@Service
@Transactional // ! Jede Methode läuft in einer Transaktion. Bei Fehlern erfolgt ein automatischer Rollback.
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
     * ! Business Rules:
     * 1. Der Autor ist immer der eingeloggte User (aus dem Security Context).
     * 2. Ein User darf jeden Film nur einmal bewerten (Vermeidung von Spam/Manipulation).
     * @param reviewDTO Die Daten der Bewertung
     * @return Die gespeicherte Bewertung als DTO
     */
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        // 1. Eingeloggten User ermitteln (Security Context)
        // ! Wir vertrauen nicht dem Frontend, sondern dem Token im SecurityContext.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User nicht gefunden: " + username));

        // 2. Film laden
        Movie movie = movieRepository.findById(reviewDTO.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException(reviewDTO.getMovieId()));

        // 3. Validierung: Doppelte Bewertung verhindern (Block 05B Logik)
        if (reviewRepository.existsByUserIdAndMovieId(currentUser.getId(), movie.getId())) {
            throw new IllegalArgumentException("Sie haben diesen Film bereits bewertet!");
        }

        // 4. Mapping und Verknüpfung
        Review review = ReviewMapper.toEntity(reviewDTO);
        review.setUser(currentUser);
        review.setMovie(movie);
        review.setCreatedAt(LocalDateTime.now()); // ! Wichtig: Zeitstempel serverseitig setzen.

        // 5. Speichern
        Review savedReview = reviewRepository.save(review);

        return ReviewMapper.toDTO(savedReview);
    }

    /**
     * Lädt alle Bewertungen zu einem Film.
     */
    @Transactional(readOnly = true) // ! Performance-Optimierung für Lesezugriffe.
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
        // Nutzt die Custom Query (AVG) aus dem Repository (Block 05B)
        // ! Datenbankgestützte Aggregation ist performanter als Berechnung in Java.
        Double average = reviewRepository.getAverageRatingForMovie(movieId);
        return average != null ? average : 0.0;
    }
}