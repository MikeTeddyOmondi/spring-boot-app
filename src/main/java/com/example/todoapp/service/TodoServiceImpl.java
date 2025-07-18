package com.example.todoapp.service;

import com.example.todoapp.dtos.TodoRequestDTO;
import com.example.todoapp.dtos.TodoResponseDTO;
import com.example.todoapp.models.Todo;
import com.example.todoapp.models.TodoStatus;
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
