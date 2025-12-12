package com.wiss.cinebase.exception;

/**
 * Benutzerdefinierte Exception für nicht gefundene Filme.
 * Wird geworfen, wenn eine ID im Repository nicht existiert.
 * ! Der GlobalExceptionHandler fängt diese Exception ab und wandelt sie in einen HTTP 404 Status um.
 */
public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Long id) {
        super("Film mit der ID " + id + " wurde nicht gefunden.");
    }
}