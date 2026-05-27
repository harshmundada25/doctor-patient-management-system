# 🏥 Doctor & Patient Management System

A modern full-stack hospital management app with a React (Vite + Tailwind) frontend and a Spring Boot backend. Includes JWT authentication, role-based access, seeded demo data, Swagger API docs, and a production-like API surface for appointments, prescriptions, and medical records.

---

## ✨ Highlights

- Secure JWT authentication (role-based: Admin / Doctor / Patient)
- Full CRUD APIs for doctors, patients, appointments, prescriptions, and medical records
- React + Vite frontend with reusable components and protected routes
- Spring Boot backend, Spring Security, and JPA (Hibernate)
- Seeded demo data for quick demos

---

## 🚀 Quick start

Prerequisites:
- Java 21
- Maven
- Node.js 18+ and npm
- (Optional) MySQL for persistent storage

1) Start the backend (uses in-memory H2 DB by default):

```powershell
cd backend
mvn spring-boot:run
```

2) Start the frontend (Vite dev server):

```powershell
cd frontend
npm install
$env:VITE_API_BASE_URL='http://localhost:8080/api'  # PowerShell example; place in frontend/.env for permanence
npm run dev
```

Open the UI: http://localhost:5173/

Open Swagger UI: http://localhost:8080/swagger-ui.html

---

## 🗂 Project layout (high level)

- `backend/` — Spring Boot app (controllers in `com.hospital.management.controller`, security in `com.hospital.management.security`).
- `frontend/` — React (Vite) app (pages in `src/pages`, API client at `src/api/client.js`).
- `postman/` — Postman collection for manual testing.

---

## ⚙️ Environment variables

Backend (for MySQL):
- `DB_URL` — JDBC URL (e.g. `jdbc:mysql://localhost:3306/doctor_patient_db?createDatabaseIfNotExist=true&useSSL=false`)
- `DB_USERNAME`
- `DB_PASSWORD`

Frontend (Vite):
- `VITE_API_BASE_URL` — e.g. `http://localhost:8080/api` (frontend falls back to `/api` if not set)

Security: JWT tokens are used for auth. Demo accounts are seeded on startup.

---

## 🔐 Demo accounts

- Admin: `admin@hospital.com` / `Admin@123`
- Doctor: `sarah.khan@hospital.com` / `Doctor@123`
- Patient: `john.mathews@hospital.com` / `Patient@123`

---

## 🧭 Important API endpoints (examples)

- `POST /api/auth/login` — login (returns JWT)
- `GET /api/auth/me` — profile
- `GET /api/dashboard/stats` — dashboard numbers
- `GET/POST/PUT/DELETE /api/doctors`
- `GET/POST/PUT/DELETE /api/patients`
- `GET/POST/PUT/PATCH /api/appointments`

Full list: `backend/src/main/java/com/hospital/management/controller` or via Swagger UI.

---

## 🧪 Testing with Postman

1. Import `postman/Doctor-Patient-Management-System.postman_collection.json`.
2. Set `baseUrl` to `http://localhost:8080/api` in the Postman environment.
3. `POST /api/auth/login` to obtain a token, then use Bearer auth for protected requests.

---

## 💡 Notes & tips

- Frontend stores JWT in `localStorage` under `hospital_token`; the shared axios client attaches it automatically.
- Backend CORS allows `localhost:5173` and `localhost:3000` for dev convenience (`SecurityConfig`).
- Seed data from `DataSeeder` helps you demo without manual setup.

---

## 🙋‍♂️ Want help pushing this to GitHub?

I can commit the README locally (done) and push — if you want me to push, run `gh auth login` to authenticate the CLI, then `git push origin main`. If you'd rather use SSH, add your public key to GitHub and push via SSH.

---

If you'd like a badge header, `CONTRIBUTING.md`, or `.env.example`, tell me which and I'll add them.
