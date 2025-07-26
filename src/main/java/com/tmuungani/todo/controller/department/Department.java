package com.tmuungani.todo.controller.department;

import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.service.department.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Department {
    private final DepartmentService departmentService;

    @PostMapping("/admin/department/add")
    public ResponseEntity<ServiceResponse<?>> addDepartment(String name){
        return ResponseEntity.ok(departmentService.registerDepartment(name));
    }

    @GetMapping("/all/all-departments")
    public ResponseEntity<ServiceResponse<List<String>>> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getAllEmployeeDepartments());
    }

    @DeleteMapping("/admin/department/delete/{name}")
    public ResponseEntity<ServiceResponse<?>> deleteDepartment(@PathVariable(value = "name") String name){
        return ResponseEntity.ok(departmentService.deleteDepartment(name));
    }
}
