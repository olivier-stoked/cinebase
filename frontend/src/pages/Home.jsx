/**
 * Startseite der Anwendung.
 * Dient als Einstiegspunkt und Landing Page.
 */
const Home = () => {
    return (
        <div style={{ textAlign: "center", marginTop: "50px" }}>
            <h1>Willkommen zu CINEBASE</h1>
            <p style={{ fontSize: "1.2rem", color: "#888" }}>
                Das Festival Presseportal
            </p>
            <div style={{ marginTop: "2rem" }}>
                <p>Bitte loggen Sie sich ein, um auf den Filmkatalog zuzugreifen.</p>
            </div>
        </div>
    );
};

export default Home;