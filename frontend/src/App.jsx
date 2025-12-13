import { Routes, Route } from "react-router-dom";
// Importiert das globale Layout (Header/Footer Wrapper).
import Layout from "./components/Layout";
// Importiert die Schutz-Logik für Routen.
import ProtectedRoute from "./components/protected-route.jsx";

// Importiert die Seiten-Komponenten.
import Home from "./pages/Home";
import Login from "./pages/Login";
import Movies from "./pages/Movies";
import MovieManager from "./pages/MovieManager";
// MovieDetail entfernt, da nicht vorhanden
import Forbidden from "./pages/Forbidden";
import PageNotFound from "./pages/PageNotFound";

/**
 * Hauptkomponente der Anwendung.
 * Definiert die Routing-Struktur und die Zugriffskontrolle.
 * Quelle: Block 04B - Routing & Protected Routes
 */
function App() {
    return (
        <Routes>
            {/* Das Layout umschließt alle untergeordneten Routen (Outlet) */}
            <Route path="/" element={<Layout />}>

                {/* Öffentliche Routen */}
                <Route index element={<Home />} />
                <Route path="login" element={<Login />} />
                <Route path="forbidden" element={<Forbidden />} />

                {/* Geschützte Route: Filmkatalog */}
                {/* Zugriff: Jeder eingeloggte User (Journalist & Admin) */}
                <Route
                    path="movies"
                    element={
                        <ProtectedRoute>
                            <Movies />
                        </ProtectedRoute>
                    }
                />

                {/* Detail-Route entfernt, damit der Build ohne extra Datei läuft */}

                {/* Admin Route: Verwaltung */}
                {/* Zugriff: Nur Benutzer mit der Rolle 'ADMIN' */}
                <Route
                    path="admin"
                    element={
                        <ProtectedRoute requiredRole="ADMIN">
                            <MovieManager />
                        </ProtectedRoute>
                    }
                />

                {/* Fallback für unbekannte URLs (404) */}
                <Route path="*" element={<PageNotFound />} />

            </Route>
        </Routes>
    );
}

export default App;