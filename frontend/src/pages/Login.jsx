import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import LoginForm from "../components/LoginForm";

const Login = () => {
    const navigate = useNavigate();

    // ! Zugriff auf den globalen AuthContext
    // Wir nutzen die login-Funktion aus dem Context, damit der globale State (user, token) aktualisiert wird.
    const { login } = useAuth();

    // Lokaler State für Fehlermeldungen auf der Seite
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    /**
     * Handler für den Login-Versuch.
     * Wird von der LoginForm-Komponente aufgerufen, wenn der User auf "Einloggen" klickt.
     */
    const handleLogin = async (usernameOrEmail, password) => {
        setError(""); // Alte Fehler löschen
        setIsLoading(true);

        try {
            console.log("Login-Prozess gestartet...");

            // ! API-Aufruf via Context
            // Hier wird der Token geholt und im LocalStorage + Context gespeichert
            await login(usernameOrEmail, password);

            console.log("Login erfolgreich! Weiterleitung...");

            // ! Redirect zur Startseite (oder Dashboard)
            navigate("/");

        } catch (err) {
            console.error("Fehler auf Login-Page:", err);
            // Die Fehlermeldung kommt aus dem auth-service (z.B. "Bad Credentials")
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

                {/* ! Fehlermeldung anzeigen, falls Login schiefgeht */}
                {error && (
                    <div className="error-message" style={{
                        backgroundColor: "#ffebee",
                        padding: "10px",
                        borderRadius: "4px",
                        marginBottom: "1rem",
                        textAlign: "center",
                        border: "1px solid #ef9a9a"
                    }}>
                        {error}
                    </div>
                )}

                {/* Das Formular-Component einbinden */}
                <LoginForm onLogin={handleLogin} isLoading={isLoading} />

                {/* Footer Links */}
                <div style={{ marginTop: "1.5rem", textAlign: "center", fontSize: "0.9rem", color: "#888" }}>
                    <p>
                        Noch keinen Account? <span style={{ color: "#666" }}>(Registrierung folgt)</span>
                    </p>
                </div>

                {/* ! Hilfreiche Info für Testzwecke (kann später weg) */}
                <div style={{ marginTop: "20px", fontSize: "0.8rem", color: "#666", background: "#f0f0f0", padding: "10px", borderRadius: "4px" }}>
                    <strong>Test-Login:</strong><br/>
                    User: <code>admin</code><br/>
                    Pass: <code>admin123</code>
                </div>
            </div>
        </div>
    );
};

export default Login;
