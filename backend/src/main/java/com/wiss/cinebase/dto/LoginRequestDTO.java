package com.wiss.cinebase.dto;

// Importiert die Annotation zur Validierung von Pflichtfeldern (darf nicht null und nicht leer sein).
// Quelle: Block 06A - Form Validation Basics
import jakarta.validation.constraints.NotBlank;

/**
 * DTO für den Login-Versuch.
 * Dient der Übertragung der Anmeldedaten vom Frontend zum Backend.
 * Quelle: Block 02A - Login DTOs & Endpoint
 * Besonderheit: Akzeptiert Username ODER Email in einem Feld für maximale Flexibilität (UX).
 */
public class LoginRequestDTO {

    @NotBlank(message = "Benutzername oder E-Mail ist erforderlich")
    private String usernameOrEmail;

    @NotBlank(message = "Passwort ist erforderlich")
    private String password;

    // Default Konstruktor für JSON-Deserialisierung (Jackson)
    public LoginRequestDTO() {}

    public LoginRequestDTO(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    // --- Getter & Setter ---

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}