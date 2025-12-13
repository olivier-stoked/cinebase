import apiClient from "./api-client";

/**
 * Lädt alle Filme vom Backend.
 * @returns {Promise<Array>} Liste von MovieDTOs.
 */
export const getAllMovies = async () => {
    try {
        const response = await apiClient.get("/movies");
        return response.data;
    } catch (error) {
        console.error("Fehler beim Laden der Filme:", error);
        throw error;
    }
};

/**
 * Lädt einen einzelnen Film anhand der ID.
 * @param {number} id - Die ID des Films.
 */
export const getMovieById = async (id) => {
    try {
        const response = await apiClient.get(`/movies/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Fehler beim Laden von Film ${id}:`, error);
        throw error;
    }
};

/**
 * Erstellt einen neuen Film (nur für ADMIN).
 * @param {Object} movieData - Das MovieDTO.
 */
export const createMovie = async (movieData) => {
    try {
        const response = await apiClient.post("/movies", movieData);
        return response.data;
    } catch (error) {
        console.error("Fehler beim Erstellen des Films:", error);
        throw error;
    }
};

/**
 * Aktualisiert einen bestehenden Film (nur für ADMIN).
 * @param {number} id - Die ID des Films.
 * @param {Object} movieData - Die aktualisierten Daten.
 */
export const updateMovie = async (id, movieData) => {
    try {
        const response = await apiClient.put(`/movies/${id}`, movieData);
        return response.data;
    } catch (error) {
        console.error(`Fehler beim Aktualisieren von Film ${id}:`, error);
        throw error;
    }
};

/**
 * Löscht einen Film (nur für ADMIN).
 * @param {number} id - Die ID des Films.
 */
export const deleteMovie = async (id) => {
    try {
        await apiClient.delete(`/movies/${id}`);
    } catch (error) {
        console.error(`Fehler beim Löschen von Film ${id}:`, error);
        throw error;
    }
};