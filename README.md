# AeroKING - Flight Finder Application

A full-stack web application for searching low-cost airline flight prices, built with Spring Boot (Java) for the backend and React (JavaScript) with Vite for the frontend. This project demonstrates API integration, data caching, robust error handling, a responsive user interface, and containerization with Docker.

## Features

* Search flights by origin, destination (IATA codes), departure/return dates, passenger count (adults, children, infants), and currency (USD, EUR, HRK).
* Displays flight results in a table format including airport codes, full departure/return dates & times, number of stops (distinguishing "Direct" for 0 stops, and showing Outbound/Inbound counts), total passenger count, price, and currency.
* Caches search results locally using an H2 in-memory database to provide faster responses for repeated searches.
* Interactive modal to view selected flight offer details, including a placeholder "Buy Now" button.
* Client-side form validation with clear user feedback and input restrictions (e.g., preventing non-integer inputs for passenger counts).
* Loading state indicators on the frontend, including a disabled search button during API calls.
* Secure handling of API keys using Spring Profiles, with support for environment variables.
* Backend error handling with custom exceptions and structured JSON responses.
* Dockerized for ease of deployment and consistent runtime environments.

## Technology Stack

* **Backend:**
    * Java 17
    * Spring Boot (3.4.6)
    * Spring Data JPA (with Hibernate)
    * Spring Web & Spring WebFlux (for reactive `WebClient`)
    * H2 Database
    * Lombok
    * Maven (for build and dependency management)
    * SLF4J for logging (used for important messages to user and debugging)
* **Frontend:**
    * React (19.1)
    * JavaScript (ES6+) & CSS3
    * Vite (as build tool and dev server)
    * Axios (for API calls)
    * React Hook Form (for form handling)
    * React DatePicker
    * NPM (for package management)
* **API Integration:**
    * Amadeus Self-Service APIs (Flight Offers Search & OAuth2 for authentication)
* **Containerization:**
    * Docker & Docker Compose

## Prerequisites

* **For Running with Docker (Recommended for Evaluators):**
    * Docker Desktop (for Windows/macOS) or Docker Engine & Docker Compose (for Linux) installed and running.
* **For Running Natively (Alternative):**
    * Java Development Kit (JDK): Version 17 or higher (e.g., OpenJDK 17).
    * Apache Maven: Version 3.6.x or higher.
    * Node.js: (v22.15.0). This will include npm.
    * npm: (10.9.2).

## Setup and Running the Application

### 1. API Keys (Amadeus) - IMPORTANT

This application requires Amadeus API credentials to fetch flight data.

* The backend's main `application.properties` file uses placeholders for the API Key and Secret:
    ```properties
    amadeus.api.key=${AMADEUS_API_KEY}
    amadeus.api.secret=${AMADEUS_API_SECRET}
    ```
* **For Evaluation (Using Provided Test Keys):**
    This submission includes a specific configuration file `backend/src/main/resources/application-local.properties` which contains the necessary Amadeus **test** keys.
    * When running with Docker (see below), this file is automatically used.
    * When running natively, the backend application **must be run with the "local" Spring profile activated** to use these keys.

    *Note on Security:* In a typical development workflow, any file containing actual keys (like `application-local.properties` if it held live keys) would be added to `.gitignore`. For production, API keys would be managed exclusively via OS environment variables or a secrets management service. The inclusion of `application-local.properties` with test keys in this submission package is for ease of evaluation.

### 2. Running with Docker Compose (Recommended for Evaluators)

1.  Ensure Docker is running.
2.  Navigate to the root directory of the project (where `docker-compose.yml` is located).
3.  Execute the following command:
    ```bash
    docker-compose up --build
    ```
    * The `--build` flag ensures Docker images are built from the Dockerfiles. This might take a few minutes on the first run.
    * This will start both the backend and frontend services.
4.  **Accessing the Application:**
    * Frontend: Open your web browser and navigate to `http://localhost:5173`
    * Backend API (if needed for direct checks): `http://localhost:8080`
5.  **Stopping the Application:**
    * Press `Ctrl+C` in the terminal where `docker-compose up` is running.
    * To remove the containers and network, run `docker-compose down`.

### 3. Running Natively (Alternative Method)

**A. Backend:**
1.  Navigate to the `backend` directory: `cd backend`
2.  To run the Spring Boot application with the "local" profile (to use API keys from `application-local.properties`):
    * **On Windows (PowerShell):**
        ```powershell
        $env:SPRING_PROFILES_ACTIVE="local"; ./mvnw spring-boot:run
        ```
    * **On Windows (Command Prompt):**
        ```cmd
        set SPRING_PROFILES_ACTIVE=local && mvnw.cmd spring-boot:run
        ```
    * **On Linux/macOS:**
        ```bash
        export SPRING_PROFILES_ACTIVE=local && ./mvnw spring-boot:run
        ```
3.  The backend server will start on `http://localhost:8080`.

**B. Frontend:**
1.  Open a new terminal.
2.  Navigate to the `frontend` directory: `cd frontend`
3.  Install dependencies (if you haven't already): `npm install`
4.  Create a `.env` file in the `frontend` directory (`frontend/.env`) with the following content (this tells the local dev server where the backend is):
    ```env
    VITE_API_BASE_URL=http://localhost:8080
    ```
5.  Start the frontend development server: `npm run dev`
6.  The frontend will open in your browser (e.g., `http://localhost:5173`).

## Project Structure Overview 
(Keep your existing detailed structure here - it's good!)

* `/backend`: Contains the Spring Boot Java application...
* `/frontend`: Contains the React application...

## Key Technical Decisions & Highlights

* **Containerization:** Dockerized with Docker Compose for consistent environments and simplified setup.
* **Caching Strategy:** Robust server-side caching (H2 & Spring Data JPA) with intelligent date-range queries significantly improves performance for repeated searches.
* **API Key Management:** Secure practice demonstrated using Spring Profiles and support for environment variables, with a clear setup for evaluation using test keys.
* **Error Handling:** Both backend (global handler, custom exceptions) and frontend (user feedback, form validation) implement robust error management.
* **User Experience (UX):** Focus on clear data presentation (full flight times, "Direct" flight indication, distinct Out/In stops, passenger counts) and user interactions (loading states, input validation hints, modal for details).
* **Code Organization & Quality:** Modular structure, Javadoc and inline comments, and SLF4J logging promote maintainability and readability.

## Known Limitations / Future Improvements

* The "Buy Now" button in the offer modal is currently a placeholder; a full booking flow is a potential future enhancement.
* While basic error handling is implemented, specific error messages from the Amadeus API for "no flights found" scenarios.
* More extensive test coverage would be added for a production-grade application.
* **The Amadeus API access token is currently fetched at application startup and is not automatically refreshed. For a production, long-running service, a token refresh mechanism (e.g., on a schedule or upon receiving a 401 error) would be implemented to handle token expiry (typically around 30 minutes).**

---