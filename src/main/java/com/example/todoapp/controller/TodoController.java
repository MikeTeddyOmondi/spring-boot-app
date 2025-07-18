package com.example.todoapp.controller;

import com.example.todoapp.dtos.TodoRequestDTO;
import com.example.todoapp.dtos.TodoResponseDTO;
import com.example.todoapp.models.TodoStatus;
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
