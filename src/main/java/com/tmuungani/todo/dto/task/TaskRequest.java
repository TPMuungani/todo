package com.tmuungani.todo.dto.task;

import com.tmuungani.todo.dto.subtask.SubTaskRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TaskRequest(
        @Size(min = 2, max = 100)
        String taskName,
        @Size(min = 2, max = 300)
        String description,
        @Size(min = 2, max = 100)
        String department,
        @NotNull
        List<String> assignedIndividuals, // comma seperated ids in form of a string
        @Size(min = 2, max = 100)
        String assignedEmployee,
        @NotNull
        List<String> sharedDepartments, // comma seperated ids in form of a string
        @NotNull
        List<SubTaskRequest> subTasks
) {
}
