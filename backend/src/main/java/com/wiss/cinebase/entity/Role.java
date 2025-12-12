package com.wiss.cinebase.entity;

/**
 * Definition der Benutzerrollen im System.
 * Quelle: Block 01A - User Entity & Rollen
 * Verwendung:
 * - ADMIN: Darf Filme erstellen, bearbeiten, löschen (Festivalleitung).
 * - USER: Darf Filme sehen und Reviews schreiben (Presse/Journalisten).
 */
// ! Exportiert das Enum für die globale Verwendung in Entities und Security-Logik.
public enum Role {
    ADMIN,
    USER
}