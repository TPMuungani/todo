package com.tmuungani.todo.service;

import com.tmuungani.todo.dto.*;
import java.util.List;
import java.util.Map;

public interface EmployeeService {
    ServiceResponse<EmployeeDto> getEmployeeByName(String name);
    AuthenticationResponse<?> registerEmployee(RegistrationRequest registrationRequest) throws Exception;
    AuthenticationResponse<Map<String,String>> authenticateEmployee(LoginRequest loginRequest);

    ServiceResponse<List<EmployeeDto>> getAllEmployees();

    ServiceResponse<EmployeeDto> getEmployeeById(Long id);

    ServiceResponse<?> deleteEmployee(Long id);

    ServiceResponse<?> createEmployee(RegistrationRequest registrationRequest);

    ServiceResponse<?> updateEmployee(RegistrationRequest registrationRequest, Long id);

    AuthenticationResponse<?> changePassword(ChangePasswordRequest changePasswordRequest) throws Exception;

    AuthenticationResponse<?> resetPassword(String email);
    ServiceResponse<List<String>> getAllEmployeeDepartments();
}
