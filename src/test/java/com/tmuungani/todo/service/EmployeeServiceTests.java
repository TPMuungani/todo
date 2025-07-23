package com.tmuungani.todo.service;

import com.tmuungani.todo.dao.DepartmentDao;
import com.tmuungani.todo.dao.EmployeeDao;
import com.tmuungani.todo.dto.AuthenticationResponse;
import com.tmuungani.todo.dto.EmployeeDto;
import com.tmuungani.todo.dto.RegistrationRequest;
import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.model.Employee;
import com.tmuungani.todo.security.CurrentAuditor;
import com.tmuungani.todo.security.JwtService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeDao employeeDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DepartmentDao departmentDao;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private CurrentAuditor currentAuditor;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    public void setup() {
//        employeeService = new EmployeeServiceImpl(employeeDao,
//                passwordEncoder,
//                departmentDao,
//                authenticationManager,
//                jwtService,
//                currentAuditor);
    }

    @Test
    @DisplayName("register employee with all valid data")
    @Order(1)
    void testRegisterEmployee_with_all_data_provided() {
//        Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "Paul",
                "Smith",
                "psmith@gmail.com",
                "12345",
                "12345",
                "Admin",
                "12343"
        );


        Mockito.when(employeeDao.save(Mockito.any(Employee.class))).thenReturn(new Employee());

//        Act
        AuthenticationResponse<?> response = employeeService.registerEmployee(registrationRequest);

//        Assert
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.success());
        Mockito.verify(employeeDao, Mockito.times(1)).save(Mockito.any(Employee.class));
    }

    @Test
    @DisplayName("Test Get all employees")
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
