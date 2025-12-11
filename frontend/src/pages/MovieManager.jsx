import { useEffect, useState } from "react";
// Services importieren
import { getAllMovies, createMovie, updateMovie, deleteMovie } from "../services/movie-service";
import MovieForm from "../components/MovieForm";

/**
 * Admin Dashboard zur Verwaltung von Filmen.
 * * Quelle: Block 04A/B - QuestionManager Analogie
 * Wir laden die Liste beim Start und verwalten den lokalen State fuer
 * optimistische UI-Updates (Liste aktualisieren ohne Reload).
 */
const MovieManager = () => {
    const [movies, setMovies] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    // State fuer den Film, der gerade bearbeitet wird (null = Create Modus)
    const [editingMovie, setEditingMovie] = useState(null);

    // Quelle: React Hooks - Daten laden beim Mounten
    useEffect(() => {
        loadMovies();
    }, []);

    const loadMovies = async () => {
        try {
            const data = await getAllMovies();
            setMovies(data);
        } catch (err) {
            console.error("Fehler beim Laden:", err);
            setError("Konnte Filme nicht laden.");
        } finally {
            setIsLoading(false);
        }
    };

    /**
     * Zentraler Handler fuer Formular-Submit (Create UND Update)
     * Quelle: Block 04A - Integration Backend
     */
    const handleFormSubmit = async (movieData) => {
        try {
            if (editingMovie) {
                // --- UPDATE FALL ---
                const updated = await updateMovie(editingMovie.id, movieData);
                setMovies(movies.map(m => m.id === editingMovie.id ? updated : m));
                setEditingMovie(null);
                alert("Film erfolgreich aktualisiert!");
            } else {
                // --- CREATE FALL ---
                const created = await createMovie(movieData);
                setMovies([...movies, created]);
                alert("Film erfolgreich erstellt!");
            }
        } catch (err) {
            console.error("Fehler beim Speichern:", err);
            alert("Fehler beim Speichern: " + err.message);
        }
    };

    const handleEditClick = (movie) => {
        setEditingMovie(movie);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const handleCancelEdit = () => {
        setEditingMovie(null);
    };

    const handleDelete = async (id) => {
        if(!window.confirm("Diesen Film wirklich löschen?")) return;

        try {
            await deleteMovie(id);
            setMovies(movies.filter(m => m.id !== id));
            if (editingMovie?.id === id) {
                setEditingMovie(null);
            }
        } catch (err) {
            console.error("Fehler beim Löschen:", err);
            alert("Fehler beim Löschen.");
        }
    };

    if (isLoading) return <div style={{textAlign: "center", marginTop: "50px"}}>Lade Admin Panel...</div>;

    return (
        <div className="page-container">
            <h1>Film Verwaltung (Admin)</h1>

            {error && (
                <div style={{ color: "red", marginBottom: "1rem", padding: "10px", border: "1px solid red" }}>
                    {error}
                </div>
            )}

            <MovieForm
                key={editingMovie ? editingMovie.id : 'create'}
                onSubmit={handleFormSubmit}
                initialData={editingMovie}
                onCancel={handleCancelEdit}
            />

            <hr style={{ margin: "2rem 0", opacity: 0.2 }} />

            <h2>Vorhandene Filme ({movies.length})</h2>

            <div style={{ display: "grid", gap: "1rem" }}>
                {movies.map(movie => (
                    // ! ANPASSUNG: Dark Mode (className="card")
                    <div key={movie.id} className="card" style={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center"
                    }}>
                        <div style={{ flex: 1 }}>
                            {/* Titel */}
                            <div style={{ fontSize: "1.1rem", fontWeight: "bold" }}>
                                {movie.title} <span style={{ color: "#aaa", fontWeight: "normal" }}>({movie.releaseYear})</span>
                            </div>

                            {/* Metadata (Regie, Genre) */}
                            <div style={{ fontSize: "0.9rem", color: "#aaa", marginTop: "0.25rem" }}>
                                Regie: {movie.director} | Genre: {movie.genre}
                            </div>

                            {/* --- NEU: Entflochtene Bewertung (Jury vs. Publikum) --- */}
                            <div style={{ display: "flex", gap: "15px", marginTop: "8px", fontSize: "0.9rem" }}>

                                {/* 1. Jury Wert */}
                                <span style={{ color: "#888" }}>
                                   ⚖️ Jury-Rating: <strong>{movie.rating}</strong>
                                </span>

                                {/* 2. Community Wert */}
                                <span style={{ color: "#FFD700", fontWeight: "bold" }}>
                                   ★ Publikum: {movie.averageRating ? movie.averageRating.toFixed(1) : "-"}
                                </span>

                            </div>
                            {/* ------------------------------------------------------- */}
                        </div>

                        <div style={{ display: "flex", gap: "10px" }}>
                            <button
                                onClick={() => handleEditClick(movie)}
                                style={{ background: "#007bff", color: "white", padding: "0.5rem 1rem" }}
                            >
                                Bearbeiten
                            </button>

                            <button
                                onClick={() => handleDelete(movie.id)}
                                style={{ background: "#dc3545", color: "white", padding: "0.5rem 1rem" }}
                            >
                                Löschen
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MovieManager;
