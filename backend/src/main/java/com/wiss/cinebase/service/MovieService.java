package com.wiss.cinebase.service;

// Importiert DTOs für den Datentransfer zwischen Controller und Service.
import com.wiss.cinebase.dto.MovieDTO;
// Importiert Entities für die Datenbankinteraktion.
import com.wiss.cinebase.entity.AppUser;
import com.wiss.cinebase.entity.Movie;
// Importiert Exceptions für Fehlerbehandlung.
import com.wiss.cinebase.exception.MovieNotFoundException;
// Importiert Mapper zur Umwandlung von Entity <-> DTO.
import com.wiss.cinebase.mapper.MovieMapper;
// Importiert Repositories für Datenzugriff.
import com.wiss.cinebase.repository.AppUserRepository;
import com.wiss.cinebase.repository.MovieRepository;
import com.wiss.cinebase.repository.ReviewRepository;

// Importiert Security-Klassen für den Zugriff auf den eingeloggten User.
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// Importiert Spring Service Annotationen.
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service für die Verwaltung von Filmen.
 * Quelle: Block 01B (Service Layer) & Block 05A (Transactions)
 * Verantwortlichkeiten:
 * - CRUD-Operationen für Filme.
 * - Multi-User: Verknüpfung von Filmen mit dem erstellenden Admin.
 * - Integration von Durchschnittsbewertungen aus dem ReviewRepository.
 */
@Service
@Transactional // ! Jede Methode läuft in einer Transaktion (ACID-Prinzip: Alles oder nichts).
public class MovieService {

    private final MovieRepository movieRepository;
    private final AppUserRepository appUserRepository;
    private final ReviewRepository reviewRepository;

    public MovieService(MovieRepository movieRepository,
                        AppUserRepository appUserRepository,
                        ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.appUserRepository = appUserRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Erstellt einen neuen Film und speichert ihn in der Datenbank.
     * ! Multi-User Aspekt:
     * Der Film wird automatisch mit dem aktuell eingeloggten Benutzer (Admin) verknüpft.
     * Wir holen den User aus dem SecurityContext, nicht vom Frontend (Sicherheit!).
     * @param movieDTO Die Daten des neuen Films
     * @return Der gespeicherte Film als DTO
     */
    public MovieDTO createMovie(MovieDTO movieDTO) {
        // 1. Eingeloggten Admin ermitteln
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser currentUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User nicht gefunden: " + username));

        // 2. Mapping DTO -> Entity
        Movie movie = MovieMapper.toEntity(movieDTO);

        // 3. Beziehung setzen (Film gehört zu diesem Admin)
        movie.setCreatedBy(currentUser);

        // 4. Speichern
        Movie savedMovie = movieRepository.save(movie);

        return MovieMapper.toDTO(savedMovie);
    }

    /**
     * Ruft eine Liste aller verfügbaren Filme ab.
     * ! Performance: readOnly=true optimiert die Datenbankabfrage (kein Dirty Checking).
     * ! Logik: Für jeden Film wird live der Durchschnittswert aus der Review-Tabelle berechnet.
     */
    @Transactional(readOnly = true)
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movie -> {
                    // Entity zu DTO
                    MovieDTO dto = MovieMapper.toDTO(movie);

                    // Durchschnittsbewertung laden (Quelle: Block 05B Custom Queries)
                    Double avg = reviewRepository.getAverageRatingForMovie(movie.getId());
                    dto.setAverageRating(avg != null ? avg : 0.0);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Sucht einen spezifischen Film anhand seiner ID.
     */
    @Transactional(readOnly = true)
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        MovieDTO dto = MovieMapper.toDTO(movie);

        // Auch beim Einzelabruf den aktuellen Durchschnitt setzen
        Double avg = reviewRepository.getAverageRatingForMovie(movie.getId());
        dto.setAverageRating(avg != null ? avg : 0.0);

        return dto;
    }

    /**
     * Aktualisiert einen bestehenden Film.
     */
    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        // Felder aktualisieren
        existingMovie.setTitle(movieDTO.getTitle());
        existingMovie.setDescription(movieDTO.getDescription());
        existingMovie.setGenre(movieDTO.getGenre());
        existingMovie.setReleaseYear(movieDTO.getReleaseYear());
        existingMovie.setDirector(movieDTO.getDirector());
        existingMovie.setRating(movieDTO.getRating()); // Basis-Rating der Jury

        // Speichern (explizit, obwohl @Transactional Dirty Checking macht)
        Movie updatedMovie = movieRepository.save(existingMovie);
        return MovieMapper.toDTO(updatedMovie);
    }

    /**
     * Löscht einen Film unwiderruflich.
     */
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new MovieNotFoundException(id);
        }
        // ! Dank CascadeType.ALL in der Entity werden auch alle Reviews gelöscht.
        movieRepository.deleteById(id);
    }
}