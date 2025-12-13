import apiClient from "./api-client";

/**
 * Führt den Login durch, speichert Token und User-Daten.
 * @param {string} email - E-Mail oder Username des Users.
 * @param {string} password - Das Passwort.
 * @returns {Promise<Object>} Die Antwort des Backends (LoginResponseDTO).
 */
export const login = async (email, password) => {
    try {
        // POST Request an Backend
        const response = await apiClient.post("/auth/login", {
            usernameOrEmail: email,
            password,
        });

        // Daten aus der Antwort extrahieren
        const { token, userId, username, role } = response.data;

        // Token im localStorage speichern
        localStorage.setItem("authToken", token);

        // User-Daten speichern
        const userData = { id: userId, username, email, role };
        localStorage.setItem("userData", JSON.stringify(userData));

        return response.data;
    } catch (error) {
        console.error("Login fehlgeschlagen:", error);

        // Fehlermeldung extrahieren
        const errorMessage =
            error.response?.data?.message || "Login fehlgeschlagen";

        throw new Error(errorMessage);
    }
};

/**
 * Meldet den Benutzer ab und bereinigt den lokalen Speicher.
 */
export const logout = () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("userData");

    // Weiterleitung zur Startseite
    window.location.href = "/";
};

/**
 * Prüft, ob ein Benutzer aktuell eingeloggt ist.
 * @returns {boolean}
 */
export const isAuthenticated = () => {
    const token = localStorage.getItem("authToken");
    return !!token;
};

/**
 * Liest die gespeicherten User-Daten aus dem localStorage.
 * @returns {Object|null} Das User-Objekt oder null.
 */
export const getUserData = () => {
    const userDataString = localStorage.getItem("userData");
    if (userDataString) {
        return JSON.parse(userDataString);
    }
    return null;
};

/**
 * Registriert einen neuen Benutzer.
 * @param {Object} userData - Das RegisterRequestDTO.
 */
export const register = async (userData) => {
    try {
        const response = await apiClient.post("/auth/register", userData);
        return response.data;
    } catch (error) {
        console.error("Registrierung fehlgeschlagen:", error);
        const errorMessage = error.response?.data?.message || "Registrierung fehlgeschlagen";
        throw new Error(errorMessage);
    }
};