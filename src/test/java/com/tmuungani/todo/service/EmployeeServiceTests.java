package com.tmuungani.todo.service;

import com.tmuungani.todo.dao.department.DepartmentDao;
import com.tmuungani.todo.dao.employee.EmployeeDao;
import com.tmuungani.todo.controller.authentication.dto.AuthenticationResponse;
import com.tmuungani.todo.controller.employee.dto.EmployeeDto;
import com.tmuungani.todo.controller.authentication.dto.RegistrationRequest;
import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.model.department.Department;
import com.tmuungani.todo.model.employee.Employee;
import com.tmuungani.todo.service.employee.EmployeeServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeServiceTests {

    @Mock
    private EmployeeDao employeeDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DepartmentDao departmentDao;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    @DisplayName("register employee with all valid data")
    @Order(1)
    public void testRegisterEmployee_with_all_data_provided() {
//        Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "Paul",
                "Smith",
                "psmith@gmail.com",
                "12345",
                "12345",
                "ADMINISTRATION",
                "12343",
                "psmith"
        );


        Mockito.when(employeeDao.save(Mockito.any(Employee.class))).thenReturn(new Employee());

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("");

        Mockito.when(departmentDao.findByName(Mockito.anyString())).thenReturn(Optional.of(new Department()));

//        Act
        AuthenticationResponse<?> response = employeeService.registerEmployee(registrationRequest);

//        Assert
        Assertions.assertNotNull(response);
        Mockito.verify(employeeDao, Mockito.times(1)).save(Mockito.any(Employee.class));
    }

    @Test
    @DisplayName("Test Get all active employees")
    @Order(2)
    public void test_get_all_employees() {
//        Arrange
        Mockito.when(employeeDao.findAllByActiveTrue()).thenReturn(new ArrayList<>());

//        Act
        ServiceResponse<List<EmployeeDto>> response = employeeService.getAllEmployees();


//        Assert
        Assertions.assertNotNull(response);
        Mockito.verify(employeeDao, Mockito.times(1)).findAllByActiveTrue();
    }

//    @Test
//    @DisplayName("Test Delete Employee")
//    @Order(3)
//    void testDeleteEmployee() {
////        Arrange
//
//
////        Act & Assert
//        Mockito.doThrow(new RuntimeException("Test Exception")).when(employeeDao).delete(Mockito.any(Employee.class));
//        ServiceResponse<?> response = employeeService.deleteEmployee(1L);
//
////        Assert
//        Assertions.assertNotNull(response);
//        Assertions.assertTrue(response.success());
////        Mockito.verify(employeeDao, Mockito.times(1)).delete(Mockito.any(Employee.class));
//    }
}
