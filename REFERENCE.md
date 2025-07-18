# Spring Boot Todo Application with libsql

A complete RESTful Todo application built with Spring Boot 3.2, JPA, and libsql database support.

## Features

- ✅ Create, Read, Update, Delete (CRUD) operations for todos
- ✅ RESTful API endpoints
- ✅ Input validation
- ✅ Exception handling
- ✅ libsql/SQLite database integration
- ✅ Comprehensive documentation
- ✅ Unit tests included

## Prerequisites

- Java 24 or higher
- Maven 3.6+
- libsql server (sqld) or local SQLite

## Project Structure

```
todo-app/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── todoapp/
│   │   │               ├── TodoAppApplication.java
│   │   │               ├── controller/
│   │   │               │   ├── TodoController.java
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               ├── model/
│   │   │               │   ├── Todo.java
│   │   │               │   └── TodoStatus.java
│   │   │               ├── repository/
│   │   │               │   └── TodoRepository.java
│   │   │               ├── service/
│   │   │               │   ├── TodoService.java
│   │   │               │   └── TodoServiceImpl.java
│   │   │               └── dto/
│   │   │                   ├── TodoRequestDTO.java
│   │   │                   └── TodoResponseDTO.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── data.sql
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── todoapp/
│                       ├── TodoAppApplicationTests.java
│                       ├── controller/
│                       │   └── TodoControllerTest.java
│                       └── service/
│                           └── TodoServiceTest.java
```

## Installation & Setup

### 1. Clone and Setup Project

```bash
# Create project directory
mkdir todo-app
cd todo-app

# Copy the pom.xml file provided earlier
# Create directory structure
mkdir -p src/main/java/com/example/todoapp/{controller,model,repository,service,dto}
mkdir -p src/main/resources
mkdir -p src/test/java/com/example/todoapp/{controller,service}
```

### 2. Install Dependencies

```bash
mvn clean install
```

### 3. Database Configuration

#### For Local SQLite (Development)
The application is configured to use a local SQLite file by default. No additional setup required.

#### For Remote libsql Server (Production)
Update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:sqlite:https://your-libsql-server.com/your-database
    # Add authentication if required
    # username: your-username
    # password: your-token
```

## Source Code

### Main Application Class

```java
// src/main/java/com/example/todoapp/TodoAppApplication.java
package com.example.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
    }
}
```

### Model Classes

```java
// src/main/java/com/example/todoapp/model/TodoStatus.java
package com.example.todoapp.model;

public enum TodoStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
```

```java
// src/main/java/com/example/todoapp/model/Todo.java
package com.example.todoapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
public class Todo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    @Column(nullable = false)
    private String title;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status = TodoStatus.PENDING;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Todo() {}
    
    public Todo(String title, String description, TodoStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public TodoStatus getStatus() { return status; }
    public void setStatus(TodoStatus status) { this.status = status; }
    
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", dueDate=" + dueDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
```

### DTOs (Data Transfer Objects)

```java
// src/main/java/com/example/todoapp/dto/TodoRequestDTO.java
package com.example.todoapp.dto;

import com.example.todoapp.model.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class TodoRequestDTO {
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    private TodoStatus status;
    private LocalDateTime dueDate;
    
    // Constructors
    public TodoRequestDTO() {}
    
    public TodoRequestDTO(String title, String description, TodoStatus status, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public TodoStatus getStatus() { return status; }
    public void setStatus(TodoStatus status) { this.status = status; }
    
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
}
```

```java
// src/main/java/com/example/todoapp/dto/TodoResponseDTO.java
package com.example.todoapp.dto;

import com.example.todoapp.model.Todo;
import com.example.todoapp.model.TodoStatus;

import java.time.LocalDateTime;

public class TodoResponseDTO {
    
    private Long id;
    private String title;
    private String description;
    private TodoStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public TodoResponseDTO() {}
    
    public TodoResponseDTO(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.status = todo.getStatus();
        this.dueDate = todo.getDueDate();
        this.createdAt = todo.getCreatedAt();
        this.updatedAt = todo.getUpdatedAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public TodoStatus getStatus() { return status; }
    public void setStatus(TodoStatus status) { this.status = status; }
    
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
```

### Repository Layer

```java
// src/main/java/com/example/todoapp/repository/TodoRepository.java
package com.example.todoapp.repository;

import com.example.todoapp.model.Todo;
import com.example.todoapp.model.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    // Find todos by status
    List<Todo> findByStatus(TodoStatus status);
    
    // Find todos by title containing (case-insensitive)
    List<Todo> findByTitleContainingIgnoreCase(String title);
    
    // Find todos due before a specific date
    List<Todo> findByDueDateBefore(LocalDateTime date);
    
    // Find todos due after a specific date
    List<Todo> findByDueDateAfter(LocalDateTime date);
    
    // Find todos created between dates
    List<Todo> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Custom query to find overdue todos
    @Query("SELECT t FROM Todo t WHERE t.dueDate < :currentDate AND t.status != :completedStatus")
    List<Todo> findOverdueTodos(@Param("currentDate") LocalDateTime currentDate, 
                               @Param("completedStatus") TodoStatus completedStatus);
    
    // Count todos by status
    long countByStatus(TodoStatus status);
    
    // Delete completed todos older than specified date
    void deleteByStatusAndUpdatedAtBefore(TodoStatus status, LocalDateTime date);
}
```

### Service Layer

```java
// src/main/java/com/example/todoapp/service/TodoService.java
package com.example.todoapp.service;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.dto.TodoResponseDTO;
import com.example.todoapp.model.TodoStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoService {
    
    TodoResponseDTO createTodo(TodoRequestDTO todoRequest);
    
    TodoResponseDTO getTodoById(Long id);
    
    List<TodoResponseDTO> getAllTodos();
    
    TodoResponseDTO updateTodo(Long id, TodoRequestDTO todoRequest);
    
    void deleteTodo(Long id);
    
    List<TodoResponseDTO> getTodosByStatus(TodoStatus status);
    
    List<TodoResponseDTO> searchTodosByTitle(String title);
    
    List<TodoResponseDTO> getOverdueTodos();
    
    List<TodoResponseDTO> getTodosDueAfter(LocalDateTime date);
    
    List<TodoResponseDTO> getTodosCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    long countTodosByStatus(TodoStatus status);
    
    void deleteCompletedTodosOlderThan(LocalDateTime date);
}
```

```java
// src/main/java/com/example/todoapp/service/TodoServiceImpl.java
package com.example.todoapp.service;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.dto.TodoResponseDTO;
import com.example.todoapp.model.Todo;
import com.example.todoapp.model.TodoStatus;
import com.example.todoapp.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {
    
    private final TodoRepository todoRepository;
    
    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    
    @Override
    public TodoResponseDTO createTodo(TodoRequestDTO todoRequest) {
        Todo todo = new Todo();
        todo.setTitle(todoRequest.getTitle());
        todo.setDescription(todoRequest.getDescription());
        todo.setStatus(todoRequest.getStatus() != null ? todoRequest.getStatus() : TodoStatus.PENDING);
        todo.setDueDate(todoRequest.getDueDate());
        
        Todo savedTodo = todoRepository.save(todo);
        return new TodoResponseDTO(savedTodo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TodoResponseDTO getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
        return new TodoResponseDTO(todo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TodoResponseDTO> getAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        return todos.stream()
                .map(TodoResponseDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    public TodoResponseDTO updateTodo(Long id, TodoRequestDTO todoRequest) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
        
        existingTodo.setTitle(todoRequest.getTitle());
        existingTodo.setDescription(todoRequest.getDescription());
        if (todoRequest.getStatus() != null) {
            existingTodo.setStatus(todoRequest.getStatus());
        }
        existingTodo.setDueDate(todoRequest.getDueDate());
        
        Todo updatedTodo = todoRepository.save(existingTodo);
        return new TodoResponseDTO(updatedTodo);
    }
    
    @Override
    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new EntityNotFoundException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TodoResponseDTO> getTodosByStatus(TodoStatus status) {
        List<Todo> todos = todoRepository.findByStatus(status);
        return todos.stream()
                .map(TodoResponseDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TodoResponseDTO> searchTodosByTitle(String title) {
        List<Todo> todos = todoRepository.findByTitleContainingIgnoreCase(title);
        return todos.stream()
                .map(TodoResponseDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TodoResponseDTO> getOverdueTodos() {
        List<Todo> todos = todoRepository.findOverdueTodos(LocalDateTime.now(), TodoStatus.COMPLETED);
        return todos.stream()
                .map(TodoResponseDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TodoResponseDTO> getTodosDueAfter(LocalDateTime date) {
        List<Todo> todos = todoRepository.findByDueDateAfter(date);
        return todos.stream()
                .map(TodoResponseDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TodoResponseDTO> getTodosCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        List<Todo> todos = todoRepository.findByCreatedAtBetween(startDate, endDate);
        return todos.stream()
                .map(TodoResponseDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countTodosByStatus(TodoStatus status) {
        return todoRepository.countByStatus(status);
    }
    
    @Override
    public void deleteCompletedTodosOlderThan(LocalDateTime date) {
        todoRepository.deleteByStatusAndUpdatedAtBefore(TodoStatus.COMPLETED, date);
    }
}
```

### Controller Layer

```java
// src/main/java/com/example/todoapp/controller/TodoController.java
package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.dto.TodoResponseDTO;
import com.example.todoapp.model.TodoStatus;
import com.example.todoapp.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TodoController {
    
    private final TodoService todoService;
    
    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    @PostMapping
    public ResponseEntity<TodoResponseDTO> createTodo(@Valid @RequestBody TodoRequestDTO todoRequest) {
        TodoResponseDTO createdTodo = todoService.createTodo(todoRequest);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> getTodoById(@PathVariable Long id) {
        TodoResponseDTO todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }
    
    @GetMapping
    public ResponseEntity<List<TodoResponseDTO>> getAllTodos() {
        List<TodoResponseDTO> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> updateTodo(@PathVariable Long id, 
                                                     @Valid @RequestBody TodoRequestDTO todoRequest) {
        TodoResponseDTO updatedTodo = todoService.updateTodo(id, todoRequest);
        return ResponseEntity.ok(updatedTodo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TodoResponseDTO>> getTodosByStatus(@PathVariable TodoStatus status) {
        List<TodoResponseDTO> todos = todoService.getTodosByStatus(status);
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<TodoResponseDTO>> searchTodosByTitle(@RequestParam String title) {
        List<TodoResponseDTO> todos = todoService.searchTodosByTitle(title);
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<TodoResponseDTO>> getOverdueTodos() {
        List<TodoResponseDTO> todos = todoService.getOverdueTodos();
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/due-after")
    public ResponseEntity<List<TodoResponseDTO>> getTodosDueAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<TodoResponseDTO> todos = todoService.getTodosDueAfter(date);
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/created-between")
    public ResponseEntity<List<TodoResponseDTO>> getTodosCreatedBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TodoResponseDTO> todos = todoService.getTodosCreatedBetween(startDate, endDate);
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/count/{status}")
    public ResponseEntity<Map<String, Long>> countTodosByStatus(@PathVariable TodoStatus status) {
        long count = todoService.countTodosByStatus(status);
        return ResponseEntity.ok(Map.of("count", count));
    }
    
    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> deleteCompletedTodosOlderThan(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        todoService.deleteCompletedTodosOlderThan(date);
        return ResponseEntity.noContent().build();
    }
}
```

```java
// src/main/java/com/example/todoapp/controller/GlobalExceptionHandler.java
package com.example.todoapp.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("message", "Invalid input data");
        response.put("errors", errors);
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

## Sample Data

Create `src/main/resources/data.sql` for sample data:

```sql
-- Sample data for testing
INSERT INTO todos (title, description, status, due_date, created_at, updated_at) VALUES
('Learn Spring Boot', 'Complete Spring Boot tutorial and build a todo app', 'IN_PROGRESS', '2024-12-31 23:59:59', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Setup Database', 'Configure libsql database for the application', 'COMPLETED', '2024-12-15 12:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Write Tests', 'Create unit tests for all service methods', 'PENDING', '2024-12-25 18:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Deploy Application', 'Deploy the application to production', 'PENDING', '2025-01-15 10:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

## Running the Application

### Development Mode

```bash
# Run with Maven
mvn spring-boot:run

# Or run with Java
mvn clean package
java -jar target/todo-app-1.0.0.jar
```

### Production Mode

```bash
# Build JAR
mvn clean package -DskipTests

# Run with production profile
java -jar target/todo-app-1.0.0.jar --spring.profiles.active=prod
```

## API Documentation

### Base URL
```
http://localhost:8080/api/todos
```

### Endpoints

#### Create Todo
```http
POST /api/todos
Content-Type: application/json

{
  "title": "Learn Spring Boot",
  "description": "Complete Spring Boot tutorial",
  "status": "PENDING",
  "dueDate": "2024-12-31T23:59:59"
}
```

#### Get All Todos
```http
GET /api/todos
```

#### Get Todo by ID
```http
GET /api/todos/{id}
```

#### Update Todo
```http
PUT /api/todos/{id}
Content-Type: application/json

{
  "title": "Updated Title",
  "description": "Updated description",
  "status": "COMPLETED"
}
```

#### Delete Todo
```http
DELETE /api/todos/{id}
```

#### Get Todos by Status
```http
GET /api/todos/status/{status}
# status: PENDING, IN_PROGRESS, COMPLETED, CANCELLED
```

#### Search Todos by Title
```http
GET /api/todos/search?title=spring
```

#### Get Overdue Todos
```http
GET /api/todos/overdue
```

#### Get Todos Due After Date
```http
GET /api/todos/due-after?date=2024-12-31T00:00:00
```

#### Get Todos Created Between Dates
```http
GET /api/todos/created-between?startDate=2024-12-01T00:00:00&endDate=2024-12-31T23:59:59
```

#### Count Todos by Status
```http
GET /api/todos/count/{status}
```

#### Clean Up Completed Todos
```http
DELETE /api/todos/cleanup?date=2024-12-01T00:00:00
```

## Testing

### Unit Tests

```java
// src/test/java/com/example/todoapp/service/TodoServiceTest.java
package com.example.todoapp.service;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.dto.TodoResponseDTO;
import com.example.todoapp.model.Todo;
import com.example.todoapp.model.TodoStatus;
import com.example.todoapp.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    
    @Mock
    private TodoRepository todoRepository;
    
    @InjectMocks
    private TodoServiceImpl todoService;
    
    private Todo sampleTodo;
    private TodoRequestDTO sampleRequest;
    
    @BeforeEach
    void setUp() {
        sampleTodo = new Todo();
        sampleTodo.setId(1L);
        sampleTodo.setTitle("Test Todo");
        sampleTodo.setDescription("Test Description");
        sampleTodo.setStatus(TodoStatus.PENDING);
        sampleTodo.setCreatedAt(LocalDateTime.now());
        sampleTodo.setUpdatedAt(LocalDateTime.now());
        
        sampleRequest = new TodoRequestDTO();
        sampleRequest.setTitle("Test Todo");
        sampleRequest.setDescription("Test Description");
        sampleRequest.setStatus(TodoStatus.PENDING);
    }
    
    @Test
    void createTodo_Success() {
        // Given
        when(todoRepository.save(any(Todo.class))).thenReturn(sampleTodo);
        
        // When
        TodoResponseDTO result = todoService.createTodo(sampleRequest);
        
        // Then
        assertNotNull(result);
        assertEquals(sampleTodo.getTitle(), result.getTitle());
        assertEquals(sampleTodo.getDescription(), result.getDescription());
        assertEquals(sampleTodo.getStatus(), result.getStatus());
        verify(todoRepository).save(any(Todo.class));
    }
    
    @Test
    void getTodoById_Success() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));
        
        // When
        TodoResponseDTO result = todoService.getTodoById(1L);
        
        // Then
        assertNotNull(result);
        assertEquals(sampleTodo.getId(), result.getId());
        assertEquals(sampleTodo.getTitle(), result.getTitle());
        verify(todoRepository).findById(1L);
    }
    
    @Test
    void getTodoById_NotFound() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> todoService.getTodoById(1L));
        verify(todoRepository).findById(1L);
    }
    
    @Test
    void getAllTodos_Success() {
        // Given
        List<Todo> todos = Arrays.asList(sampleTodo);
        when(todoRepository.findAll()).thenReturn(todos);
        
        // When
        List<TodoResponseDTO> result = todoService.getAllTodos();
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleTodo.getTitle(), result.get(0).getTitle());
        verify(todoRepository).findAll();
    }
    
    @Test
    void updateTodo_Success() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(sampleTodo);
        
        TodoRequestDTO updateRequest = new TodoRequestDTO();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description");
        updateRequest.setStatus(TodoStatus.COMPLETED);
        
        // When
        TodoResponseDTO result = todoService.updateTodo(1L, updateRequest);
        
        // Then
        assertNotNull(result);
        verify(todoRepository).findById(1L);
        verify(todoRepository).save(any(Todo.class));
    }
    
    @Test
    void deleteTodo_Success() {
        // Given
        when(todoRepository.existsById(1L)).thenReturn(true);
        
        // When
        todoService.deleteTodo(1L);
        
        // Then
        verify(todoRepository).existsById(1L);
        verify(todoRepository).deleteById(1L);
    }
    
    @Test
    void deleteTodo_NotFound() {
        // Given
        when(todoRepository.existsById(anyLong())).thenReturn(false);
        
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> todoService.deleteTodo(1L));
        verify(todoRepository).existsById(1L);
        verify(todoRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void getTodosByStatus_Success() {
        // Given
        List<Todo> todos = Arrays.asList(sampleTodo);
        when(todoRepository.findByStatus(TodoStatus.PENDING)).thenReturn(todos);
        
        // When
        List<TodoResponseDTO> result = todoService.getTodosByStatus(TodoStatus.PENDING);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TodoStatus.PENDING, result.get(0).getStatus());
        verify(todoRepository).findByStatus(TodoStatus.PENDING);
    }
}
```

### Integration Tests

```java
// src/test/java/com/example/todoapp/controller/TodoControllerTest.java
package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.dto.TodoResponseDTO;
import com.example.todoapp.model.TodoStatus;
import com.example.todoapp.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TodoService todoService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private TodoRequestDTO sampleRequest;
    private TodoResponseDTO sampleResponse;
    
    @BeforeEach
    void setUp() {
        sampleRequest = new TodoRequestDTO();
        sampleRequest.setTitle("Test Todo");
        sampleRequest.setDescription("Test Description");
        sampleRequest.setStatus(TodoStatus.PENDING);
        
        sampleResponse = new TodoResponseDTO();
        sampleResponse.setId(1L);
        sampleResponse.setTitle("Test Todo");
        sampleResponse.setDescription("Test Description");
        sampleResponse.setStatus(TodoStatus.PENDING);
        sampleResponse.setCreatedAt(LocalDateTime.now());
        sampleResponse.setUpdatedAt(LocalDateTime.now());
    }
    
    @Test
    void createTodo_Success() throws Exception {
        // Given
        when(todoService.createTodo(any(TodoRequestDTO.class))).thenReturn(sampleResponse);
        
        // When & Then
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
    
    @Test
    void createTodo_ValidationError() throws Exception {
        // Given
        TodoRequestDTO invalidRequest = new TodoRequestDTO();
        invalidRequest.setTitle(""); // Invalid empty title
        invalidRequest.setDescription("Test Description");
        
        // When & Then
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }
    
    @Test
    void getTodoById_Success() throws Exception {
        // Given
        when(todoService.getTodoById(1L)).thenReturn(sampleResponse);
        
        // When & Then
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Todo"));
    }
    
    @Test
    void getTodoById_NotFound() throws Exception {
        // Given
        when(todoService.getTodoById(1L)).thenThrow(new EntityNotFoundException("Todo not found"));
        
        // When & Then
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
    
    @Test
    void getAllTodos_Success() throws Exception {
        // Given
        List<TodoResponseDTO> todos = Arrays.asList(sampleResponse);
        when(todoService.getAllTodos()).thenReturn(todos);
        
        // When & Then
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Todo"));
    }
    
    @Test
    void updateTodo_Success() throws Exception {
        // Given
        when(todoService.updateTodo(eq(1L), any(TodoRequestDTO.class))).thenReturn(sampleResponse);
        
        // When & Then
        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Todo"));
    }
    
    @Test
    void deleteTodo_Success() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deleteTodo_NotFound() throws Exception {
        // Given
        doThrow(new EntityNotFoundException("Todo not found")).when(todoService).deleteTodo(1L);
        
        // When & Then
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getTodosByStatus_Success() throws Exception {
        // Given
        List<TodoResponseDTO> todos = Arrays.asList(sampleResponse);
        when(todoService.getTodosByStatus(TodoStatus.PENDING)).thenReturn(todos);
        
        // When & Then
        mockMvc.perform(get("/api/todos/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}
```

### Application Test

```java
// src/test/java/com/example/todoapp/TodoAppApplicationTests.java
package com.example.todoapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TodoAppApplicationTests {
    
    @Test
    void contextLoads() {
        // This test ensures that the Spring application context loads successfully
    }
}
```

## Testing with cURL

### Create a Todo
```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Complete Spring Boot tutorial and build a todo app",
    "status": "PENDING",
    "dueDate": "2024-12-31T23:59:59"
  }'
```

### Get All Todos
```bash
curl -X GET http://localhost:8080/api/todos
```

### Get Todo by ID
```bash
curl -X GET http://localhost:8080/api/todos/1
```

### Update Todo
```bash
curl -X PUT http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot - Updated",
    "description": "Complete Spring Boot tutorial and build a todo app with advanced features",
    "status": "IN_PROGRESS"
  }'
```

### Delete Todo
```bash
curl -X DELETE http://localhost:8080/api/todos/1
```

### Search Todos
```bash
curl -X GET "http://localhost:8080/api/todos/search?title=spring"
```

### Get Overdue Todos
```bash
curl -X GET http://localhost:8080/api/todos/overdue
```

## Production Configuration

### Production application.yml
```yaml
# src/main/resources/application-prod.yml
spring:
  datasource:
    url: jdbc:sqlite:https://your-libsql-server.com/your-database
    driver-class-name: org.sqlite.JDBC
    username: ${DATABASE_USERNAME:}
    password: ${DATABASE_PASSWORD:}
  
  jpa:
    hibernate:
      ddl-auto: validate
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    show-sql: false
    properties:
      hibernate:
        format_sql: false
  
  application:
    name: todo-app

server:
  port: ${PORT:8080}
  servlet:
    context-path: /
  compression:
    enabled: true

logging:
  level:
    org.hibernate.SQL: WARN
    org.hibernate.type.descriptor.sql.BasicBinder: WARN
    com.example.todoapp: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/todo-app.log

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: when-authorized
```

## Docker Support

### Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/todo-app-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml
```yaml
version: '3.8'

services:
  todo-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DATABASE_USERNAME=${DATABASE_USERNAME}
      - DATABASE_PASSWORD=${DATABASE_PASSWORD}
    volumes:
      - ./logs:/app/logs
    restart: unless-stopped
```

## Performance Optimization

### Database Indexing
```sql
-- Add indexes for better query performance
CREATE INDEX idx_todos_status ON todos(status);
CREATE INDEX idx_todos_due_date ON todos(due_date);
CREATE INDEX idx_todos_created_at ON todos(created_at);
CREATE INDEX idx_todos_title ON todos(title);
```

### Caching Configuration
Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

Enable caching in main application class:
```java
@EnableCaching
@SpringBootApplication
public class TodoAppApplication {
    // ...
}
```

## Security Considerations

### Add Spring Security (Optional)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Input Validation
- All DTOs include validation annotations
- Global exception handler manages validation errors
- SQL injection prevention through JPA

### Rate Limiting
Consider implementing rate limiting for production use:
```xml
<dependency>
    <groupId>com.github.vladimir-bukhtoyarov</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>
```

## Deployment

### Build for Production
```bash
mvn clean package -DskipTests
```

### Environment Variables
```bash
export DATABASE_USERNAME=your_username
export DATABASE_PASSWORD=your_password
export SPRING_PROFILES_ACTIVE=prod
```

### Run Production Build
```bash
java -jar target/todo-app-1.0.0.jar
```

## Monitoring and Logging

### Health Check Endpoint
```bash
curl http://localhost:8080/actuator/health
```

### Application Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Check libsql server URL
   - Verify network connectivity
   - Check authentication credentials

2. **Port Already in Use**
   ```bash
   # Change port in application.yml
   server:
     port: 8081
   ```

3. **Maven Build Issues**
   ```bash
   mvn clean install -U
   ```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Run tests: `mvn test`
6. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For issues and questions:
- Check the troubleshooting section
- Review the API documentation
- Create an issue in the project repository