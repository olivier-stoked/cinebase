package com.wiss.cinebase.config;

import com.wiss.cinebase.entity.AppUser;
import com.wiss.cinebase.entity.Movie;
import com.wiss.cinebase.entity.Role;
import com.wiss.cinebase.repository.AppUserRepository;
import com.wiss.cinebase.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * Initialisiert die Datenbank mit Testdaten beim Start.
 * Erstellt einen Admin-User und lädt die vollständige Filmliste.
 *
 * Quelle: Filmliste.txt
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(AppUserRepository userRepository,
                                   MovieRepository movieRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {

            // 1. Admin User erstellen (falls nicht vorhanden)
            AppUser admin = userRepository.findByUsername("admin")
                    .orElseGet(() -> {
                        System.out.println("⚙️ Erstelle Admin-User...");
                        AppUser newAdmin = new AppUser(
                                "admin",
                                "admin@cinebase.ch",
                                passwordEncoder.encode("admin123"),
                                Role.ADMIN
                        );
                        return userRepository.save(newAdmin);
                    });

            // 2. Filme laden, falls DB leer ist
            if (movieRepository.count() == 0) {
                System.out.println("⚙️ Lade Filme aus der Filmliste...");

                List<Movie> movies = List.of(
                        new Movie("2001: A Space Odyssey", "A visionary science fiction classic about humanity and artificial intelligence.", "Science Fiction", 1968, "Stanley Kubrick", 9.0, admin),
                        new Movie("Silent Running", "A botanist fights to save the last forests in space.", "Science Fiction", 1972, "Douglas Trumbull", 7.2, admin),
                        new Movie("Solaris", "A psychologist investigates strange occurrences on a space station.", "Science Fiction", 1972, "Andrei Tarkovsky", 8.0, admin),
                        new Movie("Star Wars: A New Hope", "The beginning of an epic space saga.", "Science Fiction", 1977, "George Lucas", 9.0, admin),
                        new Movie("Close Encounters of the Third Kind", "A man becomes obsessed with an encounter with UFOs.", "Science Fiction", 1977, "Steven Spielberg", 8.1, admin),
                        new Movie("Alien", "A spaceship crew is hunted by a deadly alien creature.", "Science Fiction", 1979, "Ridley Scott", 8.5, admin),
                        new Movie("Star Trek: The Motion Picture", "The Enterprise investigates a mysterious object threatening Earth.", "Science Fiction", 1979, "Robert Wise", 6.4, admin),
                        new Movie("Blade Runner", "A blade runner hunts replicants in a dystopian future.", "Science Fiction", 1982, "Ridley Scott", 8.1, admin),
                        new Movie("The Thing", "A research station in Antarctica is infiltrated by an alien lifeform.", "Science Fiction", 1982, "John Carpenter", 8.2, admin),
                        new Movie("E.T. the Extra-Terrestrial", "A boy befriends a stranded alien.", "Science Fiction", 1982, "Steven Spielberg", 7.9, admin),
                        new Movie("The Terminator", "A cyborg travels back in time to kill Sarah Connor.", "Science Fiction", 1984, "James Cameron", 8.0, admin),
                        new Movie("Dune", "A young man fights for the fate of a desert planet.", "Science Fiction", 1984, "David Lynch", 6.6, admin),
                        new Movie("Back to the Future", "A teenager accidentally travels back in time.", "Science Fiction", 1985, "Robert Zemeckis", 8.5, admin),
                        new Movie("Aliens", "Ellen Ripley returns to fight Xenomorphs.", "Science Fiction", 1986, "James Cameron", 8.4, admin),
                        new Movie("RoboCop", "A police officer becomes a cybernetic law enforcer.", "Science Fiction", 1987, "Paul Verhoeven", 7.6, admin),
                        new Movie("The Abyss", "An underwater crew encounters an alien lifeform.", "Science Fiction", 1989, "James Cameron", 7.6, admin),
                        new Movie("Total Recall", "A man questions his own identity.", "Science Fiction", 1990, "Paul Verhoeven", 7.5, admin),
                        new Movie("Terminator 2: Judgment Day", "A reprogrammed terminator must protect John Connor.", "Science Fiction", 1991, "James Cameron", 8.6, admin),
                        new Movie("Stargate", "A portal to another world is discovered.", "Science Fiction", 1994, "Roland Emmerich", 7.1, admin),
                        new Movie("12 Monkeys", "A man travels through time to stop a catastrophe.", "Science Fiction", 1995, "Terry Gilliam", 8.0, admin),
                        new Movie("Gattaca", "A man fights against a genetically determined society.", "Science Fiction", 1997, "Andrew Niccol", 7.8, admin),
                        new Movie("Contact", "A scientist receives a message from space.", "Science Fiction", 1997, "Robert Zemeckis", 7.4, admin),
                        new Movie("The Fifth Element", "A taxi driver must save the world.", "Science Fiction", 1997, "Luc Besson", 7.6, admin),
                        new Movie("Dark City", "A man wakes up in a city with no memory.", "Science Fiction", 1998, "Alex Proyas", 7.6, admin),
                        new Movie("The Matrix", "A hacker discovers the true nature of his reality.", "Science Fiction", 1999, "The Wachowskis", 8.7, admin),
                        new Movie("Galaxy Quest", "A canceled sci-fi show becomes real for its actors.", "Science Fiction", 1999, "Dean Parisot", 7.3, admin),
                        new Movie("Pitch Black", "A dangerous prisoner helps survivors on an alien planet.", "Science Fiction", 2000, "David Twohy", 7.1, admin),
                        new Movie("Interstellar", "Astronauts travel through a wormhole to save humanity.", "Science Fiction", 2014, "Christopher Nolan", 8.6, admin)
                );

                movieRepository.saveAll(movies);
                System.out.println("✅ " + movies.size() + " Filme erfolgreich initialisiert!");
            } else {
                System.out.println("ℹ️ Filme bereits vorhanden. Überspringe Initialisierung.");
            }
        };
    }
}