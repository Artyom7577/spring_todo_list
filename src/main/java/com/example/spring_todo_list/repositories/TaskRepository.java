package com.example.spring_todo_list.repositories;


import com.example.spring_todo_list.dto.TaskResponse;
import com.example.spring_todo_list.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<TaskResponse> findByStartDateAfter(LocalDate date);
    List<TaskResponse> findByEndDateBefore(LocalDate date);
    List<TaskResponse> findByCompleted(boolean completed);
    boolean existsByTaskDescription(String taskDescription);
    List<Task> findByTaskDescriptionContainingIgnoreCase(String keyword);
    List<Task> findByCompletedAndTaskDescriptionContainingIgnoreCase(boolean completed, String keyword);
    boolean existsByTaskDescriptionAndCompletedIsFalse(String taskDescription);
}


