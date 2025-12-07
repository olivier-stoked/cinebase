import { useEffect, useState } from "react";
import { getAllMovies } from "../services/movie-service";
import MovieCard from "../components/MovieCard";

const Movies = () => {
    const [movies, setMovies] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        loadMovies();
    }, []);

    const loadMovies = async () => {
        try {
            const data = await getAllMovies();
            setMovies(data);
        } catch (err) {
            // WICHTIG: Fehler in die Konsole loggen!
            // 1. Löst das Linter-Problem (Variable 'err' wird benutzt)
            // 2. Hilft uns zu sehen, WARUM es nicht klappt (z.B. 401, 500, Network Error)
            console.error("Fehler beim Laden der Filme:", err);

            setError("Konnte Filme nicht laden. Bist du eingeloggt?");
        } finally {
            setIsLoading(false);
        }
    };

    if (isLoading) return <div style={{ textAlign: "center", marginTop: "50px" }}>Lade Filme...</div>;

    if (error) return <div className="error-message" style={{ textAlign: "center", marginTop: "50px" }}>{error}</div>;

    return (
        <div className="page-container">
            <h1 style={{ marginBottom: "2rem" }}>Filmkatalog</h1>

            <div style={{
                display: "grid",
                gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
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