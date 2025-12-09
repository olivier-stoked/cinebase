import apiClient from "./api-client";


// Dieser Service stellt die Verbindung zum Backend her.

/**
 * Fügt eine neue Bewertung hinzu.
 * Quelle: Analogie zu game-service.js (Daten senden)
 * @param {Object} reviewData - { movieId, rating, comment }
 */
export const addReview = async (reviewData) => {
    try {
        const response = await apiClient.post("/reviews", reviewData);
        return response.data;
    } catch (error) {
        console.error("Fehler beim Senden der Bewertung:", error);
        throw error;
    }
};

/**
 * Lädt die Durchschnittsbewertung für einen Film.
 * Quelle: Analogie zu leaderboard-service.js (Statistiken laden)
 * @param {number} movieId
 */
export const getAverageRating = async (movieId) => {
    try {
        const response = await apiClient.get(`/reviews/movie/${movieId}/average`);
        return response.data;
    } catch (error) {
        console.error(`Fehler beim Laden des Durchschnitts für Film ${movieId}:`, error);
        // Kein Fehler werfen, sondern 0.0 zurückgeben, damit die UI nicht kaputt geht
        return 0.0;
    }
};

/**
 * Lädt alle Reviews für einen Film.
 */
export const getReviewsByMovie = async (movieId) => {
    try {
        const response = await apiClient.get(`/reviews/movie/${movieId}`);
        return response.data;
    } catch (error) {
        console.error(`Fehler beim Laden der Reviews für Film ${movieId}:`, error);
        throw error;
    }
};
