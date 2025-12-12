package com.wiss.cinebase.security;

// Importiert den JWT-Service zur Token-Validierung.
import com.wiss.cinebase.service.JwtService;

// Importiert Servlet-Klassen für die Request-Filterung.
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Importiert Spring Security Klassen für die Authentifizierung im Context.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter - Das "Ausweis-Lesegerät" der Applikation.
 * Quelle: Block 02B - Spring Security & Filter
 * Funktion:
 * 1. Prüft bei jedem Request, ob ein Authorization-Header mit Bearer-Token existiert.
 * 2. Validiert den Token über den JwtService.
 * 3. Lädt den Benutzer und setzt ihn in den SecurityContext, damit Controller (@PreAuthorize) darauf zugreifen können.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Constructor Injection für die benötigten Services.
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Hauptlogik des Filters. Wird einmal pro Request ausgeführt.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Authorization Header aus dem Request holen
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Prüfen: Ist der Header vorhanden und fängt er mit "Bearer " an?
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // ! Wenn kein Token da ist, Request einfach weiterleiten.
            // Der SecurityConfig entscheidet dann, ob der Zugriff erlaubt ist (bei öffentlichen Endpoints) oder 403 sendet.
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Token extrahieren (alles nach "Bearer " abschneiden)
        jwt = authHeader.substring(7);

        // 4. Username aus dem Token lesen (nutzt JwtService)
        username = jwtService.extractUsername(jwt);

        // 5. Falls Username existiert und der User im aktuellen Request noch NICHT authentifiziert ist:
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // User-Details aus der Datenbank laden
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 6. Token validieren (Prüfung auf Signatur und Ablaufdatum)
            if (jwtService.validateToken(jwt, username)) {

                // "Ausweis" erstellen: Authentication-Objekt für Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credentials (Passwort) werden nicht mehr benötigt/gespeichert
                        userDetails.getAuthorities() // Rollen/Rechte übergeben
                );

                // Details zum Request (z.B. IP-Adresse) hinzufügen
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 7. ! Spring Security informieren: "Dieser User ist jetzt sicher eingeloggt!"
                // Ab hier gilt der User für den Rest des Requests als authentifiziert.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Request an den nächsten Filter (oder schließlich den Controller) weitergeben
        filterChain.doFilter(request, response);
    }
}