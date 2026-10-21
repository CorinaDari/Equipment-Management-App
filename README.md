
# Farm Equipment Management System

A backend application built with Java and Spring Boot for managing farm equipment, assigning machinery to workers, and handling equipment repair requests.

The project focuses on equipment management, maintenance workflows, database persistence, and authentication.

## Features

- **Equipment management** – Store and manage farm equipment and machinery.
- **Worker assignments** – Support assigning machinery to farm workers.
- **Repair requests** – Model the repair request workflow, including mechanic assignment and status updates.
- **Authentication** – Spring Security authentication with BCrypt password hashing.
- **Database migrations** – Versioned schema migrations and SQL seed data managed with Flyway.
- **Continuous integration** – Automated Maven builds using GitHub Actions.

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

## Getting Started

### Prerequisites

- JDK 17
- Git
- A relational database supported by the project

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

The application runs at:

http://localhost:8080

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

The current pipeline:

1. Checks out the repository.
2. Sets up Java 17 using Amazon Corretto.
3. Caches Maven dependencies.
4. Packages the application using the Maven Wrapper.

Automated tests are currently skipped in the CI build and may be added in a future iteration.

## Roadmap

- [ ] Complete REST API endpoints for equipment and worker management.
- [ ] Implement equipment assignment business rules.
- [ ] Complete the repair request lifecycle and status transitions.
- [ ] Implement and verify role-based authorization.
- [ ] Add request validation and consistent API error handling.
- [ ] Add unit and integration tests.
- [ ] Extend CI to execute automated tests.
- [ ] Add Docker support for local development.

## Security Notes

- Passwords are hashed using BCrypt.
- Seed accounts and shared demo passwords are intended exclusively for local development and testing.
- Production credentials and other sensitive configuration must not be committed to version control.

## Author

**Corina Dari**

- GitHub: https://github.com/CorinaDari

