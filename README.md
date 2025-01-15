<div align="center" id="top"> 
  <img src="./assets/chatop_banner.png" alt="ChaTop" height="150px" />
</div>

<h1 align="center">ChâTop</h1>

<p align="center">
  <img alt="Github top language" src="https://img.shields.io/github/languages/top/NDRBAX/ChaTop?color=56BEB8">
  <img alt="Github language count" src="https://img.shields.io/github/languages/count/NDRBAX/ChaTop?color=56BEB8">
  <img alt="Repository size" src="https://img.shields.io/github/repo-size/NDRBAX/ChaTop?color=56BEB8">
</p>

<p align="center">
  <a href="#features">Features</a> &#xa0; | &#xa0;
  <a href="#features">Features</a> &#xa0; | &#xa0;
  <a href="#technologies">Technologies</a> &#xa0; | &#xa0;
  <a href="#requirements">Requirements</a> &#xa0; | &#xa0;
  <a href="#getting-started">Getting started</a> &#xa0; | &#xa0;
  <a href="https://github.com/NDRBAX" target="_blank">Author</a>
</p>

<br>

The Rental Portal Backend is a robust and scalable solution designed to support ChâTop, a real estate rental company in a tourist area. This backend application facilitates seamless communication between potential tenants and property owners. The backend ensures secure user authentication, comprehensive rental listings management, and detailed API documentation using Swagger.

This project plays a crucial role in transforming the ChâTop portal from a mockup-based application to a fully operational online platform, extending its initial functionality from the Basque coast to eventually cover all of France.

## Features

- **User Authentication:** Secure JWT-based authentication to protect resources and user data.
- **Rental Listings Management:** Complete CRUD functionality for managing rental properties.
- **API Documentation:** Comprehensive Swagger-based documentation for seamless integration and testing.
- **Database Integration:** Reliable storage and retrieval using MySQL for rental and user data.
- **Modular Architecture:** Clean separation of concerns using the MVC pattern, ensuring maintainability and scalability.

## Technologies

- **Java 17:** For modern and efficient backend development.
- **Spring Boot 3.4.0:** Framework for rapid development and deployment.
- **Spring Security:** Secure the application with JWT authentication.
- **Spring Data JPA:** Simplified interaction with MySQL database.
- **Lombok:** Reduce boilerplate code for model classes.
- **Swagger (springdoc):** API documentation for improved developer experience.
- **MapStruct:** Streamline mapping between DTOs and Entities.

## Requirements

- Java Development Kit (JDK) 17
- Maven 3.8+
- MySQL 9.0+


## Getting Started

1. Clone the repository.
   ```bash
   git clone https://github.com/NDRBAX/ChaTop.git
   cd chatop/backend/
   ```
2. Ensure Maven is installed and execute:.
   ```bash
   mvn clean install 
   ```
   Or if you are using the Maven Wrapper:
   ```bash
   ./mvnw clean install
   ```
3. Set Up the Database:
    - Install and configure MySQL on your machine.
    - Create a new database named `rental_portal` or any other name of your choice:
        ```sql
        CREATE DATABASE rental_portal;
        ```
    - Create an .env file in the root directory of the project and add the following properties:
        ```properties
        USERNAME=your_username
        DATABASE=your_database_name
        PASSWORD=your_database_password
        JWT_SECRET=your_secret_key
        ```
    - Set up Cloudinary account and add the following properties to the .env file:
        ```properties
        API_SECRET=your_api_secret
        API_KEY=your_api_key
        CLOUD_NAME=your_cloud_name
        ```
    - Run the application to create the necessary tables:
        ```bash
        mvn spring-boot:run
        ```
        or if you are using the Maven Wrapper:
        ```bash
        ./mvnw spring-boot:run
        ```

4. Access the Application
    - Swagger UI: http://localhost:8080/swagger-ui/index.html
    - API Endpoints: Refer to Swagger documentation.

