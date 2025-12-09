package com.wiss.cinebase.service;

import com.wiss.cinebase.dto.MovieDTO; // WICHTIG: DTO importieren
import com.wiss.cinebase.entity.Movie;
import com.wiss.cinebase.repository.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Unit Test für die Business-Logik.
 * Prüfungsanforderung: Logik-Test mit Mockito.
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository; // Das Repo liefert Entities

    @InjectMocks
    private MovieService movieService; // Der Service liefert DTOs

    @Test
    @DisplayName("Sollte alle Filme als DTOs zurückgeben")
    void testGetAllMovies() {
        // 1. Arrange (Vorbereiten)
        // Das Repository gibt echte Entities zurück (simuliert)
        Movie m1 = new Movie("Inception", "Traum im Traum", "Sci-Fi", 2010, "Nolan", 8.8, null);
        Movie m2 = new Movie("Matrix", "Rote oder blaue Pille", "Sci-Fi", 1999, "Wachowski", 8.7, null);

        when(movieRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        // 2. Act (Ausführen)
        // WICHTIG: Der Service gibt jetzt MovieDTO zurück, nicht Movie!
        List<MovieDTO> result = movieService.getAllMovies();

        // 3. Assert (Prüfen)
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitle());

        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Sollte einen einzelnen Film nach ID finden")
    void testGetMovieById() {
        // 1. Arrange
        Long movieId = 1L;
        Movie movie = new Movie("Alien", "Horror im All", "Sci-Fi", 1979, "Scott", 8.5, null);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        // 2. Act
        // WICHTIG: Auch hier erwarten wir ein DTO
        MovieDTO result = movieService.getMovieById(movieId);

        // 3. Assert
        assertEquals("Alien", result.getTitle());
        verify(movieRepository, times(1)).findById(movieId);
    }
}