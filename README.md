# 🏥 Doctor Appointment Booking System

A full-stack web application for booking doctor appointments, featuring a unique **slot-locking mechanism** to prevent double-booking — similar to how ticket-booking systems (BookMyShow, IRCTC) handle concurrent seat reservations.

🔗 **GitHub:** [github.com/Ramana-09/doctor-appointment-booking-system](https://github.com/Ramana-09/doctor-appointment-booking-system)

---

## 🌟 Key Feature: Race-Condition-Safe Slot Booking

Unlike typical CRUD-based appointment systems, this project implements a **two-phase booking flow**:

1. **Lock** — When a patient starts booking, the slot is temporarily locked (status: `LOCKED`) with a timestamp
2. **Confirm** — Patient confirms within 5 minutes to finalize the appointment
3. **Auto-Release** — A background scheduler (`@Scheduled`) runs every minute and automatically releases slots that were locked but never confirmed

This prevents two patients from booking the same slot at the same time.

---

## 🛠️ Tech Stack

**Backend**
- Java 21, Spring Boot
- Spring Security + JWT Authentication
- Spring Data JPA + Hibernate
- MySQL
- Maven

**Frontend**
- HTML5, CSS3, Vanilla JavaScript
- Fetch API for backend communication

---

## ✨ Features

- 🔐 **JWT Authentication** — Secure token-based login/register with BCrypt password hashing
- 👥 **Role-based access** — Separate flows for Patient, Doctor, and Admin
- 🗓️ **Doctor Management** — Doctors can add available time slots
- 🔒 **Slot Locking System** — Prevents double-booking with automatic timeout release
- 📋 **Appointment Management** — Patients can view, book, and cancel appointments
- ⚠️ **Global Exception Handling** — Clean, consistent API error responses
- 🌐 **CORS Enabled** — Frontend and backend communicate seamlessly across origins

---

## 📐 Architecture

```
Patient/Doctor → HTML/CSS/JS Frontend → REST API → Spring Boot Backend → MySQL
                                              ↓
                                  JWT Auth + Spring Security
                                              ↓
                        Scheduled Job (Slot Auto-Release every 60s)
```

### Database Schema (ER Overview)

```
User (id, name, email, password, role)
   │
   ├── Doctor (id, user_id FK, specialization, experience, clinicAddress)
   │        └── DoctorSlot (id, doctor_id FK, date, startTime, endTime, status, lockedAt)
   │
   └── Appointment (id, patient_id FK, doctor_slot_id FK, status, bookedAt, reasonForVisit)
```

**Slot status flow:** `AVAILABLE` → `LOCKED` → `BOOKED` (or back to `AVAILABLE` if lock expires)
**Appointment status:** `PENDING`, `CONFIRMED`, `CANCELLED`, `COMPLETED`

---

## 🔌 API Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|--------------|--------|
| POST | `/api/auth/register` | Register new user (Patient/Doctor) | Public |
| POST | `/api/auth/login` | Login and receive JWT token | Public |
| GET | `/api/doctors` | List all doctors | Public |
| GET | `/api/doctors/{id}/slots` | View available slots for a doctor | Public |
| POST | `/api/doctors/slots` | Add a new slot | Doctor only |
| POST | `/api/appointments/lock/{slotId}` | Lock a slot for booking | Patient |
| POST | `/api/appointments/confirm/{slotId}` | Confirm booking | Patient |
| GET | `/api/appointments/my` | View my appointments | Patient |
| PUT | `/api/appointments/{id}/cancel` | Cancel an appointment | Patient |

---

## 🚀 How to Run

### Prerequisites
- Java 21
- MySQL installed and running
- Maven (or use bundled `mvnw`)

### Backend Setup

```bash
# 1. Clone the repository
git clone https://github.com/Ramana-09/doctor-appointment-booking-system.git
cd doctor-appointment-booking-system

# 2. Create the database
mysql -u root -p
CREATE DATABASE doctor_appointment_db;
exit

# 3. Update database credentials in
# src/main/resources/application.properties
#   spring.datasource.username=your_username
#   spring.datasource.password=your_password

# 4. Build and run
mvn clean install -DskipTests
mvn spring-boot:run
```

Backend runs at `http://localhost:8080`

### Frontend Setup

```bash
cd frontend
```

Open `index.html` in a browser (or use VS Code's Live Server extension for a smoother experience).

### Testing the Flow

1. Register a **Doctor** account → Login → Add a time slot
2. Register a **Patient** account → Login → Browse doctors → View slots
3. Click **"Book This Slot"** → Confirm within 5 minutes → Appointment confirmed
4. Check **"My Appointments"** to view or cancel bookings

---

## 🎯 What I Learned

- Implementing JWT-based stateless authentication with Spring Security
- Handling race conditions in booking systems using status-based locking
- Building scheduled background jobs with `@Scheduled`
- Designing a clean REST API with proper DTOs and global exception handling
- Debugging CORS and Spring Security filter chain issues
- Connecting a vanilla JS frontend to a Spring Boot backend end-to-end

---

## 🔮 Future Improvements

- Add pessimistic database locking for stronger concurrency safety
- Email/SMS notifications for appointment confirmations
- Migrate frontend to React for better state management
- Add a doctor-specific "My Slots" view with edit/delete functionality
- Deploy backend (Render/Railway) and frontend (Netlify/Vercel) for a live demo link

---

## 👤 Author

**Ramana S**
📧 ramanaramya2004@gmail.com
🔗 [LinkedIn](https://www.linkedin.com/in/ramana-s-9s/) | [GitHub](https://github.com/Ramana-09)
