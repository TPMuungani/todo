package com.tmuungani.todo.service.employee;

import com.tmuungani.todo.controller.authentication.dto.AuthenticationResponse;
import com.tmuungani.todo.controller.authentication.dto.ChangePasswordRequest;
import com.tmuungani.todo.controller.authentication.dto.LoginRequest;
import com.tmuungani.todo.controller.authentication.dto.RegistrationRequest;
import com.tmuungani.todo.controller.employee.dto.EmployeeDto;
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
