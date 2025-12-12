package com.wiss.cinebase.dto;

// (Keine Imports nötig, da nur Standard-Java-Datentypen verwendet werden)

/**
 * DTO für die Antwort nach erfolgreichem Login.
 * Enthält den JWT Token und Basis-Informationen zum User für das Frontend (State).
 * Quelle: Block 02A - Login DTOs & Endpoint
 * Sicherheits-Aspekt: Das Passwort wird hier NIEMALS zurückgegeben.
 */
public class LoginResponseDTO {

    private String token;
    private String tokenType = "Bearer"; // Standard-Typ für JWT Autorisierung
    private Long userId;
    private String username;
    private String email;
    private String role;
    private long expiresIn; // Token-Gültigkeit in Millisekunden

    public LoginResponseDTO(String token, Long userId, String username, String email, String role, long expiresIn) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.expiresIn = expiresIn;
    }

    // Getter und Setter

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
}