import { Outlet } from "react-router-dom";
import Navigation from "./Navigation";

const Layout = () => {
    return (
        <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column" }}>
            <Navigation />

            {/* WICHTIG: maxWidth + margin: "0 auto" sorgt für die Zentrierung */}
            <main style={{
                padding: "2rem",
                maxWidth: "1200px",
                margin: "0 auto",
                width: "100%",
                boxSizing: "border-box"
            }}>
                <Outlet />
            </main>
        </div>
    );
};

export default Layout;
