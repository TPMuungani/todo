package com.tmuungani.todo.controller.employee;

import com.tmuungani.todo.controller.employee.dto.EmployeeDto;
import com.tmuungani.todo.controller.authentication.dto.RegistrationRequest;
import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Employee {
    private final EmployeeService employeeService;

    @PostMapping("/add-employee")
    public ResponseEntity<ServiceResponse<?>> addEmployee(@RequestBody RegistrationRequest registrationRequest){
        return ResponseEntity.ok(employeeService.createEmployee(registrationRequest));
    }
    @PutMapping("/update-employee/{id}")
    public ResponseEntity<ServiceResponse<?>> updateEmployee(@RequestBody RegistrationRequest registrationRequest, @PathVariable(value = "id") Long id){
        return ResponseEntity.ok(employeeService.updateEmployee(registrationRequest, id));
    }
    @GetMapping("/employees/all")
    public ResponseEntity<ServiceResponse<List<EmployeeDto>>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
    @GetMapping("/employee/{id}")
    public ResponseEntity<ServiceResponse<EmployeeDto>> getEmployeeById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
    @DeleteMapping("/employee/delete/{id}")
    public ResponseEntity<ServiceResponse<?>> deleteEmployee(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

}
