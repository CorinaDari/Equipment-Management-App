
# Farm Equipment Management System

![CI](https://github.com/CorinaDari/Equipment-Management-App/actions/workflows/ci.yml/badge.svg)

A backend application built with Java and Spring Boot for managing farm equipment, assigning machinery to workers, and handling equipment repair requests.

The project focuses on equipment management, maintenance workflows, database persistence, and authentication.

## Features

- **Repair requests** – Workers create repair tickets with business rule validation: the equipment must be actively assigned to the requesting worker, and duplicate active tickets for the same equipment are rejected.
- **Repair workflow** – Mechanics claim open tickets and finalize them with a cost and resolution timestamp. Tickets can be filtered by status.
- **Equipment assignment** – Assign equipment to workers for a given period.
- **Authentication** – Spring Security with HTTP Basic authentication, BCrypt password hashing, and user roles (`ADMIN`, `MANAGER`, `MECHANIC`, `OPERATOR`).
- **Database migrations** – Versioned schema migrations and SQL seed data managed with Flyway.
- **API documentation** – Interactive OpenAPI / Swagger UI.
- **Docker support** – Multi-stage Dockerfile and Docker Compose setup (application + PostgreSQL).
- **Continuous integration** – GitHub Actions pipeline that builds the project, runs unit tests, builds the Docker image and runs a smoke test.

> The application is under active development. Some business workflows and API endpoints are still being implemented.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot | Backend framework |
| Spring Security | Authentication and security |
| Spring Data JPA | Database access |
| Hibernate | JPA implementation |
| Flyway | Database migrations |
| BCrypt | Password hashing |
| Maven | Build and dependency management |
| SQL | Schema migrations and seed data |
| GitHub Actions | Continuous integration |
| PostgreSQL | Relational database |
| JUnit, Mockito | Unit testing |
| Springdoc OpenAPI / Swagger UI | API documentation |
| Docker, Docker Compose | Containerization and local environment |

## Run with Docker

The quickest way to run the project. Requires only Docker Desktop (no Java, Maven or PostgreSQL needed).

```bash
docker compose up --build
```

Then open http://127.0.0.1:8080/swagger-ui/index.html

Flyway creates the schema and seed data automatically on startup. To stop the stack: `docker compose down` (add `-v` to also delete the database volume).

> The credentials in `docker-compose.yml` are for local development only.

## Getting Started

### Prerequisites

- JDK 17
- Git
- PostgreSQL (or use [Docker](#run-with-docker) instead)

### 1. Clone the Repository

```bash
git clone https://github.com/CorinaDari/Equipment-Management-App.git
cd Equipment-Management-App
```

### 2. Configure the Database

Configure the following environment variables before starting the application:

| Variable | Description |
|---|---|
| `DB_URL` | Database connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |

For example, when using PostgreSQL:

```text
DB_URL=jdbc:postgresql://localhost:5432/farm_db
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
```

The database name and credentials above are examples. Use values matching your local database configuration.

The application reads these variables through `src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

server.port=8080
```

Database credentials should be configured locally and must not be committed to version control.

### 3. Run the Application

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

The application runs at [http://localhost:8080](http://localhost:8080).

Flyway applies pending database migrations according to the application configuration. Hibernate validates the entity mappings against the database schema without automatically creating or updating tables.

### 4. Build the Project

On Linux or macOS:

```bash
./mvnw -B package
```

On Windows:

```bat
mvnw.cmd -B package
```

The packaged application is generated in the `target/` directory.

### 5. Run the Tests

On Linux or macOS:

```bash
./mvnw -B test
```

On Windows:

```bat
mvnw.cmd -B test
```

## API Documentation (Swagger UI)

The application provides interactive API documentation through Swagger UI.

After starting the application, open:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Swagger UI allows you to:
- Browse available REST API endpoints.
- Inspect request parameters, request bodies, and response schemas.
- Execute API requests directly from the browser.
- Verify successful responses and error handling.
- Test endpoints that interact with the database.

### Trying authenticated endpoints

1. Click **Authorize** in Swagger UI.
2. Enter a demo account's email as username and `demo` as password (see [Demo Accounts](#demo-accounts)).
3. Call any endpoint allowed for that account's role.

## Architecture

The application uses a layered structure, with packages organized by responsibility:

- **Controllers** – Handle HTTP requests.
- **Domain entities** – Represent the main application concepts and database entities.
- **DTOs and mappers** – Structure API data and map between DTOs and entities.
- **Repositories** – Provide database access through Spring Data JPA.
- **Services** – Encapsulate application and authentication-related logic.
- **Configuration** – Contains application security configuration.
- **Flyway migrations** – Manage database schema changes and initial data.

## Continuous Integration

The GitHub Actions workflow runs on pushes and pull requests targeting the `master` branch.

The current pipeline has two jobs:

**build**
1. Checks out the repository.
2. Sets up Java 17 using Amazon Corretto.
3. Caches Maven dependencies.
4. Builds the project and runs automated tests using Maven.

**docker** (runs after `build` succeeds)
1. Builds the Docker image.
2. Starts the application and database with Docker Compose.
3. Runs a smoke test against `/v3/api-docs` to confirm the app starts.

## Roadmap

### 1. Core Infrastructure
- [x] Set up the Spring Boot project and layered architecture.
- [x] Define domain entities and relationships.
- [x] Configure database migrations and seed data with Flyway.
- [x] Create DTOs for implemented API operations.
- [x] Configure mappers for the implemented DTOs.
- [ ] Complete DTO coverage for the remaining API operations.
- [ ] Complete entity-to-DTO mapping across the application.
- [x] Configure Spring Security authentication and BCrypt password hashing.
- [ ] Complete and verify role-based authorization.

### 2. Equipment and Worker Management
- [ ] Implement equipment management endpoints and business rules.
- [ ] Implement worker management endpoints.
- [x] Implement equipment assignment
- [ ] Implement reassignment workflows.
- [ ] Validate assignment rules and prevent conflicting active assignments.

### 3. Repair Ticket Management
- [x] Implement repair ticket retrieval and filtering by status.
- [x] Allow mechanics to claim and manage repair tickets.
- [x] Implement repair status transitions and resolution details.
- [x] Track repair costs, resolution timestamps, and responsible technicians.
- [ ] Add a detailed view for a single repair ticket (`GET /repairTicket/{id}`).

### 4. Maintenance Management
- [ ] Implement maintenance record endpoints.
- [ ] Track maintenance history for each equipment item.
- [ ] Add maintenance scheduling and service history views.

### 5. Quality and Reliability
- [ ] Add request validation and consistent API error handling.
- [ ] Handle concurrent repair requests and enforce database-level integrity constraints.
- [x] Add unit tests for service-layer business logic.
- [ ] Add integration tests for API endpoints and database interactions.
- [x] Extend CI to run automated tests.
- [x] Build the Docker image and run a smoke test in CI.

### 6. Developer Experience and Deployment
- [x] Add interactive API documentation using Swagger UI.
- [x] Add Docker support for local development.
- [ ] Document API endpoints, business rules, and example requests.
- [ ] Prepare deployment configuration and production-ready settings.

## Security Notes

- Passwords are hashed using BCrypt.
- Seed accounts and shared demo passwords are intended exclusively for local development and testing.
- Production credentials and other sensitive configuration must not be committed to version control.

### Demo Accounts

The application includes preconfigured demo accounts for testing authentication and role-based access.

- **Username:** The email address associated with each account.
- **Password:** `demo`
- **Accounts and roles:** See the database seed data for the available usernames and roles.

All demo accounts share the same password for convenience. These credentials are intended exclusively for local development and demonstration purposes and must never be used in production.

## Author

**Corina Dari**

- GitHub: https://github.com/CorinaDari

