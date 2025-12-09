package com.wiss.cinebase.repository;

import com.wiss.cinebase.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// extends JpaRepository bekommen wir Methoden wie .save(), .findAll() und .delete() geschenkt.
// Die Methode findByCreatedBy_Id ist ein mächtiges Feature von Spring Data: Es versteht, dass wir im Movie das Feld createdBy haben (den User), und dieser User eine id hat. Daraus baut es den SQL-Join automatisch.

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // * Spring analysiert findByGenre. Automatischer Bau der DB-Abfrage: Spring weiß, dass die entity Movie ein Feld genre hat.
    // Spring: lediglich Methodenname wird definiert. Spring generiert das SQL.
    // SELECT * FROM movies WHERE genre = ?
    List<Movie> findByGenre(String genre);

    // SELECT * FROM movies WHERE created_by_user_id = ?
    // ! Wichtig für das Dashboard: Filmanzeige
    List<Movie> findByCreatedBy_Id(Long id);

    // --- NEU HINZUFÜGEN ---
    // Findet Filme anhand des Titels (Teil-Übereinstimmung, Groß/Kleinschreibung egal)
    // Wird vom DataInitializer benötigt, um Duplikate zu vermeiden!
    // Quelle: Block 05A - Advanced Query Methods
    List<Movie> findByTitleContainingIgnoreCase(String title);

}
