import { useState, useEffect } from "react";
import ReviewForm from "./ReviewForm";
// WICHTIG: Hier 'getReviewsByMovie' neu importiert
import { addReview, getAverageRating, getReviewsByMovie } from "../services/review-service";
// ! NEU: Auth Hook importieren, um die Rolle zu prüfen (damit Admins nicht bewerten können)
import { useAuth } from "../contexts/AuthContext";

/**
 * Komponente zur Anzeige einer einzelnen Filmkarte.
 * Enthält jetzt auch die Logik zum Anzeigen des Durchschnitts und zum Bewerten.
 */
const MovieCard = ({ movie }) => {
    // ! NEU: Zugriff auf User-Rolle
    const { user } = useAuth();

    // ! FIX: Wir nutzen den Wert vom Backend als Startwert, falls vorhanden!
    const [averageRating, setAverageRating] = useState(movie.averageRating || 0);

    const [showReviewForm, setShowReviewForm] = useState(false);

    // --- NEUE STATE VARIABLEN ---
    const [reviews, setReviews] = useState([]);
    const [showComments, setShowComments] = useState(false);
    const [isLoadingReviews, setIsLoadingReviews] = useState(false);

    // --- 1. Funktion ZUERST definieren ---
    const loadAverage = async () => {
        try {
            const avg = await getAverageRating(movie.id);
            setAverageRating(avg);
        } catch (error) {
            console.error("Fehler beim Laden des Durchschnitts:", error);
        }
    };

    // --- Kommentare laden ---
    const toggleComments = async () => {
        if (!showComments) {
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

    // --- SVG Sterne ---
    // Quelle: MDN Web Docs - SVG Gradients
    // ! Konkret ausformulieren, was hier passiert
    const renderStars = (rating) => {
        if (rating === undefined || rating === null) return <span style={{ color: "#aaa" }}>-</span>;

        const stars = [];
        for (let i = 1; i <= 5; i++) {
            let fill = "none";
            if (rating >= i * 2) {
                fill = "100%";
            } else if (rating >= (i * 2) - 1) {
                fill = "50%";
            } else {
                fill = "0%";
            }

            stars.push(
                <svg key={i} width="20" height="20" viewBox="0 0 24 24" style={{ marginRight: "2px" }}>
                    <defs>
                        <linearGradient id={`grad-${movie.id}-${i}`}>
                            <stop offset={fill} stopColor="#FFD700" />
                            <stop offset={fill} stopColor="#555" />
                        </linearGradient>
                    </defs>
                    <path
                        fill={`url(#grad-${movie.id}-${i})`}
                        d="M12 .587l3.668 7.568 8.332 1.151-6.064 5.828 1.48 8.279-7.416-3.967-7.417 3.967 1.481-8.279-6.064-5.828 8.332-1.151z"
                    />
                </svg>
            );
        }
        return <div style={{ display: "flex", alignItems: "center" }}>{stars}</div>;
    };

    // --- 2. DANN useEffect aufrufen ---
    useEffect(() => {
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
            await addReview(reviewData);
            await loadAverage();
            if (showComments) {
                const loadedReviews = await getReviewsByMovie(movie.id);
                setReviews(loadedReviews);
            }
            setShowReviewForm(false);
            alert("Vielen Dank für Ihre Bewertung!");
        } catch (error) {
            console.error("Fehler im Submit-Prozess:", error);
            throw error;
        }
    };

    return (
        // ! ANPASSUNG: className="card" für Dark Mode
        <div className="card" style={{ display: "flex", flexDirection: "column", gap: "0.5rem" }}>

            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "start" }}>
                <div>
                    <h3 style={{ margin: "0 0 0.5rem 0" }}>
                        {movie.title}
                    </h3>

                    {/* --- NEU: Entflochtene Anzeige (Community vs. Jury) --- */}
                    <div style={{ display: "flex", alignItems: "center", flexWrap: "wrap", gap: "10px" }}>

                        {/* 1. Community Sterne */}
                        <div style={{ display: "flex", alignItems: "center" }}>
                            {renderStars(averageRating)}
                            <span style={{ fontSize: "0.9rem", color: "#FFD700", marginLeft: "5px", fontWeight: "bold" }}>
                                {averageRating ? averageRating.toFixed(1) : "-"}
                            </span>
                        </div>

                        <span style={{ color: "#444" }}>|</span>

                        {/* 2. Jury Referenz */}
                        <span style={{ fontSize: "0.8rem", color: "#888", fontStyle: "italic" }}>
                            Jury: {movie.rating}
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

                {/* ! LOGIK: Admin darf NICHT bewerten (Jury entscheidet beim Anlegen) */}
                {!showReviewForm && user && user.role !== 'ADMIN' && (
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

                <button
                    onClick={toggleComments}
                    style={{
                        background: "#444",
                        color: "white",
                        padding: "0.5rem",
                        flex: 1
                    }}
                >
                    {showComments ? "Kommentare verbergen" : "💬 Kommentare anzeigen"}
                </button>
            </div>

            {/* Das Bewertungsformular */}
            {showReviewForm && (
                <ReviewForm
                    movieId={movie.id}
                    onReviewSubmit={handleReviewSubmit}
                    onCancel={() => setShowReviewForm(false)}
                />
            )}

            {/* Kommentar-Liste */}
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
