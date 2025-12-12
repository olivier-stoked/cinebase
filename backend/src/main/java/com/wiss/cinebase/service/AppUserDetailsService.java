package com.wiss.cinebase.service;

// Importiert das Repository für den Datenbankzugriff auf Benutzerdaten.
import com.wiss.cinebase.repository.AppUserRepository;

// Importiert das zentrale Interface für Benutzerinformationen in Spring Security.
// Quelle: Block 01B - AppUser als Spring Security User
import org.springframework.security.core.userdetails.UserDetails;

// Importiert das Interface, das implementiert werden muss, um User für die Authentifizierung zu laden.
import org.springframework.security.core.userdetails.UserDetailsService;

// Importiert die Exception, die geworfen wird, wenn ein Login-Versuch mit unbekanntem Benutzer erfolgt.
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Importiert die Annotation, um diese Klasse als Spring-Service-Bean zu registrieren.
import org.springframework.stereotype.Service;

/**
 * Lädt Benutzerdaten für Spring Security aus der Datenbank.
 * Diese Klasse ist das Bindeglied zwischen der Datenbank (Repository) und dem Sicherheitsframework.
 * Sie wird vom JwtAuthenticationFilter und AuthenticationManager benötigt, um Benutzer zu validieren.
 * Quelle: Block 02B - Spring Security & Filter
 */
@Service // ! WICHTIG: Das erzeugt die Bean, die für die Dependency Injection im SecurityConfig benötigt wird.
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Lädt einen User anhand des Benutzernamens.
     * Diese Methode wird von Spring Security während des Login-Prozesses aufgerufen.
     * @param username Der Benutzername aus dem Login-Formular oder Token.
     * @return Ein UserDetails-Objekt (unser AppUser), das Passwort und Rollen enthält.
     * @throws UsernameNotFoundException wenn der Benutzer nicht in der Datenbank existiert.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Sucht den User in der DB. Da AppUser das UserDetails-Interface implementiert (siehe Entity),
        // kann das Ergebnis direkt zurückgegeben werden.
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User nicht gefunden: " + username));
    }
}