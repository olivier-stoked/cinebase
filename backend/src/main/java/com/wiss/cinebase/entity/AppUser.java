package com.wiss.cinebase.entity;

import jakarta.persistence.*;

// Java-Klasse als ein Objekt markiert.
@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    // Primary key und autoincrement
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ! Optimistic Locking (verhindert, dass sich 2 Admins gegenseitig überschreiben)
    // Unerlässlich für Multi-User Anwendung. Das Problem des Lost Update wird hier behoben.
    // Das Versionsfeld schützt vor Datenverlust. "Last commit wins".
    @Version
    private Long version;

    // Feld muss gefüllt sein (Pflichtfeld).
    // Keine zwei User mit demselben Namen.
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // oder derselben E-Mail.
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    // EnumType.STRING speichert den Namen (z.B. "ADMIN") in der Datenbank.
    // ! Der Standard (ORDINAL) würde nur die Zahl speichern (0, 1, ...), (KI Info)
    // was zu falschen Berechtigungen führt, wenn man später neue Rollen einfügt.
    // Daher STRING statt ORDINAL.
    // @Enumerated: der Übersetzer - nimmt Namen aus dem Enum und schreibt ihn als Text in die DB-Tabelle.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    // Dieses Feld muss einer der Werte (ADMIN, USER) aus der Role KLasse sein.
    private Role role;

    // Leerer Konstruktor (obligatorisch für JPA)
    public AppUser() {
    }

    // Konstruktor AppUser
    public AppUser(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getter und Setter

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
