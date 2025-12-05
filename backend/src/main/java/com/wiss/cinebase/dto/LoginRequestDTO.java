package com.wiss.cinebase.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO für den Login-Versuch.
 * Akzeptiert Username ODER Email für maximale Flexibilität.
 *
 * Quelle: Block 02A - Login DTOs & Endpoint
 */
public class LoginRequestDTO {

    @NotBlank(message = "Username oder Email ist erforderlich")
    private String usernameOrEmail;

    @NotBlank(message = "Passwort ist erforderlich")
    private String password;

    // Default Constructor für JSON Deserialization
    public LoginRequestDTO() {}

    public LoginRequestDTO(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    // Getter & Setter
    public String getUsernameOrEmail() { return usernameOrEmail; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
