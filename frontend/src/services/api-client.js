import axios from "axios";

// Adresse Spring Boot Backend
const API_BASE_URL = "http://localhost:8080/api";

// Axios-Instanz
const apiClient = axios.create({
    baseURL: API_BASE_URL,
    // Abbruch nach 10 Sekunden, falls Backend failed
    timeout: 10000,
    headers: {
        "Content-Type": "application/json",
    },
});


// ! Request Interceptor: automatisch vor jedem Request ausgeführt
apiClient.interceptors.request.use(
    (config) => {
        // Token aus Browser-Speicher holen
        const token = localStorage.getItem("authToken");

        // Token an den Header hängen
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);


// ! Response Interceptor: automatisch nach jeder Response ausgeführt
apiClient.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        // Fehlerbehandlung
        if (error.response) {
            const status = error.response.status;

            // ! 401 = Unauthorized "Token ungültig oder abgelaufen"
            if (status === 401) {
                console.warn("🚪 Token ungültig - Automatischer Logout");

                // Sorting
                localStorage.removeItem("authToken");
                localStorage.removeItem("userData");

                // Redirect zum Login
                window.location.href = "/login";
            }

            // ! 403 = Forbidden "User ist unerlaubt eingeloggt"
            if (status === 403) {
                console.error("Keine Berechtigung für diese Aktion");
            }
        }

        return Promise.reject(error);
    }
);

export default apiClient;
