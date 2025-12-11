package com.wiss.cinebase.exception;

import com.wiss.cinebase.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice; // Besser für APIs als ControllerAdvice
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Zentraler Exception Handler.
 * Fängt Fehler aus allen Controllern zentral ab und wandelt sie in saubere JSON-Antworten (ErrorResponseDTO) um.
 *
 * Quelle: Block 06A - Error Handling & Validation
 * Konzepte:
 * - @RestControllerAdvice: Aspekt-orientierte Programmierung (AOP) für Fehlerbehandlung.
 * - Separation of Concerns: Controller bleiben sauber, Fehlerlogik ist hier gebündelt.
 */

// ! Ich habe Ihren Code genommen und folgende Verbesserungen eingebaut:
// ! @RestControllerAdvice: Das ist moderner für JSON APIs als @ControllerAdvice.
// ! handleMovieNotFound: Hinzugefügt. Wichtig, damit das Frontend weiß "Film weg" (404) und nicht "Server kaputt" (500).
// !Kurs-Kommentare: Block 06A Referenzen eingefügt.

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Behandelt Validierungsfehler (JSR-380 / @Valid im Controller).
     * Beispiel: Titel ist leer oder Jahr < 1888.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        // Wir sammeln alle Fehler in einer Map
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Wir bauen eine lesbare Nachricht für den User
        StringBuilder message = new StringBuilder("Validierung fehlgeschlagen: ");
        errors.forEach((field, error) -> message.append(field).append(" ").append(error).append("; "));

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "VALIDATION_ERROR",
                message.toString(),
                HttpStatus.BAD_REQUEST.value(),
                extractPath(request)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Behandelt unsere spezifische MovieNotFoundException.
     * Wichtig für REST: Gibt 404 (Not Found) zurück, nicht 500.
     */
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleMovieNotFound(
            MovieNotFoundException ex, WebRequest request) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "MOVIE_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                extractPath(request)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Behandelt Login-Fehler (Falsches Passwort/User).
     */
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleAuthErrors(
            Exception ex, WebRequest request) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "AUTH_ERROR",
                "Benutzername oder Passwort falsch", // Keine Details ausgeben (Security!)
                HttpStatus.UNAUTHORIZED.value(),
                extractPath(request)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * General Exception Handler ("Catch-All").
     * Fängt alle anderen, unerwarteten Fehler ab (z.B. NullPointer, Datenbank weg).
     * Security: Wir geben KEINEN Stacktrace an den Client, loggen ihn aber intern.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(
            Exception ex, WebRequest request) {

        // Loggen für Debugging (nur serverseitig sichtbar)
        System.err.println("CRITICAL ERROR: " + ex.getMessage());
        ex.printStackTrace();

        ErrorResponseDTO error = new ErrorResponseDTO(
                "INTERNAL_SERVER_ERROR",
                "Ein unerwarteter Fehler ist aufgetreten. Bitte Admin kontaktieren.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                extractPath(request)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Hilfsmethode: Extrahiert die URL aus dem Request
    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
