import axios from "axios";

// Basis-URL für das Spring Boot Backend
const API_BASE_URL = "http://localhost:8080/api";

// Axios-Instanz erstellen
const apiClient = axios.create({
    baseURL: API_BASE_URL,
    timeout: 10000,
    headers: {
        "Content-Type": "application/json",
    },
});

// Request Interceptor: Wird vor jedem Request ausgeführt
apiClient.interceptors.request.use(
    (config) => {
        // Token aus dem lokalen Browser-Speicher holen
        const token = localStorage.getItem("authToken");

        // Wenn ein Token existiert, wird er an den Authorization-Header angehängt
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response Interceptor: Wird nach jeder Antwort ausgeführt
apiClient.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        // Globale Fehlerbehandlung
        if (error.response) {
            const status = error.response.status;

            // 401: Unauthorized (Token ungültig oder abgelaufen)
            if (status === 401) {
                console.warn("Token ungültig - Automatischer Logout wird durchgeführt");

                // Speicher bereinigen
                localStorage.removeItem("authToken");
                localStorage.removeItem("userData");

                // Weiterleitung zur Login-Seite
                window.location.href = "/login";
            }

            // 403: Forbidden (Rolle reicht nicht aus)
            if (status === 403) {
                console.error("Keine Berechtigung für diese Aktion");
            }
        }

        return Promise.reject(error);
    }
);

export default apiClient;