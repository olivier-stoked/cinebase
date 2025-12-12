package com.wiss.cinebase.dto;

// Importiert Validierungs-Annotationen für E-Mail-Format, Pflichtfelder und Textlänge.
// Quelle: Block 06A - Form Validation Basics
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO für die Registrierungs-Anfrage.
 * Enthält Validierungs-Regeln für sichere Inputs (Bean Validation).
 * Quelle: Block 01B - DTOs und AuthController
 * Validierung: Block 06A (Backend-seitig)
 */
public class RegisterRequestDTO {

    @NotBlank(message = "Benutzername ist erforderlich")
    @Size(min = 3, max = 50, message = "Benutzername muss zwischen 3 und 50 Zeichen lang sein")
    private String username;

    @NotBlank(message = "E-Mail ist erforderlich")
    @Email(message = "E-Mail muss ein gültiges Format haben")
    private String email;

    @NotBlank(message = "Passwort ist erforderlich")
    @Size(min = 6, message = "Passwort muss mindestens 6 Zeichen lang sein")
    private String password;

    // Default Konstruktor für JSON-Deserialisierung
    public RegisterRequestDTO() {}

    // Getter und Setter

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}