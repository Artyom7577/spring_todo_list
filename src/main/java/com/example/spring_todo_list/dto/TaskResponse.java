package com.example.spring_todo_list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private String id;

    @NotBlank(message = "Task description is required")
    private String taskDescription;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean completed;
    private String statusMessage;
}