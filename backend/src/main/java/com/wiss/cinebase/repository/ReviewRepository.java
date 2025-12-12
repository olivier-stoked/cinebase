package com.wiss.cinebase.repository;

import com.wiss.cinebase.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für Bewertungen.
 * Enthält Custom Queries für statistische Auswertungen.
 *
 * Quelle: Block 05B - Custom Queries & Aggregation
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 1. Alle Reviews zu einem bestimmten Film finden (z.B. für die Detailansicht).
    List<Review> findByMovieId(Long movieId);

    // 2. Alle Reviews eines bestimmten Users finden (für "Meine Rezensionen" Dashboard).
    List<Review> findByUserId(Long userId);

    // ! Custom Query mit JPQL (Java Persistence Query Language).
    // Hier wird nicht auf Tabellen, sondern auf Java-Objekte (Review r) referenziert.
    // Die Datenbank übernimmt die Berechnung des Durchschnitts (AVG), was performanter ist,
    // als alle Reviews zu laden und in Java zu berechnen.
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId")
    Double getAverageRatingForMovie(Long movieId);

    // Prüft, ob eine Kombination aus User und Film bereits existiert.
    // Verhindert doppelte Bewertungen (Business-Regel).
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
}