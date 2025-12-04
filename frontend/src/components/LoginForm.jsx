import { useState } from "react";

/**
 * Login Formular Komponente
 * - Kümmert sich nur um UI und Eingabe-Validierung
 * - Ruft bei Erfolg die onLogin-Funktion auf
 */
const LoginForm = ({ onLogin, isLoading }) => {
    // State für die Eingabefelder
    const [usernameOrEmail, setUsernameOrEmail] = useState("");
    const [password, setPassword] = useState("");

    // State für Validierungs-Fehler
    const [errors, setErrors] = useState({});

    // Validierungs-Logik
    const validate = () => {
        const newErrors = {};

        if (!usernameOrEmail.trim()) {
            newErrors.username = "Benutzername oder E-Mail ist erforderlich";
        } else if (usernameOrEmail.length < 3) {
            newErrors.username = "Mindestens 3 Zeichen erforderlich";
        }

        if (!password) {
            newErrors.password = "Passwort ist erforderlich";
        } else if (password.length < 6) {
            newErrors.password = "Passwort muss mindestens 6 Zeichen lang sein";
        }

        setErrors(newErrors);
        // Das Formular ist valide, wenn das Error-Objekt leer ist
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // 1. Validieren
        if (!validate()) {
            return;
        }

        // 2. Daten an die Eltern-Komponente (Login.jsx) geben
        onLogin(usernameOrEmail, password);
    };

    return (
        <form onSubmit={handleSubmit} className="auth-form">
            {/* Username / Email Feld */}
            <div style={{ marginBottom: "1rem" }}>
                <label
                    htmlFor="username"
                    style={{ display: "block", marginBottom: "0.5rem", fontSize: "0.9rem", color: "#666" }}
                >
                    Benutzername oder E-Mail
                </label>
                <input
                    id="username"
                    type="text"
                    className={`form-input ${errors.username ? "form-input--error" : ""}`}
                    value={usernameOrEmail}
                    onChange={(e) => setUsernameOrEmail(e.target.value)}
                    placeholder="z.B. admin"
                    disabled={isLoading}
                />
                {errors.username && <span className="error-message">{errors.username}</span>}
            </div>

            {/* Passwort Feld */}
            <div style={{ marginBottom: "1.5rem" }}>
                <label
                    htmlFor="password"
                    style={{ display: "block", marginBottom: "0.5rem", fontSize: "0.9rem", color: "#666" }}
                >
                    Passwort
                </label>
                <input
                    id="password"
                    type="password"
                    className={`form-input ${errors.password ? "form-input--error" : ""}`}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Dein Passwort"
                    disabled={isLoading}
                />
                {errors.password && <span className="error-message">{errors.password}</span>}
            </div>

            {/* Submit Button */}
            <button
                type="submit"
                disabled={isLoading}
                style={{ width: "100%", marginTop: "1rem" }}
            >
                {isLoading ? "Wird eingeloggt..." : "Einloggen"}
            </button>
        </form>
    );
};

export default LoginForm;
