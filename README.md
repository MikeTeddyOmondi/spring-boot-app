# Spring Boot Todo Application with libsql

A complete RESTful Todo application built with Spring Boot 3.2, JPA, and libsql database support.

## Complete Application Features

**Core Functionality:**

- Full CRUD operations for todos
- Advanced search and filtering
- Status management (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
- Due date tracking
- Overdue todo detection

**Technical Features:**

- RESTful API with proper HTTP status codes
- Input validation with custom error messages
- Global exception handling
- DTO pattern for clean data transfer
- Comprehensive unit and integration tests
- Docker support for containerization

**Database Integration:**

- libsql/SQLite database support
- JPA repositories with custom queries
- Automatic schema generation
- Sample data initialization

## Project Structure

The application follows Spring Boot best practices with:

- **Controller Layer**: REST endpoints with proper error handling
- **Service Layer**: Business logic and transaction management
- **Repository Layer**: Data access with custom queries
- **Model Layer**: JPA entities with validation
- **DTO Layer**: Data transfer objects for API communication

## Key Components

1. **Todo Entity**: Complete JPA entity with timestamps and validation
2. **TodoService**: Business logic with transaction management
3. **TodoController**: RESTful API with comprehensive endpoints
4. **Global Exception Handler**: Centralized error handling
5. **Comprehensive Tests**: Unit tests and integration tests

## API Endpoints

The application provides these endpoints:

- `POST /api/todos` - Create new todo
- `GET /api/todos` - Get all todos
- `GET /api/todos/{id}` - Get todo by ID
- `PUT /api/todos/{id}` - Update todo
- `DELETE /api/todos/{id}` - Delete todo
- `GET /api/todos/status/{status}` - Filter by status
- `GET /api/todos/search?title=...` - Search by title
- `GET /api/todos/overdue` - Get overdue todos
- And many more advanced endpoints

## Getting Started

1. **Setup**: Copy the `pom.xml` and create the directory structure
2. **Install**: Run `mvn clean install`
3. **Run**: Execute `mvn spring-boot:run`
4. **Test**: Use the provided cURL commands or Postman

## Production Ready

The application includes:

- Production configuration
- Docker support
- Security considerations
- Performance optimization tips
- Monitoring and logging
- Comprehensive documentation

### [License](./APACHE.md)

```
Copyright 2025 MikeTeddyOmondi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
