import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Movies from "./pages/Movies"; // ← NEU: Import für die Film-Seite
import ProtectedRoute from "./components/protected-route"; // ← NEU: Import für den Schutz
import Forbidden from "./pages/Forbidden"; // Forbidden Import

function App() {
    return (
        <Routes>
            {/* Das Layout umschließt alle Seiten */}
            <Route path="/" element={<Layout />}>

                {/* Startseite (/) */}
                <Route index element={<Home />} />

                {/* Login Seite (/login) */}
                <Route path="login" element={<Login />} />

                <Route path="login" element={<Login />} />
                {/* NEU: Forbidden Route */}
                <Route path="forbidden" element={<Forbidden />} />

                {/* Geschützte Route: Filme anzeigen (Für alle eingeloggten User) */}
                <Route
                    path="movies"
                    element={
                        <ProtectedRoute>
                            <Movies />
                        </ProtectedRoute>
                    }
                />

            </Route>
        </Routes>
    );
}

export default App;