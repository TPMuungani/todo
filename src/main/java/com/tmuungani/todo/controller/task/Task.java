package com.tmuungani.todo.controller.task;

import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.dto.TaskRequest;
import com.tmuungani.todo.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/all/task")
@RequiredArgsConstructor
@RestController
public class Task {
    private final TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity<ServiceResponse<?>> addTask(@Valid @RequestBody TaskRequest taskRequest){
        return ResponseEntity.ok(taskService.createTask(taskRequest));
    }
}
