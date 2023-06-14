package com.example.spring_todo_list.service;


import com.example.spring_todo_list.dto.TaskRequest;
import com.example.spring_todo_list.dto.TaskResponse;
import com.example.spring_todo_list.model.Task;
import com.example.spring_todo_list.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    public void createTask(TaskRequest taskRequest) {
        validateTaskRequest(taskRequest);

        if (taskRequest.isCompleted()) {
            throw new IllegalArgumentException("Cannot create a task with completed status");
        }

        Task task = Task.builder()
                .taskDescription(taskRequest.getTaskDescription())
                .startDate(taskRequest.getStartDate())
                .endDate(taskRequest.getEndDate())
                .completed(taskRequest.isCompleted())
                .build();

        task.calculateStatusMessage();
        taskRepository.save(task);
        log.info("Task " + task.getId() + " is saved");
    }

    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream().map(this::mapToTaskResponse).toList();
    }

    public TaskResponse mapToTaskResponse(Task task) {
        TaskResponse.TaskResponseBuilder builder = TaskResponse.builder()
                .id(task.getId())
                .taskDescription(task.getTaskDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .completed(task.isCompleted())
                .statusMessage(task.getStatusMessage());

        if (task.isCompleted()) {
            builder.statusMessage("----- Congratulations for doing this Task ----- ");
        } else {
            long daysDifference = ChronoUnit.DAYS.between(task.getStartDate(), task.getEndDate());
            if (daysDifference < 2) {
                builder.statusMessage("You have " + daysDifference + " days left for doing this hurry up!!!");
            } else {
                builder.statusMessage("You have plenty of time to do it");
            }
        }

        return builder.build();
    }

    @Transactional
    public void doneTask(String taskId) throws Exception {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new Exception("Task not found with ID: " + taskId));

        existingTask.setEndDate(LocalDate.now());
        existingTask.setCompleted(true);
        existingTask.setStatusMessage("----- Congratulations for doing this Task ----- ");

        taskRepository.save(existingTask);
        log.info("Task " + taskId + " is updated");
    }

    private void validateTaskRequest(TaskRequest taskRequest) {
        Assert.notNull(taskRequest, "TaskRequest must not be null");
        Assert.hasText(taskRequest.getTaskDescription(), "Task description must not be empty");
        Assert.notNull(taskRequest.getStartDate(), "Start date must not be null");
        Assert.notNull(taskRequest.getEndDate(), "End date must not be null");
        Assert.isTrue(taskRequest.getEndDate().isAfter(taskRequest.getStartDate()), "End date must be after start date");
    }

    public List<TaskResponse> getTasksWithStartDateAfter(LocalDate date) {
        return taskRepository.findByStartDateAfter(date);
    }

    public List<TaskResponse> getTasksWithEndDateBefore(LocalDate date) {
        return taskRepository.findByEndDateBefore(date);
    }

    public List<TaskResponse> getTasksByCompletionStatus(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }

    public boolean isTaskDescriptionUnique(String taskDescription) {
        return !taskRepository.existsByTaskDescription(taskDescription);
    }

    public List<TaskResponse> searchTasksByDescription(String keyword) {
        List<Task> tasks = taskRepository.findByTaskDescriptionContainingIgnoreCase(keyword);
        return tasks.stream().map(this::mapToTaskResponse).collect(Collectors.toList());
    }

    public List<TaskResponse> searchTasksByCompletedAndDescription(boolean completed, String keyword) {
        List<Task> tasks = taskRepository.findByCompletedAndTaskDescriptionContainingIgnoreCase(completed, keyword);
        return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    public boolean existsIncompleteTaskWithDescription(String taskDescription) {
        return taskRepository.existsByTaskDescriptionAndCompletedIsFalse(taskDescription);
    }

    public Task getTaskById(String taskId) throws Exception {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new Exception("Task not found with ID: " + taskId));
    }

    public void deleteTaskById(String taskId) throws Exception {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new Exception("Task not found with ID: " + taskId));

        taskRepository.delete(existingTask);
        log.info("Task " + taskId + " is deleted");
    }
}