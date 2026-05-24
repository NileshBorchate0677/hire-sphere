# Hire Sphere

Hire Sphere is a Spring Boot based Job Portal application designed to simplify the recruitment process for both recruiters and job seekers.  
The application provides secure authentication using JWT and follows a scalable backend architecture using Spring Boot and REST APIs.

---

## Features

- JWT Based Authentication & Authorization
- Role Based Access Control
- User Registration & Login
- Recruiter and Candidate Management
- Job Posting and Job Application APIs
- Secure REST APIs
- Spring Security Integration
- MySQL Database Integration
- Hibernate / JPA ORM
- AI Feature Integration (In Progress)

---

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Hibernate / JPA
- REST APIs

### Database
- MySQL

### Build Tool
- Maven

### Tools & Platforms
- Eclipse IDE
- Git & GitHub
- Postman

---

## Project Structure

```bash
src/
 ├── main/
 │   ├── java/
 │   └── resources/
 └── test/
```

---

## API Modules

- Authentication Module
- User Management Module
- Job Management Module
- Security Configuration
- AI Integration Module

---

## Security

The application uses Spring Security with JWT Authentication to secure APIs and manage user access.

Features:
- Stateless Authentication
- Token Validation
- Protected Routes
- Role Based Authorization

---

## Database Configuration

Configure your MySQL credentials inside:

```properties
application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hiresphere_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## Getting Started

### Clone Repository

```bash
git clone https://github.com/NileshBorchate0677/hire-sphere.git
```

### Navigate to Project

```bash
cd hire-sphere
```

### Run Application

```bash
mvn spring-boot:run
```

---

## Future Enhancements

- Resume Analysis using AI
- Job Recommendation System
- Email Notifications
- Admin Dashboard
- Interview Scheduling System

---

## Author

### Nilesh Borchate

- MCA Student
- Java & Spring Boot Developer
- Backend Development Enthusiast

GitHub:
https://github.com/NileshBorchate0677

---

## License

This project is developed for learning and portfolio purposes.
