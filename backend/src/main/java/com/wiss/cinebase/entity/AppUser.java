package com.wiss.cinebase.entity;

// Stellt Annotationen bereit, um Felder bei der JSON-Serialisierung zu ignorieren (z.B. Passwort).
import com.fasterxml.jackson.annotation.JsonIgnore;

// Quelle: Block 03A - JPA Entities
// Importiert die Jakarta Persistence API für das ORM-Mapping (@Entity, @Id, etc.).
import jakarta.persistence.*;

// Quelle: Block 02B - Spring Security
// Importiert Schnittstellen für die Sicherheitslogik (Berechtigungen, User-Details).
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// Importiert Java Utility Klassen für Listen-Strukturen.
import java.util.Collection;
import java.util.List;

/**
 * Repräsentiert einen Benutzer der Anwendung (Admin oder Journalist).
 * Quelle: Block 01A (Entity) & Block 01B (Security Integration)
 * Anpassung Single-User zu Multi-User:
 * - Implementierung des 'UserDetails'-Interfaces für Spring Security.
 * - Integration von Optimistic Locking zur Vermeidung von Lost Updates.
 * - Hinzufügen von Beziehungen zu Filmen (erstellt) und Reviews (verfasst).
 */
@Entity
@Table(name = "app_users")
// ! WICHTIG: Implementiert UserDetails für die Integration in Spring Security.
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ! Optimistic Locking (verhindert, dass sich 2 Admins gegenseitig überschreiben).
    // Unerlässlich für Multi-User Anwendung. Das Problem des "Lost Update" wird hier behoben.
    // Das Versionsfeld schützt vor Datenverlust. "Last commit wins".
    @Version
    private Long version;

    // Feld muss gefüllt sein (Pflichtfeld).
    // Keine zwei User mit demselben Namen erlaubt.
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    @JsonIgnore // ! Sicherheitsrelevant: Passwort-Hash darf niemals im JSON ausgegeben werden.
    private String password;

    // EnumType.STRING speichert den Namen (z.B. "ADMIN") in der Datenbank.
    // ! Der Standard (ORDINAL) würde nur die Zahl speichern (0, 1, ...),
    // was zu falschen Berechtigungen führt, wenn man später neue Rollen einfügt.
    // Daher STRING statt ORDINAL.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ! Multi-User Erweiterung: Rückverweis
    // Welche Filme hat dieser Admin erstellt?
    // Block 03A: One-to-Many Beziehung.
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // ! Verhindert Endlosschleifen bei der JSON-Serialisierung (Zirkelbezug).
    private List<Movie> createdMovies;

    // ! Multi-User Erweiterung: Rückverweis
    // Welche Reviews hat dieser User geschrieben?
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // ! Verhindert Endlosschleifen bei der JSON-Serialisierung.
    private List<Review> reviews;

    // Default Konstruktor für JPA (Pflicht).
    public AppUser() {
    }

    // Konstruktor für die Initialisierung (DataInitializer).
    public AppUser(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // --- UserDetails Methoden (Quelle: Block 01B/02B) ---

    /**
     * Gibt die Berechtigungen (Rollen) des Benutzers zurück.
     * ! Konvention: Spring Security erwartet das Präfix "ROLE_" für Rollen-Checks.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- Getter & Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Movie> getCreatedMovies() {
        return createdMovies;
    }

    public void setCreatedMovies(List<Movie> createdMovies) {
        this.createdMovies = createdMovies;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}