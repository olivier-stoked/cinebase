package com.wiss.cinebase.config;

import org.springframework.context.annotation.Configuration;
// Quelle: Block 04A - Spring MVC Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web-Konfiguration.
 *
 * ! WICHTIG: Die CORS-Konfiguration wurde in die SecurityConfig verschoben.
 * Grund: Der Spring Security Filter greift VOR dem WebMVC Filter.
 * Eine Konfiguration an dieser Stelle wird ignoriert oder führt zu Konflikten.
 *
 * (Klasse wird als Platzhalter für zukünftige MVC-Konfigurationen beibehalten).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS wird zentral in SecurityConfig behandelt.
    // Keine addCorsMappings nötig.

}
