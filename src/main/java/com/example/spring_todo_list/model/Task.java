package com.example.spring_todo_list.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Document(value = "tasks10")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    private String id;

    @NotBlank(message = "Task description is required")
    private String taskDescription;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;
    private boolean completed;
    private String statusMessage;

    public void calculateStatusMessage() {
        if (startDate != null && endDate != null) {
            long daysDifference = endDate.toEpochDay() - startDate.toEpochDay();
            if (daysDifference < 2) {
                setStatusMessage("You have " + daysDifference + " days left for doing this hurry up!!!");
            } else {
                setStatusMessage("You have plenty of time to do it");
            }
        } else {
            setStatusMessage("No status message available.");
        }
    }
}
