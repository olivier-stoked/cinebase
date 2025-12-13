import { Link } from "react-router-dom";

const PageNotFound = () => {
    return (
        <div style={{ textAlign: "center", marginTop: "50px" }}>
            <h1>404 - Seite nicht gefunden</h1>
            <p>Diese URL existiert nicht.</p>
            <Link to="/" style={{ color: "white", textDecoration: "underline" }}>
                Zurück zur Startseite
            </Link>
        </div>
    );
};

export default PageNotFound;