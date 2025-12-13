import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import LoginForm from "../components/LoginForm";

/**
 * Seite für den Login-Prozess.
 * Verbindet das LoginForm mit dem AuthContext.
 * Quelle: Block 03B - Login Flow
 */
const Login = () => {
    const navigate = useNavigate();

    // Zugriff auf den globalen AuthContext.
    // Die login-Funktion aktualisiert den globalen State (user, token).
    const { login } = useAuth();

    // Lokaler State für Fehlermeldungen und Ladeindikator.
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    /**
     * Handler für den Login-Versuch.
     * Wird von der LoginForm-Komponente aufgerufen.
     * @param {string} usernameOrEmail
     * @param {string} password
     */
    const handleLogin = async (usernameOrEmail, password) => {
        setError(""); // Reset alter Fehler
        setIsLoading(true);

        try {
            console.log("Login-Prozess gestartet...");

            // API-Aufruf via Context (holt Token und speichert ihn).
            await login(usernameOrEmail, password);

            console.log("Login erfolgreich! Weiterleitung...");

            // Redirect zum Filmkatalog nach erfolgreichem Login.
            navigate("/movies");

        } catch (err) {
            console.error("Fehler auf Login-Page:", err);
            // Fehlermeldung aus dem auth-service anzeigen (z.B. "Bad Credentials").
            setError(err.message || "Login fehlgeschlagen. Bitte prüfen Sie Ihre Daten.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-container">
                <h2 style={{ textAlign: "center", marginBottom: "1.5rem" }}>
                    Welcome to CINEBASE
                </h2>

                {/* Fehleranzeige */}
                {error && (
                    <div className="error-message" style={{
                        backgroundColor: "#ffebee",
                        padding: "10px",
                        borderRadius: "4px",
                        marginBottom: "1rem",
                        textAlign: "center",
                        border: "1px solid #ef9a9a",
                        color: "#c62828"
                    }}>
                        {error}
                    </div>
                )}

                {/* Einbindung der Formular-Komponente */}
                <LoginForm onLogin={handleLogin} isLoading={isLoading} />

                {/* Footer Links */}
                <div style={{ marginTop: "1.5rem", textAlign: "center", fontSize: "0.9rem", color: "#888" }}>
                    <p>
                        Noch keinen Account? <span style={{ color: "#666" }}>(Registrierung via Admin)</span>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Login;