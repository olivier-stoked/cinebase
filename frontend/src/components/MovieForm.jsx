import { useState } from "react";

// Standard-Werte ausserhalb der Komponente definiert, um Re-Renders zu vermeiden.
const DEFAULT_DATA = {
    title: "",
    description: "",
    genre: "",
    releaseYear: new Date().getFullYear(),
    director: "",
    rating: 5.0
};

/**
 * Formular zum Erstellen und Bearbeiten von Filmen.
 * * Quelle: Block 03B - Form Validation Basics
 * Wir nutzen lokalen State fuer die Formulardaten und Errors.
 * * @param {Function} onSubmit - Wird aufgerufen, wenn das Formular valide ist
 * @param {Object} initialData - Daten zum Bearbeiten (oder null fuer Neu)
 * @param {Function} onCancel - Callback fuer Abbrechen-Button
 */
const MovieForm = ({ onSubmit, initialData, onCancel }) => {

    // Quelle: React Basics - State Initialisierung
    // Wir initialisieren den State direkt. Wenn initialData vorhanden ist (Edit Mode),
    // nutzen wir diese Daten. Sonst die Defaults.
    // Durch den 'key'-Prop im Parent wird diese Komponente neu erstellt, wenn sich der Film aendert.
    const [formData, setFormData] = useState(initialData || DEFAULT_DATA);
    const [errors, setErrors] = useState({});

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    // Quelle: Block 03B - Validation Logic
    // Wir validieren vor dem Absenden. Fehler werden im State gespeichert.
    const validate = () => {
        const newErrors = {};
        if (!formData.title.trim()) newErrors.title = "Titel ist erforderlich";
        if (!formData.director.trim()) newErrors.director = "Regisseur ist erforderlich";
        if (!formData.genre.trim()) newErrors.genre = "Genre ist erforderlich";
        if (formData.releaseYear < 1888) newErrors.releaseYear = "Filme gibt es erst seit 1888";

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (validate()) {
            // Daten an Parent (MovieManager) geben
            onSubmit(formData);
        }
    };

    return (
        <div className="auth-container" style={{ maxWidth: "600px", marginBottom: "2rem" }}>
            <h3>{initialData ? "Film bearbeiten" : "Neuen Film hinzufügen"}</h3>

            <form onSubmit={handleSubmit}>

                {/* Titel Feld */}
                <div style={{ marginBottom: "1rem" }}>
                    <label>Titel *</label>
                    <input
                        className="form-input"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        placeholder="Titel des Films"
                    />
                    {/* Quelle: Block 03B - Fehleranzeige */}
                    {errors.title && <span className="error-message">{errors.title}</span>}
                </div>

                {/* Director & Genre */}
                <div style={{ display: "flex", gap: "1rem", marginBottom: "1rem" }}>
                    <div style={{ flex: 1 }}>
                        <label>Regisseur *</label>
                        <input
                            className="form-input"
                            name="director"
                            value={formData.director}
                            onChange={handleChange}
                        />
                        {errors.director && <span className="error-message">{errors.director}</span>}
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Genre *</label>
                        <input
                            className="form-input"
                            name="genre"
                            value={formData.genre}
                            onChange={handleChange}
                            placeholder="z.B. Sci-Fi"
                        />
                        {errors.genre && <span className="error-message">{errors.genre}</span>}
                    </div>
                </div>

                {/* Jahr & Rating */}
                <div style={{ display: "flex", gap: "1rem", marginBottom: "1rem" }}>
                    <div style={{ flex: 1 }}>
                        <label>Jahr</label>
                        <input
                            className="form-input"
                            type="number"
                            name="releaseYear"
                            value={formData.releaseYear}
                            onChange={handleChange}
                        />
                        {errors.releaseYear && <span className="error-message">{errors.releaseYear}</span>}
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Rating (0-10)</label>
                        <input
                            className="form-input"
                            type="number"
                            step="0.1"
                            name="rating"
                            value={formData.rating}
                            onChange={handleChange}
                        />
                    </div>
                </div>

                {/* Beschreibung */}
                <div style={{ marginBottom: "1rem" }}>
                    <label>Beschreibung</label>
                    <textarea
                        className="form-input"
                        name="description"
                        value={formData.description || ""}
                        onChange={handleChange}
                        rows="3"
                    />
                </div>

                {/* Buttons */}
                <div style={{ display: "flex", gap: "10px" }}>
                    <button type="submit" style={{ flex: 1 }}>
                        {initialData ? "Aenderungen speichern" : "Film speichern"}
                    </button>

                    {/* Abbrechen Button nur anzeigen wenn wir editieren */}
                    {initialData && (
                        <button
                            type="button"
                            onClick={onCancel}
                            style={{ background: "#6c757d", width: "auto" }}
                        >
                            Abbrechen
                        </button>
                    )}
                </div>

            </form>
        </div>
    );
};

export default MovieForm;
