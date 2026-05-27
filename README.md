# Doctor & Patient Management System

A full-stack hospital management system with a Spring Boot backend and a React frontend. The app includes JWT authentication, role-based access control, seeded demo data, Swagger API documentation, and CRUD workflows for core hospital operations.

## ✨ What This Project Does

- 👨‍⚕️ Manages doctors and patient profiles
- 📅 Creates, updates, and tracks appointments
- 💊 Handles prescriptions for patients
- 🩺 Stores and views medical records
- 📊 Shows dashboard statistics for a quick overview
- 🔐 Secures the app with JWT authentication and role-based access

## 🛠️ Tech Stack

### Backend

- Java 17
- Spring Boot 3
- Spring Security 6
- Spring Data JPA
- JWT Authentication
- Swagger/OpenAPI
- Maven
- H2 by default, with optional MySQL support

### Frontend

- React
- Vite
- Tailwind CSS
- Axios
- React Router
- Recharts
- React Hot Toast

## 🧩 How It Works

The app follows a clean client-server architecture:

1. The frontend sends requests from the browser.
2. The backend handles authentication, business logic, and validation.
3. The backend reads and writes data using JPA.
4. JWT is used to prove the user is logged in.
5. Role-based access controls what each user can view and do.

## 👥 User Roles

### Admin

- Can view the full dashboard
- Can manage doctors and patients
- Can view all appointments and prescriptions
- Can update or delete records

### Doctor

- Can view assigned patients
- Can manage appointments
- Can create prescriptions
- Can update appointment status

### Patient

- Can register and log in
- Can view own profile
- Can book and track appointments
- Can view own prescriptions and medical records

## 🔐 Authentication Flow

1. The user enters email and password.
2. The backend validates the credentials.
3. If valid, the backend returns a JWT token.
4. The frontend stores the token in local storage.
5. Protected API requests send the token in the Authorization header.
6. The backend checks the token before allowing access.

This is stateless authentication, so the server does not need session storage.

## 🗂️ Project Structure

- `backend/` - Spring Boot backend, controllers, services, repositories, entities, DTOs, and security
- `frontend/` - React frontend with pages, components, context, and API client
- `postman/` - Postman collection for testing the API

## 🚀 Getting Started

### Prerequisites

- Java 17
- Maven
- Node.js 18+ and npm
- Optional: MySQL for persistent storage

### Start the backend

From the `backend` folder:

```powershell
mvn spring-boot:run
```

The backend runs on `http://localhost:8080`.

Swagger UI is available at `http://localhost:8080/swagger-ui.html`.

### Start the frontend

From the `frontend` folder:

```powershell
npm install
$env:VITE_API_BASE_URL='http://localhost:8080/api'
npm run dev
```

The frontend runs on `http://localhost:5173`.

For a permanent API base URL, create `frontend/.env`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## ⚙️ Environment Variables

### Backend

- `DB_URL` - JDBC URL for MySQL
- `DB_USERNAME` - MySQL username
- `DB_PASSWORD` - MySQL password

### Frontend

- `VITE_API_BASE_URL` - Backend API base URL

If `VITE_API_BASE_URL` is not set, the frontend falls back to `/api`.

## 🎯 Core API Endpoints

### Authentication

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

### Dashboard

- `GET /api/dashboard/stats`

### Doctors

- `GET /api/doctors`
- `GET /api/doctors/{id}`
- `GET /api/doctors/me/patients`
- `POST /api/doctors`
- `PUT /api/doctors/{id}`
- `DELETE /api/doctors/{id}`

### Patients

- `GET /api/patients`
- `GET /api/patients/{id}`
- `GET /api/patients/me`
- `PUT /api/patients/me`
- `POST /api/patients`
- `PUT /api/patients/{id}`
- `DELETE /api/patients/{id}`

### Appointments

- `GET /api/appointments`
- `GET /api/appointments/my`
- `POST /api/appointments`
- `PUT /api/appointments/{id}`
- `PATCH /api/appointments/{id}/status`
- `DELETE /api/appointments/{id}`

### Prescriptions

- `GET /api/prescriptions`
- `GET /api/prescriptions/my`
- `POST /api/prescriptions`
- `DELETE /api/prescriptions/{id}`

### Medical Records

- `GET /api/medical-records/my`
- `POST /api/medical-records`
- `PUT /api/medical-records/{id}`
- `DELETE /api/medical-records/{id}`

## 🧪 Demo Accounts

These demo users are seeded on startup:

- Admin: `admin@hospital.com` / `Admin@123`
- Doctor: `sarah.khan@hospital.com` / `Doctor@123`
- Doctor: `michael.reed@hospital.com` / `Doctor@123`
- Doctor: `priya.nair@hospital.com` / `Doctor@123`
- Doctor: `daniel.ortiz@hospital.com` / `Doctor@123`
- Patient: `john.mathews@hospital.com` / `Patient@123`
- Patient: `aisha.verma@hospital.com` / `Patient@123`
- Patient: `lucas.brown@hospital.com` / `Patient@123`
- Patient: `sophia.patel@hospital.com` / `Patient@123`

## 📦 Postman Testing

1. Import `postman/Doctor-Patient-Management-System.postman_collection.json`.
2. Set `baseUrl` to `http://localhost:8080/api`.
3. Log in with `POST /api/auth/login` and reuse the JWT for protected requests.

## 🧠 Implementation Notes

- JWT tokens are stored in `localStorage` under `hospital_token`.
- The shared axios client attaches the token automatically.
- Backend CORS allows local frontend development.
- Demo data is inserted automatically on startup.

## ✅ Summary

This project is a secure, role-based healthcare management system with a React frontend and a Spring Boot backend. It centralizes hospital workflows in one app and is ready for demos, interviews, and local development.
