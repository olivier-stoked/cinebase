package com.wiss.cinebase.controller;

import com.wiss.cinebase.dto.LoginRequestDTO;
import com.wiss.cinebase.dto.LoginResponseDTO;
import com.wiss.cinebase.dto.RegisterRequestDTO;
import com.wiss.cinebase.dto.RegisterResponseDTO;
import com.wiss.cinebase.entity.AppUser;
import com.wiss.cinebase.entity.Role;
import com.wiss.cinebase.service.AppUserService;
import com.wiss.cinebase.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * REST Controller für Authentication (Login & Register).
 *
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

    // Constructor Injection (Best Practice aus Block 01B & 03B)
    public AuthController(AppUserService appUserService, JwtService jwtService) {
        this.appUserService = appUserService;
        this.jwtService = jwtService;
    }

    // ------------------------------------------------------------------------
    // BLOCK 01B: Registration Endpoint
    // Quelle: 01B -📦DTOs und AuthController (25 Min).docx
    // ------------------------------------------------------------------------

    @PostMapping("/register")
    @Operation(summary = "Neuen Benutzer registrieren")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            // 1. Service aufrufen (Logik aus Block 01B)
            // Wir nutzen hier Role.USER statt PLAYER, wie für Cinebase definiert
            AppUser newUser = appUserService.registerUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    Role.USER
            );

            // 2. Response DTO erstellen (Keine sensiblen Daten zurückgeben!)
            RegisterResponseDTO response = new RegisterResponseDTO(
                    newUser.getId(),
                    newUser.getUsername(),
                    newUser.getEmail(),
                    newUser.getRole().name()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            // Validation Error (z.B. Username existiert schon)
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registrierung fehlgeschlagen"));
        }
    }

    // ------------------------------------------------------------------------
    // BLOCK 02A: Login Endpoint
    // Quelle: 02A - 🔐Login DTOs & Endpoint (25 min).docx
    // ------------------------------------------------------------------------

    @PostMapping("/login")
    @Operation(summary = "Benutzer einloggen und Token erhalten")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            // 1. User authentifizieren (Service prüft Passwort-Hash)
            Optional<AppUser> authenticatedUser = appUserService.authenticateUser(
                    request.getUsernameOrEmail(),
                    request.getPassword()
            );

            if (authenticatedUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Ungültige Anmeldedaten"));
            }

            AppUser user = authenticatedUser.get();

            // 2. JWT Token generieren (JwtService aus Block 02A)
            String token = jwtService.generateToken(user.getUsername(), user.getRole().name());

            // 3. Response DTO mit Token zurückgeben
            LoginResponseDTO response = new LoginResponseDTO(
                    token,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name(),
                    86400000L // 24 Stunden
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login fehlgeschlagen: " + e.getMessage()));
        }
    }
}
