package com.example.todoapp.service;

import com.example.todoapp.dtos.TodoRequestDTO;
import com.example.todoapp.dtos.TodoResponseDTO;
import com.example.todoapp.models.Todo;
import com.example.todoapp.models.TodoStatus;
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
