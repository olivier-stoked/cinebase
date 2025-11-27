package com.wiss.cinebase.repository;

import com.wiss.cinebase.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // * Spring analysiert findByGenre. Automatischer Bau der DB-Abfrage: Spring weiß, dass die entity Movie ein Feld genre hat.
    // Spring: lediglich Methodenname wird definiert. Spring generiert das SQL.
    // SELECT * FROM movies WHERE genre = ?
    List<Movie> findByGenre(String genre);

    // SELECT * FROM movies WHERE created_by_user_id = ?
    // ! Wichtig für das Dashboard: Filmanzeige
    List<Movie> findByCreatedBy_Id(Long id);

}
