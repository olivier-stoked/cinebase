package com.wiss.cinebase.config;

// Quelle: Block 02B - Custom Security Filter
import com.wiss.cinebase.security.JwtAuthenticationFilter;

// Quelle: Spring Core Container
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Quelle: Block 02B - Spring Security Authentication & Configuration
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Quelle: Block 02B & Block 04A - CORS Configuration
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Zentrale Sicherheitskonfiguration der Anwendung.
 *
 * Quelle: Block 02B - Security Basics & JWT
 *
 * Anpassungen Single-User zu Multi-User:
 * - Implementierung von Stateless JWT Authentication statt Basic Auth.
 * - Einführung rollenbasierter Zugriffskontrolle (RBAC) für Admin/User.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // ! Wichtig: Ermöglicht Nutzung von @PreAuthorize("hasRole('ADMIN')") in Controllern
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Definition der Security Filter Chain.
     * Legt fest, welche Filter aktiv sind und wie Anfragen autorisiert werden.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF Deaktivierung
                // Begründung: REST-APIs mit JWT sind stateless; CSRF-Schutz ist primär für Session-Cookies relevant.
                .csrf(csrf -> csrf.disable())

                // 2. CORS Aktivierung (Konfiguration siehe unten: corsConfigurationSource)
                // ! Verbindet Frontend (Port 5173) mit Backend (Port 8080)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Zugriffskontrolle (Access Control)
                .authorizeHttpRequests(auth -> auth
                        // Öffentliche Endpunkte (Login, Registrierung, Swagger Dokumentation)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // ! ANPASSUNG: Lesezugriff auf Filme/Reviews ist öffentlich (auch für Gäste)
                        // Schreibzugriffe werden spezifisch im Controller per @PreAuthorize geregelt.
                        .requestMatchers("/api/movies/**").permitAll()
                        .requestMatchers("/api/reviews/**").permitAll()

                        // Alle übrigen Anfragen erfordern Authentifizierung
                        .anyRequest().authenticated()
                )

                // 4. Session Management: STATELESS
                // ! Wichtig für JWT: Server speichert keinen Status (keine Session-ID im RAM).
                // Der Token muss bei jedem Request mitgesendet werden.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5. Authentication Provider (Verbindung DB-User mit Security-Logik)
                .authenticationProvider(authenticationProvider())

                // 6. Filter-Reihenfolge: JwtAuthenticationFilter wird VOR dem Standard-Login-Filter ausgeführt
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS Konfiguration (Cross-Origin Resource Sharing).
     * Erlaubt Anfragen vom React-Frontend (localhost:5173).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Frontend URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Erlaubte HTTP-Methoden
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Erlaubt Credentials (Cookies/Auth-Header)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Anwendung auf alle Pfade
        return source;
    }

    /**
     * Konfiguration der Passwort-Verschlüsselung.
     * BCrypt fügt automatisch einen Salt hinzu (Schutz vor Rainbow Tables).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Stärke 12 (Standard ist 10)
    }

    /**
     * Verknüpft UserDetailsService (Datenbankzugriff) mit dem PasswordEncoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
