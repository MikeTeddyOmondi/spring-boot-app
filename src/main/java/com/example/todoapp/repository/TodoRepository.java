package com.example.todoapp.repository;

import com.example.todoapp.models.Todo;
import com.example.todoapp.models.TodoStatus;
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
