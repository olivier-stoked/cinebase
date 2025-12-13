import { createContext, useContext, useState, useEffect } from "react";
// Importiert die API-Funktionen für Authentifizierung.
import {
    login as apiLogin,
    logout as apiLogout,
    getUserData,
} from "../services/auth-service";

// 1. Erstellung des Context-Objekts.
// Dient als Container für den globalen Auth-Status.
// eslint-disable-next-line react-refresh/only-export-components
export const AuthContext = createContext();

/**
 * Provider-Komponente für das Authentifizierungs-Management.
 * Kapselt die Logik für Login, Logout und Session-Persistenz.
 * Quelle: Block 03A - AuthContext & Global State
 */
export const AuthProvider = ({ children }) => {
    // Globaler State für den aktuellen Benutzer und Token.
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    // Initialer Ladezustand ist 'true', um die Prüfung beim App-Start abzuwarten.
    const [isLoading, setIsLoading] = useState(true);

    /**
     * Effekt zur Wiederherstellung der Sitzung beim Laden der Seite (z.B. F5 Refresh).
     * Prüft, ob gültige Anmeldedaten im LocalStorage vorhanden sind.
     */
    useEffect(() => {
        const checkAuth = () => {
            console.log("Prüfe Auth-Status...");
            const storedUser = getUserData();
            const storedToken = localStorage.getItem("authToken");

            if (storedUser && storedToken) {
                console.log("Benutzersitzung wiederhergestellt:", storedUser.username);
                setUser(storedUser);
                setToken(storedToken);
                setIsAuthenticated(true);
            } else {
                console.log("Keine aktive Sitzung gefunden.");
                setIsAuthenticated(false);
                setUser(null);
                setToken(null);
            }
            // Beendet den Ladezustand, sobald die Prüfung abgeschlossen ist.
            setIsLoading(false);
        };

        checkAuth();
    }, []);

    /**
     * Führt den Login-Prozess durch.
     * Ruft den API-Service auf und aktualisiert den globalen State.
     * @param {string} usernameOrEmail - Benutzername oder E-Mail.
     * @param {string} password - Das Passwort.
     * @returns {Promise<Object>} Die Antwort des Backends.
     */
    const login = async (usernameOrEmail, password) => {
        // Fehlerbehandlung erfolgt in der aufrufenden Komponente (LoginForm).
        console.log("AuthContext: Login-Versuch für", usernameOrEmail);

        // API-Aufruf an das Backend
        const response = await apiLogin(usernameOrEmail, password);

        // State aktualisieren mit Daten aus dem LoginResponseDTO
        setToken(response.token);
        setUser({
            // WICHTIG: Das Backend sendet 'userId' im DTO, im Frontend nutzen wir 'id'.
            id: response.userId,
            username: response.username,
            email: response.email,
            role: response.role,
        });
        setIsAuthenticated(true);

        console.log("AuthContext: Login erfolgreich");
        return response;
    };

    /**
     * Meldet den Benutzer ab.
     * Bereinigt den lokalen Speicher, setzt den State zurück und erzwingt einen Reload.
     */
    const logout = () => {
        apiLogout();
        setUser(null);
        setToken(null);
        setIsAuthenticated(false);
        // Hard Redirect zur Startseite, um den App-Zustand vollständig zurückzusetzen.
        window.location.href = "/";
    };

    // Das Value-Objekt, das allen Konsumenten zur Verfügung gestellt wird.
    const value = {
        user,
        token,
        isAuthenticated,
        isLoading,
        login,
        logout,
    };

    // Ladeschutz (Flash of Unauthenticated Content Prevention):
    // Verhindert, dass kurzzeitig die Login-Seite angezeigt wird, während
    // der useEffect noch prüft, ob der User eigentlich eingeloggt ist.
    if (isLoading) {
        return (
            <div style={{ textAlign: "center", marginTop: "50px", fontFamily: "sans-serif" }}>
                <h3>Lade CINEBASE...</h3>
            </div>
        );
    }

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// Custom Hook für den einfacheren Zugriff auf den Context in Komponenten.
// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth muss innerhalb von AuthProvider verwendet werden!");
    }
    return context;
};