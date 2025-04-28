# Memigma — Meme Matching Platform

**Memigma** is an innovative, lightweight meme discovery platform, often called the "Tinder for Memes." Built with **Java 17** and **Spring Boot**, Memigma allows users to swipe through memes, like or save their favorites, and receive personalized recommendations. The platform leverages **Gemini AI** to analyze user preferences and provide a custom-tailored meme experience. Users can also explore new content based on themes and interests.

> **Coming Soon:** [Visit Memigma here](#) (Link to be added after deployment)

## Key Features

- **Swipe through memes**: Browse through memes, like them, or skip. The more you interact, the better your meme recommendations become.
- **AI-driven recommendations**: Gemini AI learns your preferences and suggests memes you’ll love.
- **User Profiles**: Create an account, customize your profile, and track your favorite memes.
- **Meme Upload and Storage**: Upload memes and store them securely in the cloud with **Amazon S3**.
- **Interest-based Search**: Search for memes based on keywords, themes, and topics to discover content you love.
- **Secure User Authentication**: Login and account management are handled securely via **JWT tokens**.
- **Real-time Stats**: Track your meme interactions and history for a more personalized experience.
- **Seamless Integration**: The platform integrates easily with third-party services, ensuring smooth user experience and media handling.

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.4.5, Spring Web, Spring Security, Spring Data JPA, Spring Mail, Springdoc OpenAPI
- **Frontend**: Thymeleaf for dynamic server-side rendering
- **Database**: PostgreSQL for efficient data storage and querying
- **Authentication**: JWT (JSON Web Tokens) for secure login and session management
- **Cloud Storage**: Amazon AWS S3 for storing meme images and media
- **AI**: Gemini AI for personalized meme recommendations
- **Validation**: Hibernate Validator for user input validation
- **Build Tool**: Maven for project management and dependency handling

### Project Design and Development
This project follows a modular design with a clear separation between the frontend (JavaScript) and backend (Spring Boot). We used Agile for iterative development and continuous improvements.

### Reason of choosing this Technical Stack
Spring Boot was chosen for its familiarity and efficiency, thanks to previous experience. Other tools were picked for their seamless integration with authentication, data management, and containerization.

## Project Structure

```
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

## Installation Guide

To get started with **Memigma**, follow these steps:

### Clone the repository:
```
git clone https://github.com/your-username/memigma.git
```

### Install dependencies and run the application:
```
./mvnw spring-boot:run
```

The application should now be running locally, and you can access it through [http://localhost:8080](http://localhost:8080).
## API Endpoints

- **POST** `/api/auth/register` — User registration with email verification
- **POST** `/api/auth/login` — User login and authentication
- **POST** `/api/auth/verify-code` — Verify registration code and activate account
- **GET** `/api/auth/profile` — Fetch the authenticated user’s profile

- **GET** `/api/memes` — Fetch a list of memes
- **POST** `/api/memes/upload` — Upload a meme to the platform
- **POST** `/api/memes/like` — Like or dislike a meme (track meme interaction)
- **POST** `/api/memes/favorite` — Add a meme to your favorites

- **GET** `/api/recommend` — Get meme recommendations based on user’s activity (AI-powered)
- **GET** `/api/find-people` — Discover new users with similar meme interests

- **POST** `/api/ai-chat` — Get personalized meme recommendations from AI based on a user prompt

## License
### Feel free to use, modify, and share this code without restrictions. No warranty provided.


**Memigma** is here to revolutionize the way you interact with memes — swipe, laugh, and discover content that’s tailored just for you.
