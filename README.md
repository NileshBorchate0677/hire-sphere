# 🌐 HireSphere - Enterprise Job Portal Backend

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.14-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-22-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Spring Security](https://img.shields.io/badge/Spring_Security-JWT_HS512-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![Cloudinary](https://img.shields.io/badge/Cloudinary-CDN_Storage-3448C5?style=for-the-badge&logo=cloudinary&logoColor=white)](https://cloudinary.com/)

> **HireSphere** is a high-performance, enterprise-grade Job Portal REST API platform built with Spring Boot, Spring Security, and MySQL. It seamlessly connects Job Seekers with Recruiters through end-to-end recruitment pipelines, rich candidate dossiers, resume uploads, application tracking, and analytics.

---

## 🚀 Key Modules & Capabilities

### 🔐 1. Authentication & Role-Based Security
- **Stateless JWT (HS512):** Secure token generation, verification, and automated renewal.
- **Roles:** `JOB_SEEKER`, `RECRUITER`, `ADMIN`.
- **Security Features:** Multi-device session revocation, forgot/reset password with Gmail SMTP tokens, name updates, account deactivation.
- **CORS Support:** Configured for cross-origin integration with Vite / React frontends.

### 👤 2. Job Seeker Profile & Comprehensive Portfolio
- **Candidate Profiles:** Full headline, experience years, skills, salary details, location preferences.
- **Education Management:** Comprehensive degree, college, CGPA/percentage, passing year records.
- **Experience Timeline:** Work history with designation, responsibilities, tech stacks, and current job markers.
- **Portfolio Projects:** Projects with GitHub repositories, live demo links, and descriptions.
- **Career & Personal Details:** Naukri-style career preferences (shift, industry, work mode) and personal details.

### 📄 3. High-Speed Resume Engine
- **Cloudinary Storage (Production):** Direct CDN upload with timeout protection.
- **Local Disk Fallback (Development):** Automatic local storage with path-traversal prevention.
- **Instant Browser Viewing:** Streaming endpoint with `Content-Length` and HTTP caching to prevent browser PDF hangs.
- **Historical Snapshot Safety:** Previous job applications retain exact resume snapshots without broken links.

### 🏢 4. Recruiter & Job Management Pipeline
- **Recruiter Profile:** Company details, industry, website, company size, contact details.
- **Job Creation & Posting:** Full job specification (salary ranges, required skills, workplace mode, deadlines).
- **Candidate Discovery & Search:** Dynamic JPA Criteria specifications with pagination, multi-attribute filtering, and keyword search.

### 📬 5. Job Application & Candidate Dossier Lifecycle
- **Application Submission:** Duplicate application protection and cover letter support.
- **Recruiter Review Pipeline:** Multi-stage workflow (`APPLIED` ➔ `SHORTLISTED` ➔ `ACCEPTED` / `REJECTED` / `HIRED`).
- **Recruiter Dossier View:** Complete 360-degree view of candidate experience, education, projects, and contact info.
- **Application Withdrawal:** Candidate self-service withdrawal mechanism.

### 🔔 6. Notifications & Analytics
- **Recruiter Notifications:** Instant alerts when candidates apply to jobs.
- **Candidate Notifications:** Real-time status updates when applications are shortlisted or reviewed.
- **Recruiter Analytics:** Aggregated job performance, application volume counters, and pipeline distribution.

---

## 🛠️ Technology Stack

| Layer | Technologies |
| :--- | :--- |
| **Backend Framework** | Spring Boot 3.5.14, Spring MVC, Spring Data JPA |
| **Language & Runtime** | Java 22 |
| **Security & Auth** | Spring Security 6, JJWT (io.jsonwebtoken 0.12.6) |
| **Database** | MySQL 8.0, HikariCP Connection Pool |
| **ORM & Persistence** | Hibernate 6.6, JPA Specifications, JPQL |
| **Cloud Storage** | Cloudinary Java SDK (with offline local filesystem fallback) |
| **Email Service** | Spring Mail (Gmail SMTP / TLS) |
| **Build & Tooling** | Maven, Lombok |

---

## 📁 Project Architecture

```bash
com.hiresphere.hiresphere/
 ├── Analytics/           # Recruiter pipeline statistics and metrics
 ├── Application/         # Job applications, lifecycle states, and candidate reviews
 ├── Auth/                # Authentication, User accounts, JWT Filters, SecurityConfig
 ├── Config/              # Cloudinary cloud storage and CORS setup
 ├── Exception/           # Centralized GlobalExceptionHandler and REST API errors
 ├── Job/                 # Job postings, dynamic search specifications, and filtering
 ├── JobSeeker/           # Candidate profile, education, experience, portfolio, resumes
 ├── Notification/        # Recruiter & Candidate event notifications
 ├── Recruiter/           # Recruiter company profiles and candidate search
 └── SavedJob/            # Candidate bookmarks and saved jobs
```

---

## 📡 Core REST Endpoints Overview

| Category | Method | Endpoint | Access |
| :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/user/auth/register` | Public |
| **Auth** | `POST` | `/user/auth/login` | Public |
| **Auth** | `POST` | `/user/auth/forgot-password` | Public |
| **Auth** | `POST` | `/user/auth/reset-password` | Public |
| **Jobs** | `GET` | `/Jobs/getAllJobs` | Public |
| **Jobs** | `GET` | `/Jobs/search/paged` | Public |
| **Resume** | `POST` | `/jobseeker/resume/upload` | Job Seeker |
| **Resume** | `GET` | `/jobseeker/resume/download/{file}` | Public |
| **Resume** | `GET` | `/recruiter/resume/download/{file}` | Public |
| **Profile** | `POST` | `/jobseeker/createProfile` | Job Seeker |
| **Profile** | `GET` | `/jobseeker/getProfile` | Job Seeker |
| **Applications** | `POST` | `/applications/apply/{jobId}` | Job Seeker |
| **Applications** | `GET` | `/applications/myApplications` | Job Seeker |
| **Recruiter** | `POST` | `/Jobs/postJob` | Recruiter |
| **Recruiter** | `GET` | `/applications/job/{jobId}` | Recruiter |
| **Recruiter** | `PUT` | `/applications/updateStatus/{id}` | Recruiter |
| **Analytics** | `GET` | `/analytics/recruiter/overview` | Recruiter |

---

## ⚙️ Environment Configuration

Set these environment variables in your deployment platform (e.g. Render, Railway) or `application.properties`:

```properties
# Database Connection
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/hiresphere_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password

# JWT HS512 Secret
JWT_SECRET_KEY=jhckwhdwdbxuku2unqlwiu18dhd7qhdqjdqugqwbasgwlilsxnksuxwdmsxb7z8x

# CORS Frontend Allowed Origins
CORS_ALLOWED_ORIGINS=http://localhost:5173,https://your-frontend.vercel.app

# Optional: Cloudinary Cloud Storage
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Optional: Gmail SMTP
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-16-digit-app-password
```

---

## 💻 Local Quickstart

### Prerequisites
- Java 22 or higher
- MySQL 8.0 running locally on port 3306 (`hiresphere_db`)

### Run Application
```bash
# Clone the repository
git clone https://github.com/NileshBorchate0677/hire-sphere.git
cd hire-sphere

# Build and run
./mvnw spring-boot:run
```
The server will start on `http://localhost:8080`.

---

## 👨‍💻 Author

**Nilesh Borchate**  
- GitHub: [@NileshBorchate0677](https://github.com/NileshBorchate0677)  
- Project: HireSphere Platform
