import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import MovieCard from "../components/MovieCard";


// ! Frontend Test 1: Rendering
// ! Wir testen, ob die MovieCard die Daten (Titel, Jahr) korrekt anzeigt. Herausforderung: Die Karte ruft im useEffect den
// !review-service auf. Wir müssen diesen Service mocken (simulieren), sonst schlägt der Test fehl, weil kein Backend da ist.

// WICHTIG: Wir mocken den Service, damit kein echter API-Aufruf passiert
// Quelle: Block 06B - Frontend Testing & Mocking
vi.mock('../services/review-service', () => ({
    getAverageRating: vi.fn(() => Promise.resolve(8.5)), // Simuliert: Durchschnitt ist 8.5
    addReview: vi.fn()
}));

describe('MovieCard Component', () => {

    const dummyMovie = {
        id: 1,
        title: 'Test Blockbuster',
        description: 'Ein sehr spannender Film.',
        releaseYear: 2024,
        director: 'Steven Spielberg',
        genre: 'Action',
        rating: 9.0
    };

    it('rendert den Filmtitel und Details korrekt', async () => {
        // 1. Arrange & Act: Komponente rendern
        render(<MovieCard movie={dummyMovie} />);

        // 2. Assert: Prüfen, ob der Titel im Dokument ist
        expect(screen.getByText('Test Blockbuster')).toBeInTheDocument();

        // Prüfen, ob Regisseur und Genre angezeigt werden
        // Wir nutzen 'getByText', das auch Teil-Strings findet, wenn man Regex nutzt oder den genauen String
        expect(screen.getByText(/Steven Spielberg/)).toBeInTheDocument();
        expect(screen.getByText(/Action/)).toBeInTheDocument();
    });

    it('zeigt die geladene Durchschnittsbewertung an', async () => {
        render(<MovieCard movie={dummyMovie} />);

        // 3. Assert (Asynchron): Warten, bis der useEffect durchgelaufen ist und "8.5" anzeigt
        // "Note:" ist der Text, den wir im Badge haben
        await waitFor(() => {
            expect(screen.getByText(/Note: 8.5/)).toBeInTheDocument();
        });
    });
});

