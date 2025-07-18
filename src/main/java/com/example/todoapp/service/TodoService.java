package com.example.todoapp.service;

import com.example.todoapp.dtos.TodoRequestDTO;
import com.example.todoapp.dtos.TodoResponseDTO;
import com.example.todoapp.models.TodoStatus;

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
