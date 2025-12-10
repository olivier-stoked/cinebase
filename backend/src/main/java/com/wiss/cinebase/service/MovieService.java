package com.wiss.cinebase.service;

// "Chefkoch", der die Zutaten (Daten aus dem Repository) nimmt,
// verarbeitet (Validierung, User-Zuweisung) und das fertige Gericht (DTO) serviert.
// Businesslogik der App

// ! RAUSARBEITEN !!!!!!!!!!!!!!!!!
// ! Multi-User Aspekt: bei Erstellung des Films, welcher Admin ist gerade eingeloggt - Verknüpfung des Films mit diesem Admin.

import com.wiss.cinebase.dto.MovieDTO;
import com.wiss.cinebase.entity.AppUser;
import com.wiss.cinebase.entity.Movie;
import com.wiss.cinebase.exception.MovieNotFoundException;
import com.wiss.cinebase.mapper.MovieMapper;
import com.wiss.cinebase.repository.AppUserRepository;
import com.wiss.cinebase.repository.MovieRepository;
import com.wiss.cinebase.repository.ReviewRepository; // ! NEU: Import für Durchschnittsberechnung
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Spring relevanter Import!

import java.util.List;
import java.util.stream.Collectors; // ! NEU: Für die Listen-Verarbeitung

/**
 * Service für die Verwaltung von Filmen.
 * Diese Klasse enthält die Geschäftslogik für das Erstellen, Lesen, Aktualisieren und Löschen (CRUD)
 * von Filmen. Sie stellt sicher, dass Filme korrekt mit den erstellenden Benutzern verknüpft werden
 * und handhabt Transaktionen.
 */
@Service
// ! Jede Methode läuft in einer Transaktion (ACID-Prinzip: Alles oder nichts)
@Transactional

public class MovieService {
    private final MovieRepository movieRepository;
    private final AppUserRepository appUserRepository;
    private final ReviewRepository reviewRepository; // ! NEU: Injizieren des Review Repositories

    // Konstruktor-Injektion (best practice in diesem Fall) statt @Autowired - bessere Testbarkeit
    public MovieService(MovieRepository movieRepository,
                        AppUserRepository appUserRepository,
                        ReviewRepository reviewRepository) { // ! NEU: Parameter hinzugefügt
        this.movieRepository = movieRepository;
        this.appUserRepository = appUserRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Erstellt einen neuen Film und speichert ihn in der Datenbank.
     * Der Film wird automatisch mit dem aktuell eingeloggten Benutzer (Admin) verknüpft,
     * der im SecurityContext hinterlegt ist.
     * @param movieDTO Die Daten des neuen Films (vom Frontend)
     * @return Der gespeicherte Film als DTO (inklusive generierter ID)
     * @throws UsernameNotFoundException wenn der eingeloggte User nicht in der DB gefunden wird (Sicherheits-Edge-Case)
     */
    public MovieDTO createMovie(MovieDTO movieDTO) {

        // Neue Filmerstellung und Verknüpfung mit eingeloggtem Admin

        // Eingeloggten Admin bestimmt via des Security Context
        // ! hat Token geprüft und User in den "SecurityContext" gelegt
        // ! Umgehung des Controllers. Service ruft über Security Context den Namen des Admin ab
        // ! Verhinderung, dass Filme unter falschem Namen angelegt werden


        // ! SECURITY: Wir holen den User aus dem SecurityContext, nicht vom Frontend!
        // Das verhindert, dass man Filme unter falschem Namen anlegen kann.

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser currentUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User nicht gefunden " + username));


        // DTO zur Entity mappen
        // Mapping: DTO in Datenbank-Entity umwandeln
        Movie movie = MovieMapper.toEntity(movieDTO);

        // Beziehung setzen
        // Logik: Die Beziehung zum Admin setzen
        movie.setCreatedBy(currentUser);

        // Abspeichern
        Movie saveMovie = movieRepository.save(movie);

        // Rückgabe ans DTO
        return MovieMapper.toDTO(saveMovie);
    }

    // Rückgabe aller Filme
    // ! Optimierung für Lesezugriff
    /** Ruft eine Liste aller verfügbaren Filme ab. */

    // ! Der Service muss jetzt beim Laden aller Filme (getAllMovies) kurz beim ReviewRepository nachfragen:
    // ! "Wie ist der Durchschnitt für diesen Film?".

    @Transactional(readOnly = true) // ! Performance: readOnly optimiert die Datenbankabfrage
    public List<MovieDTO> getAllMovies() {
        // ! ANPASSUNG: Wir nutzen Streams, um für jeden Film den Durchschnitt zu berechnen
        return movieRepository.findAll().stream()
                .map(movie -> {
                    // 1. Entity zu DTO umwandeln
                    MovieDTO dto = MovieMapper.toDTO(movie);

                    // 2. ! NEU: Live-Durchschnitt aus der Review-Tabelle holen (Custom Query Block 05B)
                    Double avg = reviewRepository.getAverageRatingForMovie(movie.getId());
                    dto.setAverageRating(avg != null ? avg : 0.0);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Sucht einen spezifischen Film anhand seiner ID.
     * @param id Die ID des gesuchten Films
     * @return Das gefundene MovieDTO
     * @throws MovieNotFoundException wenn kein Film mit dieser ID existiert
     */

    @Transactional(readOnly = true)
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        MovieDTO dto = MovieMapper.toDTO(movie);

        // ! NEU: Auch beim Einzelabruf den Durchschnitt setzen
        Double avg = reviewRepository.getAverageRatingForMovie(movie.getId());
        dto.setAverageRating(avg != null ? avg : 0.0);

        return dto;
    }

    // Aktualisierung eines Films
    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        // Felderaktualisierung
        existingMovie.setTitle(movieDTO.getTitle());
        existingMovie.setDescription(movieDTO.getDescription());
        existingMovie.setGenre(movieDTO.getGenre());
        existingMovie.setReleaseYear(movieDTO.getReleaseYear());
        existingMovie.setDirector(movieDTO.getDirector());
        existingMovie.setRating(movieDTO.getRating());

        // ! JPA Magic: Durch @Transactional würde die Änderung auch ohne save() erkannt ("Dirty Checking"). KI !!!!
        // Wir rufen save() trotzdem explizit auf, um den Code verständlicher zu machen.
        // Abspeichern (trotzdem erkennt JPA Änderung automatisch via @Transactional) - better safe than sorry
        Movie updatedMovie = movieRepository.save(existingMovie);
        return MovieMapper.toDTO(updatedMovie);
    }

    /**
     * Löscht einen Film unwiderruflich aus der Datenbank.
     * @param id Die ID des zu löschenden Films
     * @throws MovieNotFoundException wenn der Film nicht existiert
     */

    public void deleteMovie(Long id) {
        // ! Wir prüfen vorher, ob er existiert, um eine saubere Exception zu werfen
        if (!movieRepository.existsById(id)) {
            throw new MovieNotFoundException(id);
        }

        movieRepository.deleteById(id);

    }

}
