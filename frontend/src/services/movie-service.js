import apiClient from "./api-client";

// Quelle: Block 04A - Axios Interceptor
// Wir nutzen den zentralen apiClient, damit der Token automatisch mitgeschickt wird.

export const getAllMovies = async () => {
    try {
        const response = await apiClient.get("/movies");
        return response.data;
    } catch (error) {
        console.error("Fehler beim Laden der Filme:", error);
        throw error;
    }
};

// Quelle: Block 04B - CRUD Operations
// Wir benoetigen diese Methode, um die Details fuer das Bearbeiten zu laden (optional)
export const getMovieById = async (id) => {
    try {
        const response = await apiClient.get(`/movies/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Fehler beim Laden von Film ${id}:`, error);
        throw error;
    }
};

export const createMovie = async (movieData) => {
    try {
        const response = await apiClient.post("/movies", movieData);
        return response.data;
    } catch (error) {
        console.error("Fehler beim Erstellen des Films:", error);
        throw error;
    }
};

// Quelle: Block 04B - CRUD Operations (Update)
// Wir nutzen PUT fuer Updates, da wir die gesamte Ressource aktualisieren.
// Die ID wird in der URL uebergeben, die Daten im Body.
export const updateMovie = async (id, movieData) => {
    try {
        const response = await apiClient.put(`/movies/${id}`, movieData);
        return response.data;
    } catch (error) {
        console.error(`Fehler beim Aktualisieren von Film ${id}:`, error);
        throw error;
    }
};

export const deleteMovie = async (id) => {
    try {
        await apiClient.delete(`/movies/${id}`);
    } catch (error) {
        console.error(`Fehler beim Loeschen von Film ${id}:`, error);
        throw error;
    }
};