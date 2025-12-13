# CINEBASE - Projektdokumentation

## 1. Einleitung & Projektbeschreibung
**CINEBASE** ist eine Multi-User-Webapplikation zur Verwaltung von Filmen und Rezensionen. Sie dient als Presseportal für ein Filmfestival. Das System unterscheidet zwischen Administratoren (Katalog-Verwaltung) und regulären Benutzern (Journalisten/Zuschauer), die Rezensionen verfassen können.

Das Projekt demonstriert eine vollständige **Fullstack-Architektur** mit sicherer Authentifizierung und strikter Trennung von Verantwortlichkeiten.




## 2. User Stories & Akzeptanzkriterien
Die Anforderungen wurden in Form von User Stories definiert.

### Story 1: Administrator (Katalog-Verwaltung)
*"Als Administrator möchte ich neue Filme erfassen, bearbeiten und löschen können, um den Festivalkatalog aktuell zu halten."*
* **Akzeptanzkriterium 1:** Der Admin kann über ein Formular Titel, Beschreibung und Erscheinungsdatum eingeben.
* **Akzeptanzkriterium 2:** Nur User mit der Rolle `ROLE_ADMIN` haben Zugriff auf diese Maske.
* **Code-Bezug:** Frontend `MovieManager.jsx` → Backend `MovieController` (`@PreAuthorize("hasRole('ADMIN')")`).

### Story 2: User (Filme ansehen)
*"Als Benutzer möchte ich eine Übersicht aller verfügbaren Filme sehen, um mich zu informieren."*
* **Akzeptanzkriterium 1:** Die Liste der Filme wird geladen, sobald ich eingeloggt bin.
* **Akzeptanzkriterium 2:** Ich sehe Titel und Durchschnittsbewertung auf einen Blick.
* **Code-Bezug:** Frontend `Movies.jsx` & `MovieCard.jsx` → Backend `GET /api/movies`.

### Story 3: User (Bewerten)
*"Als Benutzer möchte ich einen Film mit 1-10 Punkten und einem Kommentar bewerten, um meine Meinung zu teilen."*
* **Akzeptanzkriterium 1:** Ich kann pro Film nur **eine** Bewertung abgeben.
* **Akzeptanzkriterium 2:** Meine Bewertung wird sofort gespeichert und die Durchschnittsnote aktualisiert.
* **Code-Bezug:** Frontend `ReviewForm.jsx` → Backend `ReviewController`.



## 3. Backend-Architektur & Technologien

### Technologie-Stack

| Technologie | Version | Verwendung |
| :--- | :--- | :--- |
| **Java** | 21 | Backend-Programmiersprache |
| **Spring Boot** | 3.3.7 | Backend-Framework |
| **Spring Security** | 6.x | Authentifizierung & Autorisierung |
| **JWT (JJWT)** | 0.12.x | Token-basierte Sicherheit |
| **PostgreSQL** | 16 | Relationale Datenbank |
| **Hibernate / JPA** | 6.x | ORM (Datenbank-Zugriff) |
| **Maven** | 3.x | Build Management |
| **BCrypt** | - | Passwort-Hashing |

### 3.1 Schichten-Architektur (Layering)
Das Backend folgt strikt dem "Separation of Concerns" Prinzip:
* **Controller:** Nimmt REST-Requests entgegen und validiert DTOs (`@Valid`).
* **Service:** Beinhaltet die Geschäftslogik (z.B. "Darf User X diesen Film löschen?").
* **Repository:** Schnittstelle zur Datenbank (Spring Data JPA).

![Backend Layer Architektur](docs/img/architektur_backend.png)

### 3.2 Datenbank-Design (ER-Modell)
Das Datenmodell besteht aus drei Hauptentitäten. Die Beziehungen sind über Foreign Keys gesichert.
* **1:N Beziehung:** Ein User erstellt viele Reviews.
* **1:N Beziehung:** Ein Film hat viele Reviews.
* **AppUser:** Speichert Login-Daten (Passwort als BCrypt-Hash).

![ER Diagramm](docs/img/er_diagramm.png)



## 4. Sicherheitskonzept
Das Sicherheitskonzept basiert auf **Statelessness** und **Rollenbasierter Zugriffskontrolle (RBAC)**.

### 4.1 Authentifizierung (JWT Flow)
Wir verzichten auf Server-Sessions. Stattdessen authentifiziert sich der Client bei jedem Request mit einem **JSON Web Token (JWT)**.
1.  **Login:** User sendet Credentials. Server prüft Hash und signiert einen Token.
2.  **Zugriff:** Client sendet Token im `Authorization: Bearer`-Header.
3.  **Filter:** Der `JwtAuthenticationFilter` fängt jeden Request ab und validiert die Signatur.

![JWT Sequenzdiagramm](docs/img/jwt_sequenz.png)

### 4.2 Passwort-Sicherheit
Passwörter werden **niemals im Klartext** gespeichert. Wir nutzen **BCrypt** mit einem Stärkegrad von 10, um Passwörter vor dem Speichern in der Datenbank zu hashen (`BCryptPasswordEncoder`).



## 5. Frontend-Architektur

### Technologie-Stack

| Technologie | Version | Verwendung |
| :--- | :--- | :--- |
| **React** | 18 | UI-Library (Komponenten-basiert) |
| **Vite** | 5.x | Build-Tool & Schneller Dev-Server |
| **React Router** | 6.x | Client-Side Routing (Navigation) |
| **Axios** | 1.x | HTTP-Client für API-Calls |
| **Context API** | React 18 | Globales State-Management (Auth) |
| **Vitest** | 1.x | Unit-Testing Framework |
| **React Testing Library** | 14.x | Component Testing Utilities |

### 5.1 Component Hierarchie
Die App ist modular in Pages und wiederverwendbare Components unterteilt. Das Layout umschließt alle Routen.

![Component Tree](docs/img/component_tree.png)

### 5.2 State Management (AuthContext)
Um "Prop Drilling" zu vermeiden, wird der eingeloggte User global im `AuthContext` gespeichert. Komponenten wie die Navigation oder ProtectedRoutes konsumieren diesen State via `useAuth()`.

![Auth Context Flow](docs/img/state_management.png)

### 5.3 API-Integration
Alle HTTP-Aufrufe laufen über eine zentrale Axios-Instanz (`api-client.js`). Ein **Request-Interceptor** sorgt dafür, dass der JWT-Token automatisch in jeden Header injiziert wird.

![API Integration](docs/img/api_integration.png)



## 6. Installation & Setup

### Voraussetzungen
* JDK 21
* Node.js (v18+)
* PostgreSQL Datenbank

### Schritt 1: Datenbank
Erstelle eine Datenbank namens `cinebase_db` in PostgreSQL. Passe die Zugangsdaten in der `application.properties` an.

### Schritt 2: Backend starten
```bash
cd backend
./mvnw spring-boot:run
# Server läuft auf http://localhost:8080