package com.wiss.cinebase.dto;

// Importiert die Klasse für Zeitstempel zur Protokollierung des Fehlerzeitpunkts.
import java.time.LocalDateTime;

/**
 * Standard-Struktur für Fehlermeldungen an das Frontend.
 * Statt wirren Stacktraces schicken wir dieses saubere JSON-Objekt.
 * Verwendung im GlobalExceptionHandler (siehe Block 02B/06B).
 */
public class ErrorResponseDTO {
    private String errorCode; // z.B. "NOT_FOUND"
    private String message;   // z.B. "Film mit ID 5 nicht gefunden"
    private int status;       // z.B. 404
    private String path;      // z.B. "/api/movies/5"
    private LocalDateTime timestamp;

    public ErrorResponseDTO(String errorCode, String message, int status, String path) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    // Getter

    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public String getPath() { return path; }
    public LocalDateTime getTimestamp() { return timestamp; }
}