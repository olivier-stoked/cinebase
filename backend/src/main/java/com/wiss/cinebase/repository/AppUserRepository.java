package com.wiss.cinebase.repository;

import com.wiss.cinebase.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// ! Ist mir immer noch nicht 100% schlüssig.
// * Interface: sobald die App launcht liest Spring die Methodennamen.
// * Es muss in AppUser gefunden werden, wo die diversen Felder übereinstimmen.
// * Spring baut den SQL-Code zur Laufzeit automatisch zusammen.


// erstellt automatisch eine Datenbankverbindung.
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // Spring generiert automatisch das SQL - SELECT * FROM app_users WHERE username = ?
    // Optional: prüft, ob der User gefunden wurde.
    Optional<AppUser> findByUsername(String username);

    // Erstellt: SELECT * FROM app_user WHERE email = ?
    Optional<AppUser> findByEmail(String email);

    // Erstellt: SELECT COUNT (*) > 0 FROM app_users WHERE username = ?
    // True (return) falls User bereits existiert.
    boolean existsByUsername(String username);

    // Erstellt: SELECT COUNT (*) > 0 FROM app_users WHERE email = ?
    boolean existsByEmail(String email);
}




