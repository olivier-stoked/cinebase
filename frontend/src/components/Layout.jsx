import { Outlet } from "react-router-dom";
import Navigation from "./Navigation";

const Layout = () => {
    return (
        <div>
            <Navigation />
            <main style={{ padding: "0 2rem", maxWidth: "1200px", margin: "0 auto" }}>
                {/* Hier werden die Child-Routes (Home, Login, etc.) gerendert */}
                <Outlet />
            </main>
        </div>
    );
};

export default Layout;
