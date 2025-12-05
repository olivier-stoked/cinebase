package com.wiss.cinebase.dto;

/**
 * DTO für die Antwort nach erfolgreicher Registrierung.
 * Gibt KEIN Passwort zurück!
 *
 * Quelle: Block 01B - DTOs und AuthController
 */
public class RegisterResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String role;

    public RegisterResponseDTO(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
