# Doctor & Patient Management System

Enterprise full-stack healthcare management platform with a Spring Boot 3 backend and a React + Tailwind frontend.

## Tech Stack

- Backend: Spring Boot 3, Java 17, Spring Security 6, JWT, JPA, MySQL, Maven, Swagger/OpenAPI
- Frontend: React, Vite, Tailwind CSS, Axios, React Router, Recharts, Toast notifications

## Folder Structure

```text
doctor-patient-management-system/
  backend/
    pom.xml
    src/main/java/com/hospital/management/...
    src/main/resources/application.properties
    src/main/resources/sql/schema.sql
  frontend/
    package.json
    src/...
  postman/
    Doctor-Patient-Management-System.postman_collection.json
```

## Backend Setup

1. By default the backend starts against an in-memory H2 database so the app runs without local MySQL setup.
2. To use MySQL instead, set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` before starting the backend.
3. Run the backend from the `backend/` folder.

### Backend Run Command

```bash
mvn spring-boot:run
```

### Optional MySQL Configuration

Set these environment variables if you want to point the backend at your own MySQL instance:

- `DB_URL=jdbc:mysql://localhost:3306/doctor_patient_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
- `DB_USERNAME=root`
- `DB_PASSWORD=your_password`

### Default Demo Credentials

- Admin: `admin@hospital.com` / `Admin@123`
- Doctor: `sarah.khan@hospital.com` / `Doctor@123`
- Doctor: `michael.reed@hospital.com` / `Doctor@123`
- Doctor: `priya.nair@hospital.com` / `Doctor@123`
- Doctor: `daniel.ortiz@hospital.com` / `Doctor@123`
- Patient: `john.mathews@hospital.com` / `Patient@123`
- Patient: `aisha.verma@hospital.com` / `Patient@123`
- Patient: `lucas.brown@hospital.com` / `Patient@123`
- Patient: `sophia.patel@hospital.com` / `Patient@123`

## Frontend Setup

1. Install dependencies in the `frontend/` folder.
2. Set `VITE_API_BASE_URL` if your backend is not on `http://localhost:8080/api`.
3. Start the Vite dev server and open `http://localhost:5173/`.

### Frontend Run Command

```bash
npm install
npm run dev
```

## Database Schema

The schema is defined in `backend/src/main/resources/sql/schema.sql`.

Core tables:

- `users`
- `doctors`
- `patients`
- `appointments`
- `prescriptions`
- `medical_records`

## API Overview

### Authentication

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

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

### Dashboard

- `GET /api/dashboard/stats`

## Swagger

Open `http://localhost:8080/swagger-ui.html` after starting the backend.

## Frontend Preview

Run the frontend with Vite and open `http://localhost:5173/` to see the app.

## Postman Testing Steps

1. Import the collection from `postman/Doctor-Patient-Management-System.postman_collection.json`.
2. Create an environment variable `baseUrl` with value `http://localhost:8080/api`.
3. Login with the admin account and store the JWT token in `token`.
4. Use the bearer token for protected requests.
5. Test doctor, patient, appointment, prescription, and dashboard endpoints.

## Notes

- Sample seed data is inserted automatically on startup by `DataSeeder`.
- The frontend uses JWT session persistence, protected routes, dark mode, and role-based menus.
- The demo dataset now includes multiple doctors and patients so the tables and dashboards look fuller during trials.

## Completed Showcase Features

- Doctor availability fields in admin doctor management.
- Admin patient creation with assigned doctor selection.
- Patient profile editing from the profile page.
- Appointment edit, cancel, and status management flows.
- Shared table pagination and loading placeholders.
- Expanded dashboard analytics cards and recent-activity panels.