package com.wiss.cinebase.repository;

import com.wiss.cinebase.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für Bewertungen.
 * Enthält Custom Queries für Statistiken (Durchschnitt).
 * Quelle: Block 05B - Custom Queries
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 1. Alle Reviews zu einem bestimmten Film finden (z.B. für die Detailansicht im Frontend)
    List<Review> findByMovieId(Long movieId);

    // 2. Alle Reviews eines bestimmten Users finden (für "Meine Rezensionen" Dashboard)
    List<Review> findByUserId(Long userId);

    /**
     * Berechnet die Durchschnittsbewertung für einen Film direkt in der Datenbank.
     * Nutzt die SQL-Funktion AVG().
     *
     * @param movieId Die ID des Films
     * @return Der Durchschnittswert (kann null sein, wenn keine Reviews existieren)
     */

    // ! @Query("SELECT AVG..."): Hier greifen wir manuell ein. Wir lassen die Datenbank rechnen. Das Ergebnis ist
    // ! ein Double (z.B. 7.5), den wir später im Frontend anzeigen können. Das erfüllt Ihre Anforderung nach der Durchschnittsberechnung.

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId")
    Double getAverageRatingForMovie(Long movieId);

    // Prüfen, ob User diesen Film schon bewertet hat (verhindert doppelte Bewertungen/Spam)
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
}
