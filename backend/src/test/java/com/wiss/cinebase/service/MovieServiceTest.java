package com.wiss.cinebase.service;

// Importiert DTOs und Entities.
import com.wiss.cinebase.dto.MovieDTO;
import com.wiss.cinebase.entity.Movie;
// Importiert die benötigten Repositories (Dependencies des Services).
import com.wiss.cinebase.repository.AppUserRepository;
import com.wiss.cinebase.repository.MovieRepository;
import com.wiss.cinebase.repository.ReviewRepository;
// Importiert JUnit und Mockito für Unit-Tests.
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
 * Unit-Tests für die Business-Logik der Film-Verwaltung.
 * Isoliert den Service durch Mocking der Datenbank-Schicht.
 * Quelle: Block 06B - Backend Testing mit Mockito
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository; // Simuliert den Datenzugriff auf Filme.

    @Mock
    private ReviewRepository reviewRepository; // ! Wichtig: Wird für die Durchschnittsberechnung benötigt.

    @Mock
    private AppUserRepository appUserRepository; // ! Wichtig: Wird für die Konstruktor-Injektion benötigt.

    @InjectMocks
    private MovieService movieService; // Die zu testende Klasse (Mocks werden hier injiziert).

    @Test
    @DisplayName("Sollte alle Filme als DTOs zurückgeben und Durchschnitt berechnen")
    void testGetAllMovies() {
        // 1. Arrange (Vorbereiten der Mocks)
        Movie m1 = new Movie("Inception", "Traum im Traum", "Sci-Fi", 2010, "Nolan", 8.8, null);
        m1.setId(1L); // ID simulieren
        Movie m2 = new Movie("Matrix", "Rote oder blaue Pille", "Sci-Fi", 1999, "Wachowski", 8.7, null);
        m2.setId(2L);

        // Simulieren der Datenbankantworten
        when(movieRepository.findAll()).thenReturn(Arrays.asList(m1, m2));
        // Simulieren der Durchschnittsberechnung (aus ReviewRepository)
        when(reviewRepository.getAverageRatingForMovie(1L)).thenReturn(9.0);
        when(reviewRepository.getAverageRatingForMovie(2L)).thenReturn(8.5);

        // 2. Act (Ausführen der Logik)
        List<MovieDTO> result = movieService.getAllMovies();

        // 3. Assert (Prüfen der Ergebnisse)
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        // Prüfen, ob der Durchschnitt korrekt im DTO gesetzt wurde
        assertEquals(9.0, result.get(0).getAverageRating());

        // Verifizieren, dass die Repository-Methoden aufgerufen wurden
        verify(movieRepository, times(1)).findAll();
        verify(reviewRepository, times(1)).getAverageRatingForMovie(1L);
    }

    @Test
    @DisplayName("Sollte einen einzelnen Film nach ID finden")
    void testGetMovieById() {
        // 1. Arrange
        Long movieId = 1L;
        Movie movie = new Movie("Alien", "Horror im All", "Sci-Fi", 1979, "Scott", 8.5, null);
        movie.setId(movieId);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(reviewRepository.getAverageRatingForMovie(movieId)).thenReturn(8.5);

        // 2. Act
        MovieDTO result = movieService.getMovieById(movieId);

        // 3. Assert
        assertEquals("Alien", result.getTitle());
        assertEquals(8.5, result.getAverageRating());
        verify(movieRepository, times(1)).findById(movieId);
    }
}