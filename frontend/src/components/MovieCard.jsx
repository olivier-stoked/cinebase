import { useState, useEffect } from "react";
import ReviewForm from "./ReviewForm";
// WICHTIG: Hier 'getReviewsByMovie' neu importiert
import { addReview, getAverageRating, getReviewsByMovie } from "../services/review-service";

/**
 * Komponente zur Anzeige einer einzelnen Filmkarte.
 * Enthält jetzt auch die Logik zum Anzeigen des Durchschnitts und zum Bewerten.
 */
const MovieCard = ({ movie }) => {
    // ! FIX: Wir nutzen den Wert vom Backend als Startwert, falls vorhanden!
    // Vorher stand hier useState(0). Das hat dazu geführt, dass erst "0" angezeigt wurde,
    // bis der useEffect fertig geladen hatte.
    const [averageRating, setAverageRating] = useState(movie.averageRating || 0);

    const [showReviewForm, setShowReviewForm] = useState(false);

    // --- NEUE STATE VARIABLEN ---
    const [reviews, setReviews] = useState([]); // Liste der Kommentare
    const [showComments, setShowComments] = useState(false); // Toggle für Kommentare
    const [isLoadingReviews, setIsLoadingReviews] = useState(false);

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

    // --- NEU: Kommentare laden (nur bei Bedarf) ---
    const toggleComments = async () => {
        if (!showComments) {
            // Nur laden, wenn wir aufklappen
            setIsLoadingReviews(true);
            try {
                const loadedReviews = await getReviewsByMovie(movie.id);
                setReviews(loadedReviews);
            } catch (error) {
                console.error("Konnte Reviews nicht laden", error);
            } finally {
                setIsLoadingReviews(false);
            }
        }
        setShowComments(!showComments);
    };

    // ! Eine Hilfsfunktion renderStars erstellen, die aus einer Zahl (1-10) visuelle Sterne (⭐⭐⭐⭐⭐) macht.
    // ! Wir rechnen: 10 Punkte = 5 Sterne.
    // ! Einen Button "Kommentare anzeigen" einfügen.
    // ! Eine Liste der Kommentare rendern, die beim Klick geladen wird.

    const renderStars = (rating) => {
        // Prüfung auf null/undefined, aber 0 ist ein gültiger Wert
        if (rating === undefined || rating === null) return <span style={{ color: "#aaa" }}>Noch keine Bewertung</span>;

        const fullStars = Math.floor(rating / 2); // z.B. 8.5 -> 4 Sterne
        const hasHalfStar = (rating / 2) % 1 >= 0.5;
        const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

        return (
            <span style={{ color: "#FFD700", fontSize: "1.2rem", letterSpacing: "2px" }}>
                {"★".repeat(fullStars)}
                {hasHalfStar ? "½" : ""}
                {"☆".repeat(emptyStars)}
            </span>
        );
    };

    // --- 2. DANN useEffect aufrufen ---
    // Beim Laden der Komponente: Durchschnittsbewertung vom Backend holen
    useEffect(() => {
        // ! OPTIMIERUNG: Wenn der Wert schon vom Parent kommt (durch unser Backend-Update),
        // müssen wir nicht sofort neu laden. Das spart Requests.
        if (movie.averageRating !== undefined && movie.averageRating !== null) {
            setAverageRating(movie.averageRating);
        } else {
            loadAverage();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [movie.id, movie.averageRating]);

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

            // NEU: Falls Kommentare offen sind, Liste aktualisieren
            if (showComments) {
                const loadedReviews = await getReviewsByMovie(movie.id);
                setReviews(loadedReviews);
            }

            // 3. Formular schließen und Bestätigung zeigen
            setShowReviewForm(false);
            alert("Vielen Dank für Ihre Bewertung!");
        } catch (error) {
            console.error("Fehler im Submit-Prozess:", error);
            throw error; // Weiterwerfen, damit das Formular den Fehler anzeigen kann
        }
    };

    return (
        // ! ANPASSUNG: className="card" für Dark Mode statt inline styles mit weißem Hintergrund
        <div className="card" style={{ display: "flex", flexDirection: "column", gap: "0.5rem" }}>

            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "start" }}>
                <div>
                    {/* Farbe entfernt, damit es im Dark Mode weiss ist */}
                    <h3 style={{ margin: "0 0 0.5rem 0" }}>
                        {movie.title}
                    </h3>
                    {/* NEU: Sterne statt nur Text */}
                    <div>
                        {renderStars(averageRating)}
                        <span style={{fontSize: "0.8rem", color: "#aaa", marginLeft: "5px"}}>
                            ({averageRating ? averageRating.toFixed(1) : "-"})
                        </span>
                    </div>
                </div>
            </div>

            <div style={{ fontSize: "0.9rem", color: "#aaa", marginBottom: "1rem" }}>
                {movie.releaseYear} | {movie.genre} | {movie.director}
            </div>

            <p style={{ fontSize: "0.95rem", lineHeight: "1.5", color: "#ddd", flex: 1 }}>
                {movie.description}
            </p>

            {/* Buttons Container */}
            <div style={{ display: "flex", gap: "10px", marginTop: "1rem", flexWrap: "wrap" }}>

                {/* Button zum Öffnen des Bewertungsformulars */}
                {!showReviewForm && (
                    <button
                        onClick={() => setShowReviewForm(true)}
                        style={{
                            background: "#007bff",
                            color: "white",
                            padding: "0.5rem",
                            alignSelf: "flex-start",
                            flex: 1
                        }}
                    >
                        ★ Bewerten
                    </button>
                )}

                {/* NEU: Button für Kommentare */}
                <button
                    onClick={toggleComments}
                    style={{
                        background: "#444", // Dunklerer Button für Sekundäraktion
                        color: "white",
                        padding: "0.5rem",
                        flex: 1
                    }}
                >
                    {showComments ? "Kommentare verbergen" : "💬 Kommentare anzeigen"}
                </button>
            </div>

            {/* Das Bewertungsformular (wird nur angezeigt, wenn showReviewForm true ist) */}
            {showReviewForm && (
                <ReviewForm
                    movieId={movie.id}
                    onReviewSubmit={handleReviewSubmit}
                    onCancel={() => setShowReviewForm(false)}
                />
            )}

            {/* NEU: Kommentar-Liste (wird nur angezeigt, wenn showComments true ist) */}
            {showComments && (
                <div style={{ marginTop: "1rem", borderTop: "1px solid #444", paddingTop: "1rem" }}>
                    <h4 style={{ margin: "0 0 10px 0", fontSize: "1rem" }}>Nutzer-Meinungen:</h4>

                    {isLoadingReviews ? (
                        <div style={{ color: "#aaa" }}>Lade Kommentare...</div>
                    ) : reviews.length === 0 ? (
                        <div style={{ color: "#aaa", fontStyle: "italic" }}>Noch keine Kommentare vorhanden.</div>
                    ) : (
                        <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
                            {reviews.map(review => (
                                // ! Style angepasst für Dark Mode (Dunkelgrau #222 statt Weiß #f9f9f9)
                                <div key={review.id} style={{ background: "#222", padding: "10px", borderRadius: "4px" }}>
                                    <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "5px" }}>
                                        <span style={{ fontWeight: "bold", color: "#fff" }}>{review.username}</span>
                                        <span style={{ color: "#FFD700", fontWeight: "bold" }}>{review.rating}/10</span>
                                    </div>
                                    <p style={{ margin: 0, fontSize: "0.9rem", color: "#ccc" }}>
                                        {review.comment || "Kein Text"}
                                    </p>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default MovieCard;
