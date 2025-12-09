import { useState, useEffect } from "react";
import ReviewForm from "./ReviewForm";
import { addReview, getAverageRating } from "../services/review-service";

/**
 * Komponente zur Anzeige einer einzelnen Filmkarte.
 * Enthält jetzt auch die Logik zum Anzeigen des Durchschnitts und zum Bewerten.
 */
const MovieCard = ({ movie }) => {
    const [averageRating, setAverageRating] = useState(0);
    const [showReviewForm, setShowReviewForm] = useState(false);

    // --- 1. Funktion ZUERST definieren ---
    // Wir definieren sie hier oben, damit useEffect sie kennt.
    const loadAverage = async () => {
        try {
            const avg = await getAverageRating(movie.id);
            setAverageRating(avg);
        } catch (error) {
            console.error("Fehler beim Laden des Durchschnitts:", error);
        }
    };

    // --- 2. DANN useEffect aufrufen ---
    // Beim Laden der Komponente: Durchschnittsbewertung vom Backend holen
    useEffect(() => {
        loadAverage();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [movie.id]);

    /**
     * Wird aufgerufen, wenn das ReviewForm abgesendet wird.
     */
    const handleReviewSubmit = async (reviewData) => {
        try {
            // 1. Bewertung an das Backend senden
            await addReview(reviewData);

            // 2. Durchschnitt neu berechnen und anzeigen
            // (Hier können wir die Funktion jetzt problemlos aufrufen)
            await loadAverage();

            // 3. Formular schließen und Bestätigung zeigen
            setShowReviewForm(false);
            alert("Vielen Dank für Ihre Bewertung!");
        } catch (error) {
            console.error("Fehler im Submit-Prozess:", error);
            throw error; // Weiterwerfen, damit das Formular den Fehler anzeigen kann
        }
    };

    return (
        <div style={{
            border: "1px solid #ddd",
            borderRadius: "8px",
            padding: "1.5rem",
            backgroundColor: "white",
            boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
            display: "flex",
            flexDirection: "column",
            gap: "0.5rem"
        }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "start" }}>
                <h3 style={{ margin: "0 0 0.5rem 0", color: "#333" }}>
                    {movie.title}
                </h3>

                {/* Anzeige der Durchschnittsbewertung */}
                <span style={{
                    background: "#f0f0f0",
                    padding: "4px 8px",
                    borderRadius: "4px",
                    fontSize: "0.9rem",
                    fontWeight: "bold",
                    color: "#333"
                }}>
          Note: {averageRating ? averageRating.toFixed(1) : "-"}
        </span>
            </div>

            <div style={{ fontSize: "0.9rem", color: "#666", marginBottom: "1rem" }}>
                {movie.releaseYear} | {movie.genre} | {movie.director}
            </div>

            <p style={{ fontSize: "0.95rem", lineHeight: "1.5", color: "#444", flex: 1 }}>
                {movie.description}
            </p>

            {/* Button zum Öffnen des Bewertungsformulars */}
            {!showReviewForm && (
                <button
                    onClick={() => setShowReviewForm(true)}
                    style={{
                        background: "#007bff",
                        color: "white",
                        padding: "0.5rem",
                        marginTop: "1rem",
                        alignSelf: "flex-start"
                    }}
                >
                    Bewerten
                </button>
            )}

            {/* Das Bewertungsformular (wird nur angezeigt, wenn showReviewForm true ist) */}
            {showReviewForm && (
                <ReviewForm
                    movieId={movie.id}
                    onReviewSubmit={handleReviewSubmit}
                    onCancel={() => setShowReviewForm(false)}
                />
            )}
        </div>
    );
};

export default MovieCard;
