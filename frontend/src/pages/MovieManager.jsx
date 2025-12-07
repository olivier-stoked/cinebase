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
                // Wir rufen PUT auf dem Backend auf
                const updated = await updateMovie(editingMovie.id, movieData);

                // Lokalen State aktualisieren: Alten Film durch neuen ersetzen
                setMovies(movies.map(m => m.id === editingMovie.id ? updated : m));

                // Edit-Modus beenden
                setEditingMovie(null);
                alert("Film erfolgreich aktualisiert!");
            } else {
                // --- CREATE FALL ---
                // Wir rufen POST auf dem Backend auf
                const created = await createMovie(movieData);

                // Lokalen State aktualisieren: Neuen Film anhaengen
                setMovies([...movies, created]);
                alert("Film erfolgreich erstellt!");
            }
        } catch (err) {
            console.error("Fehler beim Speichern:", err);
            alert("Fehler beim Speichern: " + err.message);
        }
    };

    // Handler: Klick auf "Bearbeiten"
    const handleEditClick = (movie) => {
        setEditingMovie(movie);
        // Nach oben scrollen fuer bessere UX
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    // Handler: Klick auf "Abbrechen"
    const handleCancelEdit = () => {
        setEditingMovie(null);
    };

    // Handler: Film loeschen
    // Quelle: Block 04B - Delete Operation
    const handleDelete = async (id) => {
        if(!window.confirm("Diesen Film wirklich loeschen?")) return;

        try {
            await deleteMovie(id);
            // Aus lokaler Liste entfernen
            setMovies(movies.filter(m => m.id !== id));

            // Falls wir den Film gerade bearbeiten, Edit-Modus beenden
            if (editingMovie?.id === id) {
                setEditingMovie(null);
            }
        } catch (err) {
            console.error("Fehler beim Loeschen:", err);
            alert("Fehler beim Loeschen.");
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

            {/* WICHTIG: Key-Prop Pattern
         Wir nutzen 'key', um die Komponente neu zu rendern, wenn sich der Modus aendert.
         - 'create': Leeres Formular
         - editingMovie.id: Vorausgefuelltes Formular
         Dies vermeidet Probleme mit veraltetem State im Formular.
      */}
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
                    <div key={movie.id} style={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                        padding: "1rem",
                        background: "white",
                        borderRadius: "8px",
                        border: "1px solid #eee"
                    }}>
                        <div style={{ flex: 1 }}>
                            {/* Titel explizit dunkel gefaerbt, damit er auf weissem Grund sichtbar ist */}
                            <div style={{ fontSize: "1.1rem", fontWeight: "bold", color: "#333" }}>
                                {movie.title} <span style={{ color: "#888", fontWeight: "normal" }}>({movie.releaseYear})</span>
                            </div>
                            <div style={{ fontSize: "0.9rem", color: "#666", marginTop: "0.25rem" }}>
                                Regie: {movie.director} | Genre: {movie.genre} | Rating: {movie.rating}
                            </div>
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
                                Loeschen
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MovieManager;