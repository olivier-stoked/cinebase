package com.wiss.cinebase.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO für die Registrierungs-Anfrage.
 * Enthält Validierungs-Regeln für sichere Inputs.
 *
 * Quelle: Block 01B - DTOs und AuthController
 */
public class RegisterRequestDTO {

    @NotBlank(message = "Username ist erforderlich")
    @Size(min = 3, max = 50, message = "Username muss 3-50 Zeichen haben")
    private String username;

    @NotBlank(message = "Email ist erforderlich")
    @Email(message = "Email muss gültig sein")
    private String email;

    @NotBlank(message = "Passwort ist erforderlich")
    @Size(min = 6, message = "Passwort muss mindestens 6 Zeichen haben")
    private String password;

    // Default Constructor für JSON Deserialisierung
    public RegisterRequestDTO() {}

    // Getter & Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
