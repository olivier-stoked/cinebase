import { Outlet } from "react-router-dom";
import Navigation from "./Navigation";

/**
 * Grundgerüst der Anwendung (Wrapper).
 * Enthält die Navigation und den Bereich für den wechselnden Inhalt (Outlet).
 * Quelle: Block 04B - Layout & Routing
 */
const Layout = () => {
    return (
        <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column" }}>
            <Navigation />

            {/* Hauptinhalt zentriert und mit maximaler Breite für gute Lesbarkeit */}
            <main style={{
                padding: "2rem",
                maxWidth: "1200px",
                margin: "0 auto",
                width: "100%",
                boxSizing: "border-box"
            }}>
                {/* Platzhalter für die Kind-Routen (Pages) */}
                <Outlet />
            </main>
        </div>
    );
};

export default Layout;