package com.tmuungani.todo.dto.subtask;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record SubTaskRequest(
        @Size(min = 2, max = 100)
        String name,
        @Size(min = 2, max = 300)
        String description,
        @NotNull
        LocalDateTime startTime,
        @NotNull
        LocalDateTime dueTime,
        @Size(min = 2, max = 100)
        String comment
) {
}
