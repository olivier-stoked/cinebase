package com.wiss.cinebase.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Java-Klasse als ein Objekt markiert.
@Entity
@Table(name = "app_users")
// WICHTIG: Interface implementieren!
public class AppUser implements UserDetails {

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


    // ! UserDetails Methoden - unabdingbar für Spring Security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Wandelt Role (ADMIN) in eine Spring Authority (ROLE_ADMIN) um
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

    public void setUsername(String username) {
        this.username = username;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
