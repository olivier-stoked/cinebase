package com.wiss.cinebase.entity;

// Enum für die Benutzerrolen in CINEBASE: wer darf was.

public enum Role {

    // Festivalleitung: Darf Filme erstellen, bearbeiten, löschen
    ADMIN,
    // Journalist/Jury: Darf Filme ansehen und bewerten
    USER
}