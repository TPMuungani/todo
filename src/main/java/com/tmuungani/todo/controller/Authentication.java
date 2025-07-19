package com.tmuungani.todo.controller;

import com.tmuungani.todo.dto.*;
import com.tmuungani.todo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/all")
@RequiredArgsConstructor
public class Authentication {
    private final EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse<Map<String,String>>> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(employeeService.authenticateEmployee(loginRequest));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse<?>> registerEmployee(@RequestBody RegistrationRequest registrationRequest) throws Exception {
        return ResponseEntity.ok(employeeService.registerEmployee(registrationRequest));
    }
    @PostMapping("/resetpassword/{email}")
    public ResponseEntity<AuthenticationResponse<?>> resetPassword(@PathVariable(value = "email") String email){
        return ResponseEntity.ok(employeeService.resetPassword(email));
    }
    @PostMapping("/changepassword")
    public ResponseEntity<AuthenticationResponse<?>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) throws Exception {
        return ResponseEntity.ok(employeeService.changePassword(changePasswordRequest));
    }

    @GetMapping("/departments")
    public ResponseEntity<ServiceResponse<List<String>>> getAllDepartments(){
        return ResponseEntity.ok(employeeService.getAllEmployeeDepartments());
    }
}
