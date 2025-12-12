package com.wiss.cinebase.exception;

// Importiert das Fehler-DTO für die einheitliche JSON-Antwort.
import com.wiss.cinebase.dto.ErrorResponseDTO;

// Importiert HTTP-Statuscodes (404, 401, 500 etc.).
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Importiert Security-Exceptions für Login-Fehler.
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Importiert Validierungs-Exceptions und Hilfsklassen.
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

// Importiert Annotationen für die globale Fehlerbehandlung.
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Importiert WebRequest, um Details zur aufgerufenen URL zu erhalten.
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Zentraler Exception Handler.
 * Fängt Fehler aus allen Controllern zentral ab und wandelt sie in saubere JSON-Antworten (ErrorResponseDTO) um.
 * Quelle: Block 06A - Error Handling & Validation
 * Konzepte:
 * - @RestControllerAdvice: Aspekt-orientierte Programmierung (AOP) für globale Fehlerbehandlung.
 * - Separation of Concerns: Controller bleiben sauber, Fehlerlogik ist hier gebündelt.
 */
@RestControllerAdvice // ! Modernere Variante von @ControllerAdvice, ideal für REST APIs (automatisch @ResponseBody).
public class GlobalExceptionHandler {

    /**
     * Behandelt Validierungsfehler (JSR-380 / @Valid im Controller).
     * Beispiel: Titel ist leer oder Jahr < 1888.
     * @return HTTP 400 Bad Request mit Liste der fehlerhaften Felder.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        // Sammeln aller Feld-Fehler in einer Map
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Bau einer lesbaren Fehlermeldung für den Client
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
     * Behandelt die spezifische MovieNotFoundException.
     * ! Wichtig für REST: Gibt 404 (Not Found) zurück, statt generischem 500er Fehler.
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
     * Behandelt Login-Fehler (Falsches Passwort oder unbekannter User).
     * ! Security: Gibt keine Details preis ("Benutzer nicht gefunden" vs "Passwort falsch"), um User Enumeration zu verhindern.
     */
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleAuthErrors(
            Exception ex, WebRequest request) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "AUTH_ERROR",
                "Benutzername oder Passwort falsch", // ! Pauschale Meldung aus Sicherheitsgründen.
                HttpStatus.UNAUTHORIZED.value(),
                extractPath(request)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * General Exception Handler ("Catch-All").
     * Fängt alle anderen, unerwarteten Fehler ab (z.B. NullPointer, Datenbankverbindung weg).
     * @return HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(
            Exception ex, WebRequest request) {

        // ! Loggen für Debugging (nur serverseitig sichtbar, nicht im Client-Response).
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

    // Hilfsmethode: Extrahiert die URL aus dem Request ohne den Präfix "uri=".
    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}