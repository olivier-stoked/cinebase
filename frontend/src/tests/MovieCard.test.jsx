import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import MovieCard from "./MovieCard";

/**
 * Frontend Test 1: Rendering & Async Data
 * Testet, ob die MovieCard die statischen Props korrekt anzeigt und
 * ob die asynchronen Daten (Durchschnitts-Rating) nachgeladen werden.
 * Herausforderung: Der useEffect in der Komponente ruft den review-service auf.
 * Lösung: Wir mocken diesen Service, um keine echte Netzwerkverbindung zu benötigen.
 *
 * Quelle: Block 06B - Frontend Testing & Mocking
 */

// Mockt den Service, damit kein echter API-Aufruf passiert.
vi.mock('../services/review-service', () => ({
    getAverageRating: vi.fn(() => Promise.resolve(8.5)), // Simuliert: Durchschnitt ist 8.5
    addReview: vi.fn(),
    getReviewsByMovie: vi.fn(() => Promise.resolve([]))
}));

// Mockt den AuthContext, da MovieCard darauf zugreift (useAuth).
vi.mock('../contexts/AuthContext', () => ({
    useAuth: () => ({
        user: { role: 'USER' }, // Simuliert einen eingeloggten User
        isAuthenticated: true
    })
}));

describe('MovieCard Component', () => {

    const dummyMovie = {
        id: 1,
        title: 'Test Blockbuster',
        description: 'Ein sehr spannender Film.',
        releaseYear: 2024,
        director: 'Steven Spielberg',
        genre: 'Action',
        rating: 9.0 // Jury Rating
    };

    it('rendert den Filmtitel und Details korrekt', () => {
        // 1. Arrange & Act: Komponente rendern
        render(<MovieCard movie={dummyMovie} />);

        // 2. Assert: Prüfen, ob der Titel im Dokument ist
        expect(screen.getByText('Test Blockbuster')).toBeInTheDocument();

        // Prüfen, ob Regisseur und Genre angezeigt werden (Regex für Teilübereinstimmung)
        expect(screen.getByText(/Steven Spielberg/)).toBeInTheDocument();
        expect(screen.getByText(/Action/)).toBeInTheDocument();

        // Prüfen, ob das Jury-Rating angezeigt wird
        expect(screen.getByText(/9/)).toBeInTheDocument();
    });

    it('zeigt die geladene Durchschnittsbewertung an', async () => {
        render(<MovieCard movie={dummyMovie} />);

        // 3. Assert (Asynchron): Warten, bis der useEffect durchgelaufen ist.
        // Wir erwarten, dass der gemockte Wert "8.5" im DOM erscheint.
        await waitFor(() => {
            expect(screen.getByText("8.5")).toBeInTheDocument();
        });
    });
});