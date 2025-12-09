package com.wiss.cinebase.config;

import com.wiss.cinebase.entity.AppUser;
import com.wiss.cinebase.entity.Movie;
import com.wiss.cinebase.entity.Review;
import com.wiss.cinebase.entity.Role;
import com.wiss.cinebase.repository.AppUserRepository;
import com.wiss.cinebase.repository.MovieRepository;
import com.wiss.cinebase.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * Initialisiert die Datenbank mit Testdaten beim Start.
 * Erstellt Admins, User und Beispieldaten.
 *
 * Quelle: Filmliste.txt
 * Konzept: Data Initializer aus Block 07
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(AppUserRepository userRepository,
                                   MovieRepository movieRepository,
                                   ReviewRepository reviewRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {

            // --- 1. User erstellen (2 Admins, 3 Journalisten) ---

            // Admin 1 (Festival-Leitung)
            AppUser admin1 = createOrGetUser(userRepository, passwordEncoder, "admin", "admin@cinebase.ch", Role.ADMIN);
            // Admin 2 (Co-Leitung)
            AppUser admin2 = createOrGetUser(userRepository, passwordEncoder, "co_admin", "leitung@cinebase.ch", Role.ADMIN);

            // User 1 (Der Kritische)
            AppUser critic = createOrGetUser(userRepository, passwordEncoder, "kritiker_hans", "hans@presse.ch", Role.USER);
            // User 2 (Der Sci-Fi Fan)
            AppUser scifiFan = createOrGetUser(userRepository, passwordEncoder, "scifi_sarah", "sarah@nerd.ch", Role.USER);
            // User 3 (Der Mainstream Zuschauer)
            AppUser casual = createOrGetUser(userRepository, passwordEncoder, "max_mustermann", "max@web.ch", Role.USER);

            // ! MACHT DAS SINN ???
            System.out.println("Benutzerkonten wurden initialisiert (Passwort für alle: admin123)");


            // --- 2. Filme laden ---

            if (movieRepository.count() == 0) {
                System.out.println("Lade Filme aus der Filmliste...");

                List<Movie> movies = List.of(
                        new Movie("2001: A Space Odyssey", "A visionary science fiction classic about humanity and artificial intelligence.", "Science Fiction", 1968, "Stanley Kubrick", 9.0, admin1),
                        new Movie("Silent Running", "A botanist fights to save the last forests in space.", "Science Fiction", 1972, "Douglas Trumbull", 7.2, admin1),
                        new Movie("Solaris", "A psychologist investigates strange occurrences on a space station.", "Science Fiction", 1972, "Andrei Tarkovsky", 8.0, admin2), // Admin 2 hat diesen erstellt
                        new Movie("Star Wars: A New Hope", "The beginning of an epic space saga.", "Science Fiction", 1977, "George Lucas", 9.0, admin1),
                        new Movie("Alien", "A spaceship crew is hunted by a deadly alien creature.", "Science Fiction", 1979, "Ridley Scott", 8.5, admin1),
                        new Movie("Blade Runner", "A blade runner hunts replicants in a dystopian future.", "Science Fiction", 1982, "Ridley Scott", 8.1, admin2),
                        new Movie("The Matrix", "A hacker discovers the true nature of his reality.", "Science Fiction", 1999, "The Wachowskis", 8.7, admin1),
                        new Movie("Inception", "A thief who steals corporate secrets through the use of dream-sharing technology.", "Science Fiction", 2010, "Christopher Nolan", 8.8, admin2),
                        new Movie("Interstellar", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.", "Science Fiction", 2014, "Christopher Nolan", 8.6, admin1),
                        new Movie("The Godfather", "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.", "Crime", 1972, "Francis Ford Coppola", 9.2, admin1),
                        new Movie("Pulp Fiction", "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine.", "Crime", 1994, "Quentin Tarantino", 8.9, admin2)
                        // ... (Hier koennten die restlichen Filme stehen, Liste gekuerzt fuer Uebersicht)
                );

                movieRepository.saveAll(movies);
                System.out.println(movies.size() + " Filme erfolgreich initialisiert!");


                // --- 3. Beispiel-Bewertungen erstellen (Damit wir Durchschnitte sehen) ---

                // Wir holen uns die gespeicherten Filme aus der DB, um sicher die IDs zu haben
                Movie matrix = movieRepository.findByTitleContainingIgnoreCase("The Matrix").get(0);
                Movie alien = movieRepository.findByTitleContainingIgnoreCase("Alien").get(0);
                Movie solaris = movieRepository.findByTitleContainingIgnoreCase("Solaris").get(0);

                List<Review> reviews = List.of(
                        // Matrix Bewertungen (Durchschnitt sollte hoch sein)
                        new Review(scifiFan, matrix, 10, "Absolutes Meisterwerk!"),
                        new Review(critic, matrix, 9, "Visuell beeindruckend, Story etwas verwirrend."),
                        new Review(casual, matrix, 10, "Bester Film aller Zeiten!"),

                        // Alien Bewertungen (Durchschnitt mittel)
                        new Review(scifiFan, alien, 9, "Klassiker."),
                        new Review(casual, alien, 7, "Zu gruselig fuer mich."),

                        // Solaris Bewertungen (Durchschnitt niedrig/gemischt)
                        new Review(casual, solaris, 3, "Zu langweilig, bin eingeschlafen."),
                        new Review(critic, solaris, 9, "Intellektuell fordernd, grandios.")
                );

                reviewRepository.saveAll(reviews);
                System.out.println("Beispiel-Reviews erstellt!");

            } else {
                System.out.println("Daten bereits vorhanden. Ueberspringe Initialisierung.");
            }
        };
    }

    // Hilfsmethode, um User zu erstellen, falls sie nicht existieren
    private AppUser createOrGetUser(AppUserRepository repo, PasswordEncoder encoder, String username, String email, Role role) {
        return repo.findByUsername(username)
                .orElseGet(() -> {
                    AppUser user = new AppUser(username, email, encoder.encode("admin123"), role);
                    return repo.save(user);
                });
    }
}
