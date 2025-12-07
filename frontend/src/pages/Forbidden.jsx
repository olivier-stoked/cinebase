import { Link } from "react-router-dom";

const Forbidden = () => {
    return (
        <div style={{ textAlign: "center", marginTop: "50px" }}>
            <h1 style={{ fontSize: "3rem", marginBottom: "1rem" }}>🚫 403</h1>
            <h2>Zugriff verweigert</h2>
            <p style={{ color: "#666", marginBottom: "2rem" }}>
                Du hast leider nicht die nötigen Rechte, um diese Seite zu sehen.
            </p>

            <Link to="/" style={{ color: "#646cff", textDecoration: "none", fontWeight: "bold" }}>
                Zurück zur Startseite
            </Link>
        </div>
    );
};

export default Forbidden;