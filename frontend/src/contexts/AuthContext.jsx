import { createContext, useContext, useState, useEffect } from "react";
import {
    login as apiLogin,
    logout as apiLogout,
    getUserData,
} from "../services/auth-service";

// 1. Context erstellen (Der "Container" für unsere Daten)
const AuthContext = createContext();

// 2. Provider Component (Der "Verteiler", der die App umhüllt)
export const AuthProvider = ({ children }) => {

    // --- STATE: Hier speichern wir den aktuellen Zustand der App ---
    const [user, setUser] = useState(null);            // Wer ist eingeloggt? (null = niemand)
    const [token, setToken] = useState(null);          // Der JWT Token
    const [isAuthenticated, setIsAuthenticated] = useState(false); // Sind wir eingeloggt?
    const [isLoading, setIsLoading] = useState(true);  // Laden wir noch beim Start?

    // --- EFFECT: Beim Neuladen der Seite (F5) ---
    // Wir schauen sofort im LocalStorage nach, ob wir noch eingeloggt sind.
    useEffect(() => {
        checkAuth();
    }, []);

    const checkAuth = () => {
        console.log("🔍 Prüfe Auth-Status...");
        const storedUser = getUserData(); // Holt User-Objekt aus localStorage
        const storedToken = localStorage.getItem("authToken");

        if (storedUser && storedToken) {
            console.log("✅ User wiederhergestellt:", storedUser.username);
            setUser(storedUser);
            setToken(storedToken);
            setIsAuthenticated(true);
        } else {
            console.log("❌ Kein User gefunden");
            setIsAuthenticated(false);
        }
        // WICHTIG: Loading beenden, damit die App angezeigt wird
        setIsLoading(false);
    };

    // --- FUNKTION: Login ---
    // Diese Funktion wird vom LoginForm aufgerufen
    const login = async (email, password) => {
        try {
            // Wir rufen den Service auf (der macht den API-Call)
            const response = await apiLogin(email, password);

            // Wenn erfolgreich, aktualisieren wir unseren globalen State
            // (Der Service hat Token & User schon im localStorage gespeichert)
            setUser({
                id: response.id,
                username: response.username,
                email: response.email,
                role: response.role,
            });
            setToken(response.token);
            setIsAuthenticated(true);

            return response;
        } catch (error) {
            // Fehler werfen wir weiter, damit das Formular sie anzeigen kann
            throw error;
        }
    };

    // --- FUNKTION: Logout ---
    const logout = () => {
        apiLogout(); // Löscht localStorage
        // State zurücksetzen
        setUser(null);
        setToken(null);
        setIsAuthenticated(false);
    };

    // Das Objekt, das wir allen Komponenten zur Verfügung stellen
    const value = {
        user,
        token,
        isAuthenticated,
        isLoading,
        login,
        logout,
    };

    // Solange wir noch prüfen (beim Neuladen), zeigen wir nichts oder einen Ladebalken an.
    // Das verhindert, dass der User kurz die Login-Seite sieht, obwohl er eingeloggt ist.
    if (isLoading) {
        return <div>Lade CINEBASE...</div>;
    }

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// 3. Custom Hook (Die "Abkürzung" für die Verwendung)
// Statt `useContext(AuthContext)` schreiben wir in Komponenten nur `useAuth()`
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth muss innerhalb von AuthProvider verwendet werden!");
    }
    return context;
};