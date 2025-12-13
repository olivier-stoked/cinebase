import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
// Importiert den Router für die Navigation (URL-Handling).
import { BrowserRouter } from 'react-router-dom'
// Importiert den AuthProvider für den globalen User-State.
import { AuthProvider } from './contexts/AuthContext.jsx'
import './index.css'
import App from './App.jsx'

/**
 * Einstiegspunkt der React-Anwendung.
 * Hier wird die Komponenten-Hierarchie initialisiert.
 * Reihenfolge der Provider (Wichtig!):
 * 1. StrictMode: Prüft auf potenzielle Probleme im Code (nur Dev).
 * 2. AuthProvider: Stellt sicher, dass der User-Status überall verfügbar ist.
 * 3. BrowserRouter: Ermöglicht Routing innerhalb der App und Zugriff auf URL-Parameter.
 */
createRoot(document.getElementById('root')).render(
    <StrictMode>
        <AuthProvider>
            <BrowserRouter>
                <App />
            </BrowserRouter>
        </AuthProvider>
    </StrictMode>,
)