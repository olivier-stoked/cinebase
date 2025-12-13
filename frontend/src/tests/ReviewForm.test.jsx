import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import ReviewForm from "./ReviewForm";

/**
 * Frontend Test 2: Interaktion & Formulare
 * Testet, ob das Bewertungsformular korrekt rendert und ob
 * die Buttons (Speichern/Abbrechen) die erwarteten Events auslösen.
 * Quelle: Block 06B - Component Interaction Testing
 */
describe('ReviewForm Component', () => {

    it('rendert das Formular korrekt', () => {
        render(<ReviewForm movieId={1} onReviewSubmit={() => {}} onCancel={() => {}} />);

        // Prüfen, ob der Button "Speichern" existiert
        expect(screen.getByRole('button', { name: /Speichern/i })).toBeInTheDocument();

        // Prüfen, ob das Textfeld für Kommentare existiert
        expect(screen.getByPlaceholderText(/Was denkst du über den Film?/i)).toBeInTheDocument();

        // Prüfen, ob das Dropdown vorhanden ist
        expect(screen.getByRole('combobox')).toBeInTheDocument();
    });

    it('ruft onCancel auf, wenn Abbrechen geklickt wird', () => {
        // 1. Arrange: Wir erstellen eine "Spion"-Funktion (Mock)
        const handleCancel = vi.fn();

        render(<ReviewForm movieId={1} onReviewSubmit={() => {}} onCancel={handleCancel} />);

        // 2. Act: Wir simulieren einen Klick auf "Abbrechen"
        const cancelButton = screen.getByRole('button', { name: /Abbrechen/i });
        fireEvent.click(cancelButton);

        // 3. Assert: Wurde die Funktion genau einmal aufgerufen?
        expect(handleCancel).toHaveBeenCalledTimes(1);
    });
});