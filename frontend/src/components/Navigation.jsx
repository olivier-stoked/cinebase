import { Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

const Navigation = () => {
    const { isAuthenticated, user, logout } = useAuth();

    return (
        <nav style={{
            padding: "1rem 2rem",
            background: "#1a1a1a",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "2rem"
        }}>
            {/* Links */}
            <div style={{ display: "flex", gap: "20px" }}>
                <Link to="/" style={{ color: "white", textDecoration: "none", fontWeight: "bold" }}>CINEBASE</Link>
                <Link to="/" style={{ color: "#ccc", textDecoration: "none" }}>Home</Link>
                <Link to="/movies">Filme</Link>
                {!isAuthenticated && (
                    <Link to="/login" style={{ color: "#ccc", textDecoration: "none" }}>Login</Link>
                )}
            </div>

            {/* Rechts: User Info & Logout */}
            {isAuthenticated && (
                <div style={{ display: "flex", gap: "15px", alignItems: "center" }}>
          <span style={{ color: "#888", fontSize: "0.9rem" }}>
            Hallo, {user?.username}
          </span>
                    <button
                        onClick={logout}
                        style={{
                            padding: "5px 10px",
                            fontSize: "0.8rem",
                            background: "#c62828",
                            border: "none",
                            color: "white",
                            cursor: "pointer",
                            borderRadius: "4px"
                        }}
                    >
                        Logout
                    </button>
                </div>
            )}
        </nav>
    );
};

export default Navigation;
