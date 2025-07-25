package com.tmuungani.todo.dto.task;

import com.tmuungani.todo.dto.subtask.SubTaskResponse;
import java.util.List;

public record TaskResponse(
        Long id,
        String taskName,
        String description,
        String department,
        List<String> assignedIndividuals,
        String assignedEmployee,
        List<String> sharedDepartments,
        List<SubTaskResponse> subTasks
) {
}
