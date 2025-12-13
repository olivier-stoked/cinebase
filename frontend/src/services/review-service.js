import apiClient from "./api-client";

/**
 * Sendet eine neue Bewertung an das Backend.
 * @param {Object} reviewData - Das ReviewDTO.
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
 * @param {number} movieId - Die ID des Films.
 * @returns {Promise<number>} Der Durchschnittswert.
 */
export const getAverageRating = async (movieId) => {
    try {
        const response = await apiClient.get(`/reviews/movie/${movieId}/average`);
        return response.data;
    } catch (error) {
        console.error(`Fehler beim Laden des Durchschnitts für Film ${movieId}:`, error);
        // Fallback auf 0.0 bei Fehler
        return 0.0;
    }
};

/**
 * Lädt alle Reviews zu einem bestimmten Film.
 * @param {number} movieId - Die ID des Films.
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