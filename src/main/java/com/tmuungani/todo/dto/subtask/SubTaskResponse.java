package com.tmuungani.todo.dto.subtask;

import java.time.LocalDateTime;

public record SubTaskResponse(
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime dueTime,
        String comment
) {
}
