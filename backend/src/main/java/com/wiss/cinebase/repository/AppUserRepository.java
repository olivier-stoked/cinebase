package com.wiss.cinebase.repository;

// Quelle: Block 03A - Entities
import com.wiss.cinebase.entity.AppUser;
// Quelle: Block 04A - Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository für den Zugriff auf Benutzerdaten.
 * Quelle: Block 04A - Repositories
 * Funktionsweise:
 * Durch die Erweiterung von JpaRepository erbt dieses Interface Standard-CRUD-Methoden.
 * Spring Data JPA analysiert zur Laufzeit die Methodennamen und generiert automatisch
 * die passenden SQL-Queries (Query Derivation).
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // Generiert: SELECT * FROM app_users WHERE username = ?
    // Optional verhindert NullPointerExceptions, falls der User nicht existiert.
    Optional<AppUser> findByUsername(String username);

    // Generiert: SELECT * FROM app_users WHERE email = ?
    Optional<AppUser> findByEmail(String email);

    // ! Performance-Optimierung: Prüft nur auf Existenz, lädt nicht die ganze Entity.
    // Generiert: SELECT COUNT(*) > 0 FROM app_users WHERE username = ?
    boolean existsByUsername(String username);

    // Generiert: SELECT COUNT(*) > 0 FROM app_users WHERE email = ?
    boolean existsByEmail(String email);
}