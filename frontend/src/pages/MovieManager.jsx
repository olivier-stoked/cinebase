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

    // Handler: Film löschen
    // Quelle: Block 04B - Delete Operation
    const handleDelete = async (id) => {
        if(!window.confirm("Diesen Film wirklich löschen?")) return;

        try {
            await deleteMovie(id);
            // Aus lokaler Liste entfernen
            setMovies(movies.filter(m => m.id !== id));

            // Falls wir den Film gerade bearbeiten, Edit-Modus beenden
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
                    // ! ANPASSUNG: Dark Mode (className="card") & User Rating Anzeige
                    <div key={movie.id} className="card" style={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center"
                        // WICHTIG: background: "white" entfernt fuer Dark Mode!
                    }}>
                        <div style={{ flex: 1 }}>
                            {/* Titel ohne festes 'black', damit er im Darkmode weiss ist
                            Design-Fix: Die fest codierten Farben (background: "white", color: "#333") entfernt und
                             durch die CSS-Klasse className="card" ersetzt. Damit passt es jetzt zum Dark Mode.*/}
                            <div style={{ fontSize: "1.1rem", fontWeight: "bold" }}>
                                {movie.title} <span style={{ color: "#aaa", fontWeight: "normal" }}>({movie.releaseYear})</span>
                            </div>
                            <div style={{ fontSize: "0.9rem", color: "#aaa", marginTop: "0.25rem" }}>
                                Regie: {movie.director} | Genre: {movie.genre} | Admin-Rating: {movie.rating}
                            </div>

                            {/* User-Rating (Durchschnitt) für den Admin sichtbar machen. Feature: Die Anzeige des
                             User-Ratings (Durchschnitt) hinzugefügt, damit der Admin sieht, wie der Film ankommt. */}
                            <div style={{ marginTop: "8px", fontSize: "0.9rem", color: "#FFD700", fontWeight: "bold" }}>
                                ★ User-Rating: {movie.averageRating ? movie.averageRating.toFixed(1) : "0.0"} / 10
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
