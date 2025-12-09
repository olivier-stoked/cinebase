import { useState } from "react";

/**
 * Kleines Formular für die Bewertung eines Films.
 * Wird direkt in der MovieCard angezeigt.
 */
const ReviewForm = ({ movieId, onReviewSubmit, onCancel }) => {
    const [rating, setRating] = useState(5);
    const [comment, setComment] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError("");

        try {
            // Daten vorbereiten für das Backend (DTO Struktur)
            const reviewData = {
                movieId: movieId,
                rating: parseInt(rating),
                comment: comment
            };

            // Callback an die Eltern-Komponente aufrufen
            await onReviewSubmit(reviewData);

            // Formular zurücksetzen
            setRating(5);
            setComment("");
        } catch (err) {
            // Fehlermeldung vom Backend anzeigen (z.B. "Bereits bewertet")
            setError(err.response?.data?.error || "Fehler beim Bewerten.");
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div style={{ marginTop: "1rem", padding: "1rem", background: "#f9f9f9", borderRadius: "8px", border: "1px solid #eee" }}>
            <h4 style={{ margin: "0 0 10px 0", color: "#333" }}>Film bewerten</h4>

            <form onSubmit={handleSubmit}>
                {/* Rating Auswahl 1-10 */}
                <div style={{ marginBottom: "10px" }}>
                    <label style={{ display: "block", marginBottom: "5px", color: "#666", fontSize: "0.9rem" }}>
                        Punkte (1-10):
                    </label>
                    <select
                        className="form-input"
                        value={rating}
                        onChange={(e) => setRating(e.target.value)}
                        disabled={isSubmitting}
                    >
                        {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map(num => (
                            <option key={num} value={num}>{num}</option>
                        ))}
                    </select>
                </div>

                {/* Kommentar Feld */}
                <div style={{ marginBottom: "10px" }}>
                    <label style={{ display: "block", marginBottom: "5px", color: "#666", fontSize: "0.9rem" }}>
                        Kommentar (optional):
                    </label>
                    <textarea
                        className="form-input"
                        value={comment}
                        onChange={(e) => setComment(e.target.value)}
                        placeholder="Was denkst du über den Film?"
                        rows="2"
                        maxLength="500"
                        disabled={isSubmitting}
                    />
                </div>

                {/* Error Anzeige */}
                {error && <div style={{ color: "red", fontSize: "0.9rem", marginBottom: "10px" }}>{error}</div>}

                {/* Buttons */}
                <div style={{ display: "flex", gap: "10px" }}>
                    <button
                        type="submit"
                        disabled={isSubmitting}
                        style={{ flex: 1, background: "#28a745" }}
                    >
                        {isSubmitting ? "Sende..." : "Speichern"}
                    </button>
                    <button
                        type="button"
                        onClick={onCancel}
                        disabled={isSubmitting}
                        style={{ background: "#6c757d" }}
                    >
                        Abbrechen
                    </button>
                </div>
            </form>
        </div>
    );
};

export default ReviewForm;
