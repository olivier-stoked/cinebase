package com.wiss.cinebase.exception;

// Wenn ein Film nicht gefunden wird, kommt die Exception zum Einsatz.
// ! Der GlobalExceptionHandler fängt sie ab und erstellt einen 404-Fehler.
public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Long id) {
        super("Film mit der ID " + id + "wurde nicht gefunden.");
    }
}

