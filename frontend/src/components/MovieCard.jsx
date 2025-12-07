const MovieCard = ({ movie }) => {
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
                <h3 style={{ margin: "0 0 0.5rem 0", color: "#333" }}>{movie.title}</h3>
                <span style={{
                    background: "#f0f0f0",
                    padding: "4px 8px",
                    borderRadius: "4px",
                    fontSize: "0.8rem",
                    fontWeight: "bold"
                }}>
          ⭐ {movie.rating}
        </span>
            </div>

            <div style={{ fontSize: "0.9rem", color: "#666", marginBottom: "1rem" }}>
                {movie.releaseYear} • {movie.genre} • {movie.director}
            </div>

            <p style={{ fontSize: "0.95rem", lineHeight: "1.5", color: "#444", flex: 1 }}>
                {movie.description}
            </p>
        </div>
    );
};

export default MovieCard;