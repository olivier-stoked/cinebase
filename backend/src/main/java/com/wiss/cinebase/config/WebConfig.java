package com.wiss.cinebase.config;

import org.springframework.context.annotation.Configuration;
// Quelle: Block 04A - Spring MVC Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web-Konfiguration für Spring MVC.
 * HINWEIS: Die CORS-Konfiguration wurde vollständig in die SecurityConfig verschoben.
 * Grund: Der Spring Security Filter greift VOR dem WebMVC Filter.
 * Eine Konfiguration an dieser Stelle würde von Security-Filtern ignoriert werden oder zu Konflikten führen.
 * Diese Klasse wird als Platzhalter für zukünftige MVC-spezifische Konfigurationen (z.B. Interceptors) beibehalten.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS wird zentral in SecurityConfig behandelt (siehe dort: corsConfigurationSource).
    // Keine addCorsMappings nötig.

}