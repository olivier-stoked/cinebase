package com.wiss.cinebase.service;

// Quelle: Block 03A - Entities
import com.wiss.cinebase.entity.AppUser;
import com.wiss.cinebase.entity.Role;
// Quelle: Block 04A - Repositories
import com.wiss.cinebase.repository.AppUserRepository;
// Quelle: Block 02B - Security (BCrypt)
import org.springframework.security.crypto.password.PasswordEncoder;
// Quelle: Spring Core
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Layer für User-Management.
 * Verantwortlichkeiten:
 * - Trennung von Business Logic und Web Layer (Controller).
 * - Registrierung neuer User (mit Passwort-Hashing).
 * - Validierung von Username/Email.
 * - Authentication (Login-Prüfung).
 * Quelle: Block 01B - Service Layer & Password Hashing
 */
@Service
@Transactional // ! Stellt sicher, dass Datenbankoperationen atomar ausgeführt werden (ACID).
public class AppUserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection (Best Practice für Testbarkeit und Immutability).
    public AppUserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registriert einen neuen Benutzer.
     * @param username Gewünschter Benutzername
     * @param email Gewünschte Email
     * @param rawPassword Passwort im Klartext (wird gehashed)
     * @param role Rolle des Users (ADMIN oder USER)
     * @return Der gespeicherte User
     * @throws IllegalArgumentException wenn Username oder Email schon existieren
     */
    public AppUser registerUser(String username, String email, String rawPassword, Role role) {
        // 1. Validierung: Existiert der User schon?
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' ist bereits vergeben");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email '" + email + "' ist bereits vergeben");
        }

        // 2. Passwort hashen (NIEMALS Passwörter im Klartext speichern!)
        // Quelle: Block 01B - BCrypt Configuration
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // 3. User Entity erstellen
        AppUser newUser = new AppUser(username, email, hashedPassword, role);

        // 4. Speichern und zurückgeben
        return userRepository.save(newUser);
    }

    /**
     * Authentifiziert einen User (für den Login).
     * Prüft, ob der User existiert und das Passwort mit dem Hash übereinstimmt.
     * @param usernameOrEmail Username oder Email
     * @param rawPassword Das eingegebene Passwort
     * @return Optional mit User wenn erfolgreich, sonst leer
     */
    public Optional<AppUser> authenticateUser(String usernameOrEmail, String rawPassword) {
        // User suchen (kann Username ODER Email sein)
        Optional<AppUser> userOpt = userRepository.findByUsername(usernameOrEmail);

        if (userOpt.isEmpty()) {
            // Falls nicht per Username gefunden, per Email versuchen
            userOpt = userRepository.findByEmail(usernameOrEmail);
        }

        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();

            // Passwort prüfen (BCrypt vergleicht Raw-Passwort mit Hash)
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return userOpt;  // Login erfolgreich
            }
        }

        return Optional.empty();  // Login fehlgeschlagen
    }

    /**
     * Findet User anhand des Usernames (Hilfsmethode).
     */
    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}