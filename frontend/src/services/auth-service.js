

// Login-Funktion nach 04A

import apiClient from "./api-client";

/**
 * Login Funktion
 * Sendet Email + Passwort an Backend und speichert Token
 *
 * @param {string} email - User Email (oder Username)
 * @param {string} password - User Passwort
 * @returns {Promise<Object>} User Daten + Token
 */
export const login = async (email, password) => {
    try {
        console.log("📧 Login-Versuch für:", email);

        // POST Request an Backend
        // HINWEIS: Unser Backend (AuthController) erwartet "usernameOrEmail" im Body
        const response = await apiClient.post("/auth/login", {
            usernameOrEmail: email,
            password,
        });

        // Token und User-Daten aus Response extrahieren
        // (Die Struktur entspricht dem LoginResponseDTO aus dem Backend)
        const { token, id, username, role } = response.data;

        // 1. Token in localStorage speichern (für zukünftige Requests)
        localStorage.setItem("authToken", token);

        // 2. User-Daten auch speichern (für schnellen Zugriff ohne API-Call)
        const userData = { id, username, email, role };
        localStorage.setItem("userData", JSON.stringify(userData));

        console.log("✅ Login erfolgreich - Token gespeichert");

        // Gesamte Response zurückgeben
        return response.data;
    } catch (error) {
        console.error("❌ Login fehlgeschlagen:", error);

        // Fehlermeldung vom Backend extrahieren (falls vorhanden)
        const errorMessage =
            error.response?.data?.error || "Login fehlgeschlagen";

        // Error mit besserer Message weiterwerfen
        throw new Error(errorMessage);
    }
};

/**
 * Logout Funktion
 * Löscht Token und User-Daten aus localStorage
 */
export const logout = () => {
    console.log("🚪 Logout - Token wird gelöscht");
    localStorage.removeItem("authToken");
    localStorage.removeItem("userData");

    // Optional: Hard Redirect zur Home-Page, um den State komplett zu leeren
    window.location.href = "/";
};

/**
 * Prüft ob User eingeloggt ist
 * @returns {boolean} true wenn Token existiert
 */
export const isAuthenticated = () => {
    const token = localStorage.getItem("authToken");
    return !!token; // !! konvertiert string zu boolean (true wenn string existiert)
};

/**
 * Gibt den aktuellen Token zurück
 * @returns {string|null} Token oder null
 */
export const getToken = () => {
    return localStorage.getItem("authToken");
};

/**
 * Hole User-Daten aus localStorage
 * (Nützlich um User-Namen anzuzeigen, ohne das Backend zu fragen)
 */
export const getUserData = () => {
    const userDataString = localStorage.getItem("userData");
    if (userDataString) {
        return JSON.parse(userDataString);
    }
    return null;
};

/**
 * Register Funktion (optional für später)
 */
export const register = async (userData) => {
    try {
        console.log("📝 Registrierung für:", userData.email);
        const response = await apiClient.post("/auth/register", userData);
        console.log("✅ Registrierung erfolgreich");
        return response.data;
    } catch (error) {
        console.error("❌ Registrierung fehlgeschlagen:", error);
        const errorMessage = error.response?.data?.error || "Registrierung fehlgeschlagen";
        throw new Error(errorMessage);
    }
};