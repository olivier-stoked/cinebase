import { useEffect, useState } from "react";
// Importiert den Service für Film-Daten.
import { getAllMovies } from "../services/movie-service";
// Importiert die Darstellungskomponente für einen einzelnen Film.
import MovieCard from "../components/MovieCard";

/**
 * Seite zur Anzeige des Filmkatalogs.
 * Lädt alle Filme vom Backend und stellt sie als Gitter (Grid) dar.
 * Quelle: Block 04A - Daten laden & Anzeigen
 */
const Movies = () => {
    const [movies, setMovies] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    // Laden der Daten beim Mounten der Komponente.
    useEffect(() => {
        loadMovies();
    }, []);

    const loadMovies = async () => {
        try {
            const data = await getAllMovies();
            setMovies(data);
        } catch (err) {
            // ! WICHTIG: Fehler in die Konsole loggen für Debugging (401, 500 etc.)
            console.error("Fehler beim Laden der Filme:", err);
            setError("Konnte Filme nicht laden. Möglicherweise sind Sie nicht eingeloggt.");
        } finally {
            setIsLoading(false);
        }
    };

    if (isLoading) return <div style={{ textAlign: "center", marginTop: "50px" }}>Lade Filme...</div>;

    if (error) return <div className="error-message" style={{ textAlign: "center", marginTop: "50px", color: "red" }}>{error}</div>;

    return (
        <div className="page-container">
            <h1 style={{ marginBottom: "2rem" }}>Filmkatalog</h1>

            <div style={{
                display: "grid",
                gridTemplateColumns: "repeat(auto-fill, minmax(350px, 1fr))", // Responsive Grid
                gap: "2rem"
            }}>
                {movies.map((movie) => (
                    <MovieCard key={movie.id} movie={movie} />
                ))}
            </div>

            {movies.length === 0 && (
                <p style={{ textAlign: "center", color: "#666" }}>Keine Filme gefunden.</p>
            )}
        </div>
    );
};

export default Movies;