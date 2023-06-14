package com.example.spring_todo_list.controller;


import com.example.spring_todo_list.dto.TaskRequest;
import com.example.spring_todo_list.dto.TaskResponse;
import com.example.spring_todo_list.model.Task;
import com.example.spring_todo_list.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    private TaskRequest taskRequest;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@RequestBody TaskRequest taskRequest) {
        taskService.createTask(taskRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/startDate/{date}")
    public List<TaskResponse> getTasksWithStartDateAfter(@PathVariable("date") String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return taskService.getTasksWithStartDateAfter(date);
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse getTaskById(@PathVariable("taskId") String taskId) throws Exception {
        Task task = taskService.getTaskById(taskId);
        return taskService.mapToTaskResponse(task);
    }

    @GetMapping("/endDate/{date}")
    public List<TaskResponse> getTasksWithEndDateBefore(@PathVariable("date") String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return taskService.getTasksWithEndDateBefore(date);
    }

    @PutMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void doneTask(@PathVariable("taskId") String taskId) throws Exception {
        taskService.doneTask(taskId);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskById(@PathVariable("taskId") String taskId) throws Exception {
        taskService.deleteTaskById(taskId);
    }

    @GetMapping("/completed")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponse> getCompletedTasks() {
        return taskService.getTasksByCompletionStatus(true);
    }

    @GetMapping("/incomplete")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponse> getIncompleteTasks() {
        return taskService.getTasksByCompletionStatus(false);
    }

    @GetMapping("/exists/{taskDescription}")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkTaskDescriptionExists(@PathVariable String taskDescription) {
        return taskService.isTaskDescriptionUnique(taskDescription);
    }


    @GetMapping("/search")
    public List<TaskResponse> searchTasksByDescription(@RequestParam String keyword) {
        return taskService.searchTasksByDescription(keyword);
    }

    @GetMapping("/searchByStatus")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponse> searchTasksByDescriptionAndStatus(
            @RequestParam("completed") boolean completed,
            @RequestParam("keyword") String keyword
    ) {
        return taskService.searchTasksByCompletedAndDescription(completed, keyword);
    }

    @GetMapping("/exists")
    public boolean checkIncompleteTaskExists(@RequestParam String taskDescription) {
        return taskService.existsIncompleteTaskWithDescription(taskDescription);
    }
}
