🏢 BillBoarding & Hording Management System – Backend

A complete Spring Boot + JWT + Role-Based Access + Image Upload + Owner Billboard Management backend system for managing Billboards / Hordings, Owners, Advertisers, and Admin workflows.

🚀 1. Overview

This project provides a backend API for a real-world billboard/hording management platform where:

Admin manages users & KYC verification.

Owner creates billboards, uploads images, sets pricing & location.

Advertiser browses billboards and books them (coming module).

Uses JWT authentication, Role-based authorization, and File upload handling.

🧱 2. Key Features Implemented
✅ User Authentication & Roles
Role	Capabilities
Admin	Approve KYC, block users, manage platform
Owner	Create billboards, upload images, manage availability
Advertiser	Browse available billboards, book ads (next module)
✅ JWT Security

Stateless authentication

Role-based URL access

Tokens contain email + role

Spring Security + custom JWT filter

✅ Billboard Management (Owner Module)

Create billboard

Update billboard details

Delete billboard

Upload multiple images (min 3 required)

Billboard types: NORMAL, LED, DIGITAL

Store billboard geolocation: latitude + longitude

✅ Image Upload System

Owner uploads at least 3 images

Stored under:

uploads/billboards/{billboardId}/


Image paths saved in database

Files served statically via /uploads/**

📁 3. Project Folder Structure
BillBoarding-And-Hording/
│
├── src/main/java/com/billboarding/
│   ├── Entity/
│   │   ├── User.java
│   │   ├── OWNER/Billboard.java
│   │   └── ENUM/
│   │       ├── UserRole.java
│   │       ├── KycStatus.java
│   │       └── BillboardType.java
│   │
│   ├── Controller/
│   │   ├── AUTH/AuthController.java
│   │   ├── Owner/OwnerController.java
│   │   ├── Admin/AdminController.java
│   │   └── Advertiser/AdvertiserController.java
│   │
│   ├── Services/
│   │   ├── Auth/AuthService.java
│   │   ├── BillBoard/BillboardService.java
│   │   ├── UserService.java
│   │   └── JWT/JwtService.java
│   │
│   ├── Repository/
│   │   ├── UserRepository.java
│   │   └── BillBoard/BillboardRepository.java
│   │
│   ├── configs/
│   │   ├── SecurityConfig.java
│   │   ├── JwtAuthFilter.java
│   │   └── WebConfig.java
│   │
│   └── BillBoardingAndHordingApplication.java
│
├── src/main/resources/
│   ├── application.properties
│   └── static/uploads/ (generated at runtime)
│
└── pom.xml

🔐 4. Authentication & Authorization
JWT Token Structure

A generated token contains:

{
  "role": "OWNER",
  "sub": "owner@test.com",
  "iat": 1764631084,
  "exp": 1764737484
}

Role-Based API Access
Role	Accessible Routes
ADMIN	/api/admin/**
OWNER	/api/owner/**
ADVERTISER	/api/advertiser/**
PUBLIC	/api/auth/login, /api/auth/register

SecurityConfig ensures only the correct role can hit each endpoint.

🗂️ 5. Billboard Entity Structure
Billboard {
  Long id;
  String title;
  String location;
  Double latitude;
  Double longitude;
  Double pricePerDay;
  String size;
  BillboardType type;     // NORMAL, LED, DIGITAL
  Boolean available;
  User owner;
  LocalDateTime createdAt;
  List<String> imagePaths;
}

🖼️ 6. Image Upload System
Storage Path Example
uploads/billboards/5/
 ├── 1764654926724_pic1.jpg
 ├── 1764654926931_pic2.jpg
 └── 1764654926972_pic3.jpg

Controller Endpoint
POST /api/owner/billboards/{id}/upload-images

Requirements

✔ Minimum 3 images
✔ Any number ≥ 3 allowed

🌍 7. Geolocation Support (COMING NEXT)

Each billboard stores:

latitude: 18.5204
longitude: 73.8567


This will be integrated with:

Google Maps Geocoding API

Frontend map view displaying billboards

🧪 8. API Testing (curl Examples)
1️⃣ Register User
curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{
  "name":"Owner User",
  "email":"owner@test.com",
  "password":"owner123",
  "phone":"8888888888",
  "role":"OWNER"
}'

2️⃣ Login
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
  "email":"owner@test.com",
  "password":"owner123"
}'

3️⃣ Create Billboard
curl -X POST http://localhost:8080/api/owner/billboards \
-H "Authorization: Bearer <TOKEN>" \
-H "Content-Type: application/json" \
-d '{
  "title": "LED Board Near Station",
  "location": "Pune Station",
  "latitude": 18.528,
  "longitude": 73.874,
  "pricePerDay": 2000,
  "size": "40x20",
  "type": "LED"
}'

4️⃣ Upload Images (minimum 3)
curl -X POST http://localhost:8080/api/owner/billboards/2/upload-images \
-H "Authorization: Bearer <TOKEN>" \
-F "images=@/path/img1.jpg" \
-F "images=@/path/img2.jpg" \
-F "images=@/path/img3.jpg"

🧭 9. Environment Configuration
application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/billboard
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update

google.maps.api.key=YOUR_API_KEY_HERE  # (Coming soon)

🛠️ 10. Future Modules
Module	Status
Google Maps Geolocation API	⏳ Pending
Advertiser Billboard Booking	⏳ Pending
Payment Gateway (Razorpay)	⏳ Pending
KYC Upload & Verification	⏳ Pending
Admin Dashboard Analytics	⏳ Pending
👨‍💻 11. Tech Stack

Backend: Java 21, Spring Boot 3

Security: Spring Security + JWT

Database: MySQL

Storage: Local file system

Build Tool: Maven

Validation: Jakarta Validation

Logging: SLF4J + Logback

📌 12. How to Run
mvn clean install
mvn spring-boot:run


App runs at:

http://localhost:8080

🏁 13. Conclusion

This backend now fully supports:

Role-based authentication

JWT token security

Billboard CRUD

Billboard image upload

Billboard geolocation

Billboard type

Admin + Owner + Advertiser roles

You can now safely integrate Maps, Booking, and Payments next.
