package com.wiss.cinebase.repository;

import com.wiss.cinebase.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository für Film-Operationen.
 * Quelle: Block 04A & 05A
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Generiert: SELECT * FROM movies WHERE genre = ?
    List<Movie> findByGenre(String genre);

    // ! Property Expression: Spring erkennt 'CreatedBy' als Feld in Movie und '_Id' als Feld im User.
    // Generiert einen automatischen JOIN:
    // SELECT m.* FROM movies m JOIN app_users u ON m.created_by_user_id = u.id WHERE u.id = ?
    // Wichtig für das Admin-Dashboard ("Meine erstellten Filme").
    List<Movie> findByCreatedBy_Id(Long id);

    // Generiert eine Suche mit Wildcards (LIKE %title%).
    // Ignoriert Groß-/Kleinschreibung (Lower/Upper Case).
    // Quelle: Block 05A - Advanced Query Methods
    List<Movie> findByTitleContainingIgnoreCase(String title);
}