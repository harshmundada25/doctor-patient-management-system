# Doctor & Patient Management System

Enterprise full-stack healthcare management platform with a Spring Boot 3 backend and a React + Tailwind frontend.

## Tech Stack

- Backend: Spring Boot 3, Java 17, Spring Security 6, JWT, JPA, MySQL, Maven, Swagger/OpenAPI
- Frontend: React, Vite, Tailwind CSS, Axios, React Router, Recharts, Toast notifications
# Doctor & Patient Management System

Enterprise full-stack hospital management app with a React (Vite + Tailwind) frontend and a Spring Boot backend.

This README provides everything needed to run the project locally, test the API, and understand the architecture.

---

## Quick start (recommended)

Prerequisites:
- Java 21
- Maven
- Node.js 18+ and npm
- (Optional) MySQL for production testing

Start backend (defaults to H2 in-memory DB):

```powershell
cd backend
mvn spring-boot:run
```

Start frontend (Vite dev server):

```powershell
cd frontend
npm install
$env:VITE_API_BASE_URL='http://localhost:8080/api' # PowerShell example; use .env for permanent
npm run dev
```

Open the UI at: http://localhost:5173/

Open Swagger UI at: http://localhost:8080/swagger-ui.html

---

## Project structure (high level)

- `backend/` — Spring Boot application, controllers under `com.hospital.management.controller`, security under `com.hospital.management.security`.
- `frontend/` — React app (Vite), pages under `src/pages`, shared `src/api/client.js` contains the axios instance.
- `postman/` — Postman collection for manual API testing.

---

## Environment variables

Backend (set before running if you want MySQL):

- `DB_URL` — JDBC URL (example: `jdbc:mysql://localhost:3306/doctor_patient_db?createDatabaseIfNotExist=true&useSSL=false`)
- `DB_USERNAME`
- `DB_PASSWORD`

Frontend (Vite):
- `VITE_API_BASE_URL` — base API URL, e.g. `http://localhost:8080/api`. If not set, frontend uses `/api`.

Security: JWTs are used for authentication; demo credentials are seeded on startup.

---

## Demo accounts

- Admin: `admin@hospital.com` / `Admin@123`
- Doctor: `sarah.khan@hospital.com` / `Doctor@123`
- Patient: `john.mathews@hospital.com` / `Patient@123`

---

## Important endpoints (sample)

- `POST /api/auth/login` — login (returns JWT)
- `GET /api/auth/me` — fetch profile
- `GET /api/dashboard/stats` — dashboard stats
- `GET/POST/PUT/DELETE /api/doctors`
- `GET/POST/PUT/DELETE /api/patients`
- `GET/POST/PUT/PATCH /api/appointments`

Full list is available in the code under `backend/src/main/java/com/hospital/management/controller` and via Swagger.

---

## Testing with Postman

1. Import `postman/Doctor-Patient-Management-System.postman_collection.json`.
2. Set environment variable `baseUrl` to `http://localhost:8080/api`.
3. `POST /api/auth/login` to receive a token, then use Bearer token for protected requests.

---

## Notes & Tips

- The frontend stores the JWT in `localStorage` (`hospital_token`) and the axios client attaches it on each request.
- The backend config (`SecurityConfig`) permits CORS for common dev origins (`localhost:5173`, `localhost:3000`).
- Seed data is injected by `DataSeeder` on startup to make demos quick.

---

If you want, I can add a `frontend/.env.example`, a short CONTRIBUTING.md, or create a GitHub Release for the current commit.