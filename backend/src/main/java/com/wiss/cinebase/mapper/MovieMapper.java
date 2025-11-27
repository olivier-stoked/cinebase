package com.wiss.cinebase.mapper;

import com.wiss.cinebase.dto.MovieDTO;
import com.wiss.cinebase.entity.Movie;

import java.util.List;
import java.util.stream.Collectors;

// Übersetzung zwischen DB (Movie) und API (MovieDTO)
public class MovieMapper {

    // Konvertiert Database-Entity zu API-DTO
    public static MovieDTO toDTO(Movie movie) {
        if (movie == null) {
            return null;
        }

        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getGenre(),
                movie.getReleaseYear(),
                movie.getDirector(),
                movie.getRating()
        );
    }

    // Konvertiert API-DTO zu Database-Entity
    public static Movie toEntity(MovieDTO dto) {
        if (dto == null) {
            return null;
        }

        // ID und User (createdBy) werden hier nicht gesetzt.
        // ! Die ID wird von der Datenbank generiert.
        // Der User (createdBy) wird im MovieService aus dem SecurityContext geholt.
        // ! KONKRETER: Mapper kümmert sich nur um Datenfelder
        return new Movie(
                dto.getTitle(),
                dto.getDescription(),
                dto.getGenre(),
                dto.getReleaseYear(),
                dto.getDirector(),
                dto.getRating(),
                null
        );
    }

    // ! Hilfsmethode für Listen (wandelt eine Liste von Movies in eine Liste von DTOs um)
    // static: damit der Mapper im Service direkt aufgerufen werden kann ohne ihn mit "new" erstellen zu müssen
    // Stardard bei Benutzung reiner Hilfsklassen
    public static List<MovieDTO> toDTOList(List<Movie> movies) {
        return movies.stream()
                .map(MovieMapper::toDTO)
                .collect(Collectors.toList());
    }
}
