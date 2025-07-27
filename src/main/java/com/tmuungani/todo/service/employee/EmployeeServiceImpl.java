package com.tmuungani.todo.service.employee;

import com.tmuungani.todo.controller.authentication.dto.AuthenticationResponse;
import com.tmuungani.todo.controller.authentication.dto.ChangePasswordRequest;
import com.tmuungani.todo.controller.authentication.dto.LoginRequest;
import com.tmuungani.todo.controller.authentication.dto.RegistrationRequest;
import com.tmuungani.todo.controller.employee.dto.EmployeeDto;
import com.tmuungani.todo.dao.department.DepartmentDao;
import com.tmuungani.todo.dao.employee.EmployeeDao;
import com.tmuungani.todo.dto.*;
import com.tmuungani.todo.model.department.Department;
import com.tmuungani.todo.model.employee.Employee;
import com.tmuungani.todo.security.CurrentAuditor;
import com.tmuungani.todo.security.JwtService;
import com.tmuungani.todo.service.email.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao employeeDao;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentDao departmentDao;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CurrentAuditor currentAuditor;

    @Override
    public ServiceResponse<EmployeeDto> getEmployeeByName(String name) {
        return null;
    }
    @Override
    public AuthenticationResponse<?> registerEmployee(RegistrationRequest registrationRequest) {
        Employee employee = new Employee();
        employee.setFirstName(registrationRequest.firstName());
        employee.setLastName(registrationRequest.lastName());
        employee.setEmail(registrationRequest.email());
        employee.setUsername(registrationRequest.username());
        employee.setPassword(passwordEncoder.encode(registrationRequest.password()));
        Optional<Department> department = departmentDao.findByName(registrationRequest.department());
        if (department.isPresent()) {
            employee.setDepartment(department.get());
        }else {
            return new AuthenticationResponse<>(false, "Department Not Found");
        }
        AuthenticationResponse<String> validate = validateEmployee(registrationRequest);
        if (validate.success()) {
            try {
                employeeDao.save(employee);
                return new AuthenticationResponse<>(true, "Account created successfully.");
            } catch (Exception e) {
                return new AuthenticationResponse<>(false, "Failed to create account " + e);
            }
        }
        return validate;
    }

    @Override
    public AuthenticationResponse<Map<String,String>> authenticateEmployee(LoginRequest loginRequest){
        Map<String, String> x = new HashMap<>();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
        Optional<Employee> employee = employeeDao.findByUsernameAndActiveTrue(loginRequest.username());
        if (employee.isPresent()){
            String token = jwtService.generateToken(employee.get());
            x.put("token", token);
            return new AuthenticationResponse<>(true, "SignIn successful.", x);
        }
        return new AuthenticationResponse<>(false, "Username or password incorrect.");
    }

    @Override
    public ServiceResponse<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employeesData = new ArrayList<>();
        List<Employee> employees = employeeDao.findAllByActiveTrue();
        if (employees.isEmpty()) return new ServiceResponse<>(false, "No active users available.", null);
        employees.forEach(x->employeesData.add(new EmployeeDto(x.getId(),x.getFirstName() + " " +x.getLastName(),x.getDepartment().getName())));
        return new ServiceResponse<>(true,"Success.",employeesData);
    }

    @Override
    public ServiceResponse<EmployeeDto> getEmployeeById(Long id) {
        Optional<Employee> user = employeeDao.findById(id);
        if (user.isPresent()) {
            var x = user.get();
            return new ServiceResponse<>(true, "Success", new EmployeeDto(x.getId(), x.getFirstName()+" " +x.getLastName(),x.getDepartment().getName()));
        }
        return new ServiceResponse<>(false, "User not found by id "+id,  null);
    }

    @Override
    public ServiceResponse<?> deleteEmployee(Long id) {
        Optional<Employee> user = employeeDao.findById(id);
        user.ifPresent(value -> value.setActive(false));
        return new ServiceResponse<>(true, "Success", null);
    }

    @Override
    public ServiceResponse<?> createEmployee(RegistrationRequest registrationRequest) {
        Employee employee = new Employee();
        employee.setFirstName(registrationRequest.firstName());
        employee.setLastName(registrationRequest.lastName());
        employee.setEmail(registrationRequest.email());
        employee.setUsername(registrationRequest.username());
        employee.setDepartment(departmentDao.findByName(registrationRequest.department().toUpperCase().trim()).orElse(null));
        String password = UUID.randomUUID().toString().replace("-", "").substring(0, 10);;
        employee.setPassword(passwordEncoder.encode(password));
        //            Todo to add logic for email sending with password after user is created by admin.
//        credentialNotification(registrationRequest.email(),password);
        AuthenticationResponse<String> validate = validateEmployee(registrationRequest);
        if (validate.success()) {
            try {
                employeeDao.save(employee);
                return new ServiceResponse<>(true, "User created successfully.", null);
            } catch (Exception e) {
                return new ServiceResponse<>(false, "Failed. " + e.getMessage(), null);
            }
        }
        return new ServiceResponse<>(false, validate.message(), null);
    }

    @Override
    public ServiceResponse<?> updateEmployee(RegistrationRequest registrationRequest, Long id) {
        Optional<Employee> existingEmployee = employeeDao.findById(id);
        if (existingEmployee.isPresent()){
            var employee = existingEmployee.get();
            employee.setFirstName(registrationRequest.firstName());
            employee.setLastName(registrationRequest.lastName());
            employee.setEmail(registrationRequest.email());
            employee.setUsername(registrationRequest.username());
            employee.setActive(true);
            employee.setDepartment(departmentDao.findByName(registrationRequest.department().toUpperCase().trim()).orElse(null));
//            AuthenticationResponse<String> validate = validateUser(registrationRequest);
//            if (validate.success()) {
            try {
                employeeDao.save(employee);
                return new ServiceResponse<>(true, "Success.", null);
            } catch (Exception e) {
                return new ServiceResponse<>(false, e.getMessage(), null);
            }
//            }
//            return new ApiResponse<>(false, validate.message());
        }
        return new ServiceResponse<>(false, "Employee does not exist.", null);
    }

    @Override
    public AuthenticationResponse<?> changePassword(ChangePasswordRequest changePasswordRequest) throws Exception {
        Employee employee = currentAuditor.getLoggedInUserOrThrow();
        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), employee.getPassword()))
            return new AuthenticationResponse<>(false, "Old password is incorrect. Try again.");
        if (changePasswordRequest.newPassword().equals(changePasswordRequest.oldPassword()))
            return new AuthenticationResponse<>(false, "You can not use old password as new password.");
        if (!changePasswordRequest.newPassword().equals(changePasswordRequest.newPasswordConfirmation()))
            return new AuthenticationResponse<>(false, "The passwords you entered did not match.");
        employee.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
        employeeDao.save(employee);
        return new AuthenticationResponse<>(true, "Password changed successfully.");
    }

    @Override
    public AuthenticationResponse<?> resetPassword(String email) {
        Optional<Employee> employee = employeeDao.findByUsernameAndActiveTrue(email);
        String password = UUID.randomUUID().toString().replace("-", "").substring(0, 10);;
        if (employee.isPresent()){
            employee.get().setPassword(passwordEncoder.encode(password));
//            Todo to add logic for email sending with new password after reset.
//            credentialNotification(email,password);
            employeeDao.save(employee.get());
            return new AuthenticationResponse<>(true, "Email sent with new password.");
        }
        return new AuthenticationResponse<>(false, "Email does not exist.");
    }

    @Override
    public ServiceResponse<List<String>> getAllEmployeeDepartments() {
        List<String> departments = new ArrayList<>();
        List<Department> departmentList = departmentDao.findAll();
        departmentList.forEach(department -> departments.add(department.getName()));
        return new ServiceResponse<>(true, "Success", departments);
    }

    private AuthenticationResponse<String> validateEmployee(RegistrationRequest registrationRequest) {
        Optional<Employee> employee = employeeDao.findByUsernameAndActiveTrue(registrationRequest.username());
        if (employee.isPresent()) return new AuthenticationResponse<>(false,"Username already exist.");
        Optional<Employee> emailEmployee = employeeDao.findByEmailAndActiveTrue(registrationRequest.email());
        if (emailEmployee.isPresent()) return new AuthenticationResponse<>(false,"Email already exist.");
        if (!registrationRequest.password().equals(registrationRequest.confirmPassword())) return new AuthenticationResponse<>(false,"Password mismatch.");
        if (!EmailValidator.isValidEmail(registrationRequest.email())) return new AuthenticationResponse<>(false,"Invalid email.");
        return new AuthenticationResponse<>(true);
    }

//    private void credentialNotification(String email, String password) {
//        passwordEmailService.sendEmail(email,password);
//    }
}
