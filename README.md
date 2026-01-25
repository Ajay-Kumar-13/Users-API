# 🔐 Users API

A fully reactive Spring Boot application for managing users, roles, and permissions with JWT-based authentication. Built with non-blocking I/O for high performance and scalability.

## 🚀 Tech Stack

| Technology | Purpose |
|------------|---------|
| **Java 21** | Programming language |
| **Spring Boot 3.3.5** | Application framework |
| **Spring WebFlux** | Reactive web framework (non-blocking I/O) |
| **Spring Security** | Authentication and authorization |
| **R2DBC** | Reactive database connectivity |
| **PostgreSQL** | Relational database |
| **Flyway** | Database migrations |
| **JWT** | Stateless authentication tokens |
| **BCrypt** | Password hashing |
| **Lombok** | Boilerplate code reduction |
| **Gradle** | Build automation |
| **Docker** | Containerization |
| **AWS RDS** | Production database hosting |
| **AWS Secrets Manager** | Secure credential management |

## ✨ Key Features

- ✅ **Fully Reactive Architecture** - Non-blocking I/O with Project Reactor
- ✅ **JWT Authentication** - Secure, stateless token-based auth
- ✅ **Role-Based Access Control (RBAC)** - Hierarchical permission system
- ✅ **Granular Permissions** - CREATE, READ, UPDATE, DELETE authorities
- ✅ **User Permission Overrides** - Individual user exceptions to role permissions
- ✅ **Secure Password Storage** - BCrypt hashing
- ✅ **AWS Integration** - RDS with SSL and Secrets Manager
- ✅ **Database Migrations** - Version-controlled schema with Flyway
- ✅ **Docker Support** - Easy local development and deployment
- ✅ **Global Exception Handling** - Consistent error responses

## 📋 Table of Contents

- [Architecture Overview](#-architecture-overview)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [API Endpoints](#-api-endpoints)
- [Request/Response Examples](#-requestresponse-examples)
- [Database Schema](#-database-schema)
- [Configuration](#-configuration)
- [Deployment](#-deployment)
- [Security](#-security)
- [Project Structure](#-project-structure)

## 🏗 Architecture Overview

### Permission System Hierarchy

```
Users ──┬──> Role ──┬──> Authorities (CREATE, READ, UPDATE, DELETE)
        │           │
        └──> User Authority Overrides (Can add/remove specific permissions)
```

**How it works:**

1. **Users** are assigned a **Role** (e.g., ADMIN, MANAGER, USER)
2. **Roles** contain multiple **Authorities** (permissions like CREATE, READ, UPDATE, DELETE)
3. **Users** inherit all authorities from their role
4. **User Authority Overrides** allow specific users to have exceptions:
   - ➕ Add extra permissions not in their role
   - ➖ Remove permissions that are in their role

### Example Scenario

```
Role: MANAGER
├─ Authorities: [CREATE, READ, UPDATE]

User: John (Manager Role)
├─ Default Permissions: [CREATE, READ, UPDATE]
├─ Override: DELETE = true (added)
└─ Final Permissions: [CREATE, READ, UPDATE, DELETE]

User: Jane (Manager Role)  
├─ Default Permissions: [CREATE, READ, UPDATE]
├─ Override: UPDATE = false (removed)
└─ Final Permissions: [CREATE, READ]
```

## 🛠 Prerequisites

- **Java 21+** installed
- **Docker & Docker Compose** (for local development)
- **PostgreSQL** (for production deployment)
- **Gradle 8.7+** (wrapper included)
- **AWS Account** (for production with RDS)

## 🚀 Getting Started

### Local Development with Docker

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd users-api
   ```

2. **Build the application**
   ```bash
   ./gradlew clean build
   ```

3. **Run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

The API will be available at `http://localhost:8080`

### Local Development without Docker

1. **Set up PostgreSQL** (ensure it's running on port 5432)

2. **Configure application properties**
   - Update `src/main/resources/application.yml` with your database credentials

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

## API Endpoints

### Authentication

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/auth/login` | Login and receive JWT token | Public |
| GET | `/auth/test` | Test endpoint | Public |

**Login Request:**
```json
{
  "username": "superuser",
  "password": "password"
}
```

**Login Response:**
```json
{
  "jwtToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

### Users Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/admin/users` | Get all users | ADMIN |
| POST | `/admin/users/newuser` | Create new user | ADMIN |
| POST | `/admin/users/{userId}/override-permissions` | Override user permissions | ADMIN |

### Roles Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/admin/roles` | Get all roles | ADMIN |
| POST | `/admin/roles/newrole` | Create new role | ADMIN |

### Authorities Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/admin/authorities` | Get all authorities | ADMIN |
| POST | `/admin/authorities/new-authority` | Create new authority | ADMIN |

## Request/Response Examples

### Create User
```json
POST /admin/users/newuser
Authorization: Bearer <token>

{
  "username": "john.doe",
  "password": "securepassword",
  "roleId": "uuid-of-role"
}
```

### Create Role
```json
POST /admin/roles/newrole
Authorization: Bearer <token>

{
  "roleName": "MANAGER",
  "authorities": [
    "uuid-of-create-authority",
    "uuid-of-read-authority"
  ]
}
```

### Override User Permission
```json
POST /admin/users/{userId}/override-permissions
Authorization: Bearer <token>

{
  "authorityName": "DELETE",
  "active": false
}
```

## Database Schema

### Tables

- **users** - User accounts
- **roles** - Available roles
- **authorities** - Available permissions (CREATE, READ, UPDATE, DELETE)
- **role_authorities** - Many-to-many relationship between roles and authorities
- **user_authorities** - User-specific permission overrides

## Security

- Passwords are hashed using BCrypt
- JWT tokens for stateless authentication
- Role-based authorization with `@PreAuthorize`
- SSL/TLS for database connections in production
- AWS RDS CA certificates included for secure connections

### Code Structure

```
src/main/java/com/crm/users/
├── config/          # Security and application configuration
├── controller/      # REST endpoints
├── DTO/             # Data Transfer Objects
├── Exception/       # Custom exceptions and handlers
├── model/           # Domain entities
├── repository/      # R2DBC repositories
├── security/        # JWT and authentication logic
├── service/         # Business logic
└── util/            # Utility classes
```

## Migration from SQL Init to Flyway

The project has migrated from Spring's SQL initialization to Flyway:

- Schema: `src/main/resources/db/migration/V1__schema.sql`
- Seed data: `src/main/resources/db/migration/V2__seed_data.sql`

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please open an issue on GitHub.
