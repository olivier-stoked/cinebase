package com.wiss.cinebase.controller;

// Importiert DTOs für Login und Registrierung (Datentransfer ohne Entities).
import com.wiss.cinebase.dto.LoginRequestDTO;
import com.wiss.cinebase.dto.LoginResponseDTO;
import com.wiss.cinebase.dto.RegisterRequestDTO;
import com.wiss.cinebase.dto.RegisterResponseDTO;

// Importiert Entities für Rollenzuweisung.
import com.wiss.cinebase.entity.AppUser;
import com.wiss.cinebase.entity.Role;

// Importiert Services für die Geschäftslogik.
import com.wiss.cinebase.service.AppUserService;
import com.wiss.cinebase.service.JwtService;

// Importiert Swagger-Annotationen für die API-Dokumentation.
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

// Importiert Validierungs-Annotationen (z.B. @Valid).
import jakarta.validation.Valid;

// Importiert Spring Web Annotationen für REST-Controller.
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * REST Controller für die Authentifizierung (Login & Registrierung).
 * Stellt öffentliche Endpunkte bereit, um JWT-Tokens zu erhalten.
 * Quellen:
 * - Struktur & Register: Block 01B - DTOs und AuthController
 * - Login & JWT: Block 02A - Login DTOs & Endpoint
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentifizierung (Login & Register)")
public class AuthController {

    private final AppUserService appUserService;
    private final JwtService jwtService;

    // Constructor Injection (Best Practice aus Block 01B & 03B für Testbarkeit).
    public AuthController(AppUserService appUserService, JwtService jwtService) {
        this.appUserService = appUserService;
        this.jwtService = jwtService;
    }

    // ------------------------------------------------------------------------
    // BLOCK 01B: Registration Endpoint
    // ------------------------------------------------------------------------

    /**
     * Registriert einen neuen Benutzer im System.
     * @param request Das DTO mit Username, Email und Passwort.
     * @return Eine Bestätigung mit den User-Daten (ohne Passwort).
     */
    @PostMapping("/register")
    @Operation(summary = "Neuen Benutzer registrieren")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            // 1. Service aufrufen (Logik aus Block 01B).
            // ! Für CINEBASE werden standardmäßig USER (Journalisten) erstellt.
            // Admins werden üblicherweise über den DataInitializer oder direkt in der DB angelegt.
            AppUser newUser = appUserService.registerUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    Role.USER
            );

            // 2. Response DTO erstellen (Vermeidung der Rückgabe sensibler Entity-Daten).
            RegisterResponseDTO response = new RegisterResponseDTO(
                    newUser.getId(),
                    newUser.getUsername(),
                    newUser.getEmail(),
                    newUser.getRole().name()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            // Validation Error (z.B. Username existiert bereits).
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registrierung fehlgeschlagen"));
        }
    }

    // ------------------------------------------------------------------------
    // BLOCK 02A: Login Endpoint
    // ------------------------------------------------------------------------

    /**
     * Authentifiziert einen Benutzer und erstellt einen JWT Token.
     * @param request Das DTO mit Credentials.
     * @return Ein JSON-Objekt, das den Token und User-Details enthält.
     */
    @PostMapping("/login")
    @Operation(summary = "Benutzer einloggen und Token erhalten")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            // 1. User authentifizieren (Service prüft Passwort-Hash mit BCrypt).
            Optional<AppUser> authenticatedUser = appUserService.authenticateUser(
                    request.getUsernameOrEmail(),
                    request.getPassword()
            );

            if (authenticatedUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Ungültige Anmeldedaten"));
            }

            AppUser user = authenticatedUser.get();

            // 2. JWT Token generieren (JwtService aus Block 02A).
            // ! Der Token enthält die Rolle als Claim für spätere Autorisierungsprüfungen.
            String token = jwtService.generateToken(user.getUsername(), user.getRole().name());

            // 3. Response DTO mit Token zurückgeben.
            LoginResponseDTO response = new LoginResponseDTO(
                    token,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name(),
                    86400000L // Gültigkeit: 24 Stunden
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login fehlgeschlagen: " + e.getMessage()));
        }
    }
}