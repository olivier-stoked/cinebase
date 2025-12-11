import { useState } from "react";

/**
 * Kleines Formular für die Bewertung eines Films.
 * Wird direkt in der MovieCard angezeigt.
 */
const ReviewForm = ({ movieId, onReviewSubmit, onCancel }) => {
    // Standard auf 10 (Exzellent) oder 5 (Durchschnitt) setzen
    const [rating, setRating] = useState(10);
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
                rating: parseInt(rating), // WICHTIG: Integer erzwingen
                comment: comment
            };

            // Callback an die Eltern-Komponente aufrufen
            await onReviewSubmit(reviewData);

            // Formular zurücksetzen
            setRating(10);
            setComment("");
        } catch (err) {
            // Fehlermeldung vom Backend anzeigen
            setError(err.response?.data?.error || "Fehler beim Bewerten.");
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        // Style angepasst für Dark Mode (Dunkelgrau #333 statt Weiß #f9f9f9)
        <div style={{ marginTop: "1rem", padding: "1rem", background: "#333", borderRadius: "8px", border: "1px solid #444" }}>
            <h4 style={{ margin: "0 0 10px 0", color: "#fff" }}>Film bewerten</h4>

            <form onSubmit={handleSubmit}>
                {/* Rating Auswahl 0-10 (Dropdown für klare Zuordnung) */}
                <div style={{ marginBottom: "10px" }}>
                    <label style={{ display: "block", marginBottom: "5px", color: "#ccc", fontSize: "0.9rem" }}>
                        Punktevergabe:
                    </label>
                    <select
                        className="form-input"
                        value={rating}
                        onChange={(e) => setRating(e.target.value)}
                        disabled={isSubmitting}
                        style={{ background: "#444", color: "white", border: "1px solid #555" }} // Dark Input
                    >
                        {/* Harmonisierte Skala: 1 Punkt = 1/2 Stern */}
                        <option value="10">10 Punkte (⭐⭐⭐⭐⭐ Exzellent)</option>
                        <option value="9"> 9 Punkte (⭐⭐⭐⭐½ Herausragend)</option>
                        <option value="8"> 8 Punkte (⭐⭐⭐⭐ Sehr gut)</option>
                        <option value="7"> 7 Punkte (⭐⭐⭐½ Gut)</option>
                        <option value="6"> 6 Punkte (⭐⭐⭐ Befriedigend)</option>
                        <option value="5"> 5 Punkte (⭐⭐½ Durchschnitt)</option>
                        <option value="4"> 4 Punkte (⭐⭐ Unterdurchschnittlich)</option>
                        <option value="3"> 3 Punkte (⭐½ Schlecht)</option>
                        <option value="2"> 2 Punkte (⭐ Sehr schlecht)</option>
                        <option value="1"> 1 Punkt  (½ Furchtbar)</option>
                        <option value="0"> 0 Punkte (Müll)</option>
                    </select>
                </div>

                {/* Kommentar Feld */}
                <div style={{ marginBottom: "10px" }}>
                    <label style={{ display: "block", marginBottom: "5px", color: "#ccc", fontSize: "0.9rem" }}>
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
                        style={{ background: "#444", color: "white", border: "1px solid #555" }} // Dark Input
                    />
                </div>

                {/* Error Anzeige */}
                {error && <div style={{ color: "#ff6b6b", fontSize: "0.9rem", marginBottom: "10px" }}>{error}</div>}

                {/* Buttons */}
                <div style={{ display: "flex", gap: "10px" }}>
                    <button
                        type="submit"
                        disabled={isSubmitting}
                        style={{ flex: 1, background: "#28a745", color: "white", border: "none", padding: "8px", cursor: "pointer" }}
                    >
                        {isSubmitting ? "Sende..." : "Speichern"}
                    </button>
                    <button
                        type="button"
                        onClick={onCancel}
                        disabled={isSubmitting}
                        style={{ background: "#6c757d", color: "white", border: "none", padding: "8px", cursor: "pointer" }}
                    >
                        Abbrechen
                    </button>
                </div>
            </form>
        </div>
    );
};

export default ReviewForm;
