import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'


// https://vite.dev/config/
// Test-Environment auf jsdom (simulierter Browser) setzen

export default defineConfig({
  plugins: [react()],

  // Für Tests
  test: {
    environment: 'jsdom',
    setupFiles: './src/setupTests.js',
    globals: true
  }
})