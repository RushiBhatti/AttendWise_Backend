# AttendWise – Smart Attendance Tracking System (Backend)

AttendWise is a robust and secure backend service designed for educational institutions to automate and manage attendance tracking. Built using Spring Boot and REST APIs, this system supports QR code-based attendance marking, role-based access control (RBAC), dynamic report generation in PDF format, email notifications, and much more.


---

## 🚀 Project Overview

- 🔐 **Authentication & Authorization**: Secure login and signup system using JWT, role-based authorization (`ADMIN` and `STUDENT` roles), and Spring Security with method-level protection.
- 📸 **QR Code Based Attendance**: Generate session-specific QR codes for seamless student attendance marking via scanning. Admin generates a secure QR code (JWT encoded session ID), Students scan to mark attendance (valid for 30 mins), System auto-marks absentees after expiry.
- 📩 **Email Notifications**: Admins can send absence notifications to students using scheduled CRON jobs and JavaMailSender.
- 📊 **PDF Report Generation**: Generate and download attendance reports in PDF format for sessions and individual users.
- 📦 **RESTful APIs**: Well-structured endpoints for managing users, sessions, reports, notifications, and attendance records.
- 🧠 **Exception Handling**: Global exception handler and consistent API response format using `@RestControllerAdvice`.

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security with JWT
- MySQL Database
- JavaMailSender
- ZXing (QR Code Generator)
- iText PDF (PDF Generation)
- CRON Scheduler
- Postman (for API Testing)

---

## 🧑‍💻 Getting Started

### Prerequisites

- Java 17+
- Maven (or Gradle if you're using it)
- MySQL
- An IDE (like IntelliJ IDEA, VS Code, etc.)



### Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/RushiBhatti/AttendWise_Backend.git
   cd AttendWise_Backend
   ```

2. **Create a MySQL Database**
   ```sql
   CREATE DATABASE attendwise_db;
   ```

3. **Configure `application.properties` Update the following values:**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/attendwise_db
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password

   jwt.secretKey=your_jwt_secretKey
   ```

4. **Create `application.yml`:**
   ```yml 
   spring:
      mail:
         host: smtp.gmail.com
         port: 587
         username: email@gmail.com
         password: xxxx xxxx xxxx xxxx (your password)
         properties:
            mail:
               smtp:
                  auth: true
                  starttls:
                    enable: true
   ```

5. **Build & Run the Application**
   ```bash
   mvn spring-boot:run
   ```

---

## 🙌 Acknowledgements

Built with passion to make attendance management smart, secure, and scalable for schools and colleges.

“Automate. Authenticate. Attend – with AttendWise.”
