# Project Overview
This is a RESTful API built using Spring Boot, PostgreSQL, and Docker, deployed on Google Cloud. The API utilizes JWT for security and is tested using Postman and Swagger.

## Table of Contents
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Security](#security)

## Project Structure
The project is structured as follows:
- **src/main/java**: Java source code
- **src/main/resources**: Application configuration files
- **Dockerfile**: Docker configuration file
- **docker-compose.yml**: Docker Compose configuration file
- **build.gradle**: Gradle build configuration file

## Technologies Used
- **Spring Boot**: Latest version
- **PostgreSQL**: Database management system
- **Docker**: Containerization platform
- **Google Cloud**: Cloud platform for deployment
- **JWT**: JSON Web Token for security
- **Postman**: API testing tool
- **Swagger**: API documentation tool
- **JDK 21**: Java Development Kit
- **Gradle**: Build tool

## Getting Started
1. Clone the repository:
   ```bash
   git clone https://github.com/sreyny-dev/mobile-banking-api

2. Build the project:
   ```bash
   ./gradlew build
3. Run the project:
   ```bash
   ./gradlew bootRun
4. Access the API: http://localhost:8080/swagger-ui/index.html

## API Documentation 
The API documentation is available at http://localhost:8080/swagger-ui/index.html. This documentation provides information on available endpoints, request and response formats, and authentication mechanisms.

## Security
The API uses JWT for security. To access protected endpoints, you need to obtain a JWT token by sending a POST request to the /login endpoint with valid credentials.
