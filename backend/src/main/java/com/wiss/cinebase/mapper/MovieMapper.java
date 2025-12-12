package com.wiss.cinebase.mapper;

// Importiert das Data Transfer Object für Filme.
import com.wiss.cinebase.dto.MovieDTO;
// Importiert die Entity-Klasse für Filme.
import com.wiss.cinebase.entity.Movie;

// Importiert Listen-Utilities.
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper für die Konvertierung zwischen Movie Entity und MovieDTO.
 * Quelle: Block 04A - Mapper Pattern
 * Zweck: Entkopplung der internen Datenbankstruktur von der externen API.
 */
public class MovieMapper {

    /**
     * Konvertiert eine Database-Entity in ein API-DTO.
     * @param movie Die zu konvertierende Entity.
     * @return Das resultierende DTO oder null.
     */
    public static MovieDTO toDTO(Movie movie) {
        if (movie == null) {
            return null;
        }

        // ! Das Feld averageRating wird hier noch nicht gesetzt (Standard 0.0).
        // ! Dies erfolgt nachträglich im MovieService durch eine separate Datenbankabfrage.
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

    /**
     * Konvertiert ein API-DTO in eine Database-Entity.
     * @param dto Das zu konvertierende DTO.
     * @return Die resultierende Entity oder null.
     */
    public static Movie toEntity(MovieDTO dto) {
        if (dto == null) {
            return null;
        }

        // ! ID und User (createdBy) werden hier NICHT gesetzt.
        // ! Die ID wird von der Datenbank generiert.
        // ! Der User (createdBy) wird im MovieService aus dem SecurityContext geholt.
        // Der Mapper kümmert sich ausschließlich um die reinen Datenfelder.
        return new Movie(
                dto.getTitle(),
                dto.getDescription(),
                dto.getGenre(),
                dto.getReleaseYear(),
                dto.getDirector(),
                dto.getRating(),
                null // User wird im Service gesetzt
        );
    }

    /**
     * Hilfsmethode für Listen.
     * Wandelt eine Liste von Movie-Entities in eine Liste von MovieDTOs um.
     * @param movies Liste der Entities.
     * @return Liste der DTOs.
     */
    public static List<MovieDTO> toDTOList(List<Movie> movies) {
        return movies.stream()
                .map(MovieMapper::toDTO)
                .collect(Collectors.toList());
    }
}