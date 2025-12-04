import { createContext, useContext, useState, useEffect } from "react";
import {
    login as apiLogin,
    logout as apiLogout,
    getUserData,
} from "../services/auth-service";

// 1. Context erstellen
// eslint-disable-next-line react-refresh/only-export-components
export const AuthContext = createContext();

// Globaler Status: user und isauthenticated is überall in der App verfügbar.
// Provider Component
export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true); // Wichtig: Startet als true!

    // Beim Laden der Seite: Prüfen, ob wir noch eingeloggt sind
    // ! useEffect sorgt dafür, dass der user beim Neuladen der Seite eingeloggt bleibt
    useEffect(() => {
        const checkAuth = () => {
            console.log("Prüfe Auth-Status...");
            const storedUser = getUserData();
            const storedToken = localStorage.getItem("authToken");

            if (storedUser && storedToken) {
                console.log("User wiederhergestellt:", storedUser.username);
                setUser(storedUser);
                setToken(storedToken);
                setIsAuthenticated(true);
            } else {
                console.log("Kein User gefunden");
                setIsAuthenticated(false);
            }
            setIsLoading(false);
        };

        checkAuth();
    }, []);

    /**
     * Login-Funktion
     * @param {string} usernameOrEmail - Username oder Email
     * @param {string} password - Passwort
     */
    const login = async (usernameOrEmail, password) => {
        // Wir brauchen kein try/catch, da der Fehler automatisch and die Komponente (LoginForm) weitergeleitet wird
        // wenn, wenn apiLogin fehlschlägt

        console.log("📧 AuthContext: Login für", usernameOrEmail);

        // API Call
        const response = await apiLogin(usernameOrEmail, password);

        // Wenn wir hier ankommen, war der Login erfolgreich (sonst hätte apiLogin einen Fehler geworfen)

        // State aktualisieren
        setToken(response.token);
        setUser({
            // Achtung: Backend sendet 'userId', nicht 'id' im DTO
            id: response.userId,
            username: response.username,
            email: response.email,
            role: response.role,
        });
        setIsAuthenticated(true);

        console.log("AuthContext: Login erfolgreich");
        return response;
    };

    // Logout-Funktion
    const logout = () => {
        apiLogout();
        setUser(null);
        setToken(null);
        setIsAuthenticated(false);
        window.location.href = "/";
    };

    const value = {
        user,
        token,
        isAuthenticated,
        isLoading,
        login,
        logout,
    };

    // ! if (isLoading)... Ladeschutz - BESSER ERKLÄREN:
    // Warten, bis der Auth-Check fertig ist, bevor wir die App anzeigen.
    // Erklärung: Beim Neuladen der Seite (F5) ist 'isAuthenticated' kurzzeitig 'false',
    // bis der useEffect oben den User aus dem LocalStorage geladen hat.
    // Ohne diesen Schutz würde die App kurz die Login-Seite zeigen (Flackern),
    // obwohl der User eigentlich eingeloggt ist.
    if (isLoading) {
        return (
            <div style={{ textAlign: "center", marginTop: "50px" }}>
                Lade CINEBASE...
            </div>
        );
    }

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// Custom Hook
// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth muss innerhalb von AuthProvider verwendet werden!");
    }
    return context;
};
