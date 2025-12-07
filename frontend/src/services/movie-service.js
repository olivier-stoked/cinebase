import apiClient from "./api-client";

/**
 * Ruft alle Filme vom Backend ab.
 * Zugriff: Nur für eingeloggte User (ADMIN oder USER).
 * * @returns {Promise<Array>} Liste von MovieDTOs
 */
export const getAllMovies = async () => {
    try {
        const response = await apiClient.get("/movies");
        return response.data;
    } catch (error) {
        console.error("❌ Fehler beim Laden der Filme:", error);
        throw error;
    }
};

/**
 * Ruft einen einzelnen Film nach ID ab.
 */
export const getMovieById = async (id) => {
    try {
        const response = await apiClient.get(`/movies/${id}`);
        return response.data;
    } catch (error) {
        console.error(`❌ Fehler beim Laden von Film ${id}:`, error);
        throw error;
    }
};

// Weitere Methoden (create, update, delete) fügen wir später für den Admin hinzu.