import { Navigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

/**
 * Wrapper-Komponente für geschützte Routen.
 * Prüft Authentifizierung und Rollenberechtigung.
 * Quelle: Block 04B - Protected Routes
 *
 * @param {Object} children - Die zu schützende Komponente (z.B. <AdminDashboard />).
 * @param {string} requiredRole - Optional: Erforderliche Rolle (z.B. "ADMIN").
 */
const ProtectedRoute = ({ children, requiredRole }) => {
    const { isAuthenticated, user, isLoading } = useAuth();

    // 1. Warten, bis der Auth-Status (Token-Check) initialisiert ist
    if (isLoading) {
        return (
            <div style={{ textAlign: "center", marginTop: "50px" }}>
                Lädt...
            </div>
        );
    }

    // 2. Check: Ist der User eingeloggt?
    if (!isAuthenticated) {
        // Redirect zum Login
        return <Navigate to="/login" replace />;
    }

    // 3. Check: Hat der User die nötige Rolle?
    if (requiredRole && user?.role !== requiredRole) {
        // Redirect zur Forbidden Page bei fehlenden Rechten
        return <Navigate to="/forbidden" replace />;
    }

    // 4. Zugriff gewährt
    return children;
};

export default ProtectedRoute;