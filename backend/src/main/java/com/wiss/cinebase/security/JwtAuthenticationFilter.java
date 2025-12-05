package com.wiss.cinebase.security;

import com.wiss.cinebase.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Wir injizieren den JwtService (für Token-Logik) und UserDetailsService (um User aus DB zu laden)
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

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

        // 2. Prüfen: Ist der Header da und fängt er mit "Bearer " an?
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Wenn nein: Weiter machen (ohne Login)
            return;
        }

        // 3. Token extrahieren (alles nach "Bearer ")
        jwt = authHeader.substring(7);

        // 4. Username aus dem Token lesen
        username = jwtService.extractUsername(jwt);

        // 5. Falls Username existiert und noch niemand eingeloggt ist:
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // User aus der Datenbank laden
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 6. Token validieren
            if (jwtService.validateToken(jwt, username)) {

                // "Ausweis" erstellen
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 7. Spring Security sagen: "Dieser User ist jetzt sicher eingeloggt!"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Weiter zum nächsten Filter (oder Controller)
        filterChain.doFilter(request, response);
    }
}
