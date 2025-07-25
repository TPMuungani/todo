package com.tmuungani.todo.service.task;

import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.dto.task.TaskRequest;
import com.tmuungani.todo.dto.task.TaskResponse;
import com.tmuungani.todo.dto.task.UpdateTaskRequest;
import java.util.List;

public interface TaskService {
    ServiceResponse<?> createTask(TaskRequest taskRequest);
    ServiceResponse<?> updateTask(UpdateTaskRequest taskRequest, Long id);
    ServiceResponse<?> deleteTask(Long id);
    ServiceResponse<List<TaskResponse>> getTasksByDepartment(String department);
    ServiceResponse<List<TaskResponse>> getAllTasksAssignedToMe();
    ServiceResponse<List<TaskResponse>> getAllTasksAssignedToACertainEmployee(String employeeUsername);
}
