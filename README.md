# Memigma — Meme Matching Platform

Memigma is a lightweight "Tinder for Memes" built with Java 17 and Spring Boot.  
Users can browse, like, and save memes, receiving personalized recommendations based on their preferences.  
The platform integrates external services and ensures secure authentication and media management.

## Technology Stack

- Java 17
- Spring Boot 3.4.5
- Spring Web
- Spring Security
- Spring Data JPA
- Spring Mail
- Springdoc OpenAPI
- Thymeleaf
- JWT (JSON Web Tokens)
- PostgreSQL
- Amazon AWS S3
- Gemini AI
- Hibernate Validator
- Maven

## Key Features

- Secure user authentication with JWT tokens
- User registration with email verification
- Personalized meme recommendation system (Gemini AI)
- Meme upload and storage on Amazon S3
- Session management with Spring Security
- RESTful API design with OpenAPI documentation
- Dynamic server-side rendering with Thymeleaf templates

## Project Structure

```bash
src/
├── main/
│   ├── java/
│   │   └── kz/memigma/project/
│   │       ├── configs/
│   │       ├── controllers/
│   │       ├── dto/
│   │       ├── models/
│   │       ├── payload/
│   │       ├── repositories/
│   │       ├── services/
│   │       └── MemigmaApplication.java
│   ├── resources/
│   │   ├── templates/
│   │   └── static/
└── test/
    └── java/
        └── kz/memigma/project/

```
Installation
### Clone the repository:
```bash 
git clone https://github.com/your-username/memigma.git 
```
### Run the application:
```bash 
./mvnw spring-boot:run
```
## API Endpoints

- <span style="color: #4dff6d;"><strong>POST</strong></span> `/api/auth/login` — <span style="color: #6c757d;">User login</span>
- <span style="color: #ff1275;"><strong>GET</strong></span> `/api/memes` — <span style="color: #6c757d;">Upload a meme</span>
- <span style="color: #4dff6d;"><strong>POST</strong></span> `/api/memes/upload` — <span style="color: #6c757d;"></span>
- <span style="color: #4dff6d;"><strong>POST</strong></span> `/api/memes/like` — <span style="color: #6c757d;">Like a meme</span>
- <span style="color: #ff1275;"><strong>GET</strong></span> `/api/profile/{username}` — <span style="color: #6c757d;">Fetch user profile</span>
- <span style="color: #ff1275;"><strong>GET</strong></span> `/api/find-people` — <span style="color: #6c757d;">Discover new people</span>







