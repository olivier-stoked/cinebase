package com.wiss.cinebase.config;

// Quelle: Block 04 - API Documentation (OpenAPI Standards)
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfiguration für OpenAPI / Swagger UI.
 * Ermöglicht die automatische Generierung der API-Dokumentation.
 * Erreichbar unter: http://localhost:8080/swagger-ui.html
 * Quelle: Block 04 - API Dokumentation
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("CINEBASE API")
                .version("1.0.0")
                .description("REST API für die CINEBASE Filmdatenbank (Modul 223).")
                .contact(new Contact()
                        .name("Cinebase Dev Team")
                        .email("dev@cinebase.ch")));
    }
}