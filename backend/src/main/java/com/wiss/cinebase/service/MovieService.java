package com.wiss.cinebase.service;


// "Chefkoch", der die Zutaten (Daten aus dem Repository) nimmt,
// verarbeitet (Validierung, User-Zuweisung) und das fertige Gericht (DTO) serviert.
// Businesslogik der App

// ! Multi-User Aspekt: bei Erstellung des Films, welcher Admin ist gerade eingeloggt - Verknüpfung des Films mit diesem Admin.

import com.wiss.cinebase.dto.MovieDTO;
import com.wiss.cinebase.entity.AppUser;
import com.wiss.cinebase.entity.Movie;
import com.wiss.cinebase.exception.MovieNotFoundException;
import com.wiss.cinebase.mapper.MovieMapper;
import com.wiss.cinebase.repository.AppUserRepository;
import com.wiss.cinebase.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

// Diese Klasse ist ein Service
@Service
// ! Methoden laufen über Transaktionen ab
@Transactional

public class MovieService {
    private final MovieRepository movieRepository;
    private final AppUserRepository appUserRepository;

    // Konstruktor-Injektion (best practice in diesem Fall)
    public MovieService(MovieRepository movieRepository, AppUserRepository appUserRepository) {
        this.movieRepository = movieRepository;
        this.appUserRepository = appUserRepository;
    }

    // Neue Filmerstellung und Verknüpfung mit eingeloggtem Admin


    public MovieDTO createMovie(MovieDTO dto) {
        // Eingeloggten Admin bestimmt via des Security Context
        // ! JwtAuthenticationFilter hat Token geprüft und User in den "SecurityContext" gelegt
        // ! Umgehung des Controllers. Service ruft über Security Context den Namen des Admin ab
        // ! Verhinderung, dass Filme unter falschem Namen angelegt werden
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName;

        AppUser currentUser = appUserRepository.findByUsername(username);
                .orElseThrow(() -> new UsernameNotFoundException("User nicht gefunden " + username));

        // DTO zur Entity mappen
        Movie movie = MovieMapper.toEntity(movieDTO);

        // Beziehung setzen
        movie.setCreatedBy(currentUSer);

        // Abspeichern
        Movie saveMovie = movieRepository.save(movie);

        // Rückgabe ans DTO
        return MovieMapper.toDTO(saveMovie);
    }

    // Rückgabe aller Filme
    // ! Optimierung für Lesezugriff
    @Transactional(readOnly = true)

    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return MovieMapper.toDTOList(movies);
    }

    // Rückgabe eines einzelnen Films
    @Transactional(readOnly = true)

    public MovieDTO getMovieById() {
        Movie movie = movieRepository.findById(id);
                .orElseThrow(() -> new MovieNotFoundException(id));
        return MovieMapper.toDTO(movie);
    }

    // Aktualisierung eines Films
    public MovieDTO updateMovie(Long id; MovieDTO movieDTO) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(( -> new MovieNotFoundException(id));
    }

    // Felderaktualisierung
    existingMovie.setTitle(movieDTO.getTitle());
    existingMovie.setDescription(movieDTO.getDescription());
    existingMovie.setGenre(movieDTO.getGenre());
    existingMovie.setReleaseYear(movieDTO.getReleaseYear());
    existingMovie.setDirector(movieDTO.getDirector());
    existingMovie.setRating(movieDTO.getRating());

    // Abspeichern (trotzdem erkennt JPA Änderung automatisch via @Transactional) - better safe than sorry
    Movoe updatedMovie = movieRepository.save(existingMovie);
    return MovieMapper.toDTO(updatedMovie);
}

    // ! Löschung eines Films
    public void deleteMovie(Long id) {
    if (!movieRepository.existsById(id)) {
        throw new MovieNotFoundException(id);
    }
    movieRepository.deleteById(id);
    }
}
