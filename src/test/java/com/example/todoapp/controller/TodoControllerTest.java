package com.example.todoapp.controller;

import com.example.todoapp.dtos.TodoRequestDTO;
import com.example.todoapp.dtos.TodoResponseDTO;
import com.example.todoapp.models.TodoStatus;
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
                .andExpect(jsonPath("$.length()").value(1))
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
