package com.wiss.cinebase.service;

import com.wiss.cinebase.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Lädt Benutzerdaten für Spring Security aus der Datenbank.
 * Wird vom JwtAuthenticationFilter und AuthenticationManager benötigt.
 */
@Service // WICHTIG: Das erzeugt die Bean, die gefehlt hat!
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Sucht den User in der DB. Da AppUser das UserDetails-Interface implementiert,
        // können wir ihn direkt zurückgeben.
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User nicht gefunden: " + username));
    }
}
