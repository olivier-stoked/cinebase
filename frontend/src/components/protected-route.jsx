import { Navigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

/**
 * ProtectedRoute Component
 * Schützt Routen vor unauthentifizierten Zugriffen.
 *
 * @param {Object} children - Die zu schützende Seite (z.B. <Movies />)
 * @param {string} requiredRole - Optional: Erforderliche Rolle (z.B. "ADMIN")
 */
const ProtectedRoute = ({ children, requiredRole }) => {
    const { isAuthenticated, user, isLoading } = useAuth();

    // 1. Warten, bis der Auth-Check fertig ist
    if (isLoading) {
        return (
            <div style={{ textAlign: "center", marginTop: "50px" }}>
                Lädt...
            </div>
        );
    }

    // 2. Check: Ist der User überhaupt eingeloggt?
    if (!isAuthenticated) {
        // Wenn nicht -> Redirect zum Login
        return <Navigate to="/login" replace />;
    }

    // 3. Check: Hat der User die nötige Rolle? (Nur wenn requiredRole gesetzt ist)
    if (requiredRole && user?.role !== requiredRole) {
        // Wenn falsche Rolle -> Redirect zur Forbidden Page
        return <Navigate to="/forbidden" replace />;
    }

    // 4. Alles ok -> Seite anzeigen
    return children;
};

export default ProtectedRoute;