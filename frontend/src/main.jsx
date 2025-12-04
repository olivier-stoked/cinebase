import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { AuthProvider } from './contexts/AuthContext.jsx'
import './index.css'
import App from './App.jsx'

createRoot(document.getElementById('root')).render(
    <StrictMode>
        {/* 1. AuthProvider: Stellt User-Daten global bereit */}
        <AuthProvider>
            {/* 2. BrowserRouter: Aktiviert das Routing (URLs) */}
            <BrowserRouter>
                <App />
            </BrowserRouter>
        </AuthProvider>
    </StrictMode>,
)
