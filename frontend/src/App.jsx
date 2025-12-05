import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import Home from "./pages/Home";
import Login from "./pages/Login";

function App() {
    return (
        <Routes>
            {/* Das Layout umschließt alle Seiten */}
            <Route path="/" element={<Layout />}>

                {/* Startseite (/) */}
                <Route index element={<Home />} />

                {/* Login Seite (/login) */}
                <Route path="login" element={<Login />} />

                {/* Platzhalter für später: */}
                {/* <Route path="movies" element={<Movies />} /> */}

            </Route>
        </Routes>
    );
}

export default App;
