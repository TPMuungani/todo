package com.tmuungani.todo.controller.task;

import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.dto.task.TaskRequest;
import com.tmuungani.todo.dto.task.TaskResponse;
import com.tmuungani.todo.dto.task.UpdateTaskRequest;
import com.tmuungani.todo.enums.TaskStatus;
import com.tmuungani.todo.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Stream;

@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Task {
    private final TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity<ServiceResponse<?>> addTask(@Valid @RequestBody TaskRequest taskRequest){
        return ResponseEntity.ok(taskService.createTask(taskRequest));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ServiceResponse<?>> updateTask(@Valid @RequestBody UpdateTaskRequest taskRequest, @PathVariable(value = "id") Long id){
        return ResponseEntity.ok(taskService.updateTask(taskRequest, id));
    }
    @GetMapping("/statuses")
    public ResponseEntity<ServiceResponse<List<String>>> getAllTaskStatuses(){
        return ResponseEntity.ok(new ServiceResponse<>(true, "Success", Stream.of(TaskStatus.values()).map(Enum::name).toList()));
    }

    @GetMapping("/by-department")
    public ResponseEntity<ServiceResponse<List<TaskResponse>>> getTasksByDepartment(@RequestParam(value = "department") String departmentName){
        return ResponseEntity.ok(taskService.getTasksByDepartment(departmentName.trim().toUpperCase()));
    }

    @GetMapping("/assigned-to-me")
    public ResponseEntity<ServiceResponse<List<TaskResponse>>> getAllTasksAssignedToMe(){
        return ResponseEntity.ok(taskService.getAllTasksAssignedToMe());
    }

    @GetMapping("/assigned-to-someone/{employee}")
    public ResponseEntity<ServiceResponse<List<TaskResponse>>> getAllTasksAssignedToACertainEmployee(@PathVariable(value = "employee") String employeeUsername){
        return ResponseEntity.ok(taskService.getAllTasksAssignedToACertainEmployee(employeeUsername.trim().toLowerCase()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ServiceResponse<?>> deleteTask(@PathVariable Long id){
        return ResponseEntity.ok(taskService.deleteTask(id));
    }
}
