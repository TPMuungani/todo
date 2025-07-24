package com.tmuungani.todo.service.task;

import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.dto.TaskRequest;

public interface TaskService {
    ServiceResponse<?> createTask(TaskRequest taskRequest);
    ServiceResponse<?> updateTask(TaskRequest taskRequest, Long id);
}
