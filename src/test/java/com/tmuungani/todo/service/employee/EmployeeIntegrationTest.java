package com.tmuungani.todo.service.employee;

import com.tmuungani.todo.controller.authentication.dto.AuthenticationResponse;
import com.tmuungani.todo.controller.employee.dto.EmployeeDto;
import com.tmuungani.todo.dto.ServiceResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;

    @BeforeEach
    public void setup() throws JSONException {
//        Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "tmuungani");
        jsonObject.put("password", "123456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

//        Act

        ResponseEntity<AuthenticationResponse<Map<String, String>>> response = restTemplate.exchange(
                "/api/v1/all/login",
                HttpMethod.POST,
                entity,
                new  ParameterizedTypeReference<AuthenticationResponse<Map<String, String>>>() {});


        AuthenticationResponse<Map<String, String>> authenticationResponse = response.getBody();

//        Assert
        Assertions.assertNotNull(authenticationResponse);
        jwtToken = authenticationResponse.body().get("token");
    }


    @Test
    @DisplayName("Employee can be successfully be registered.")
    @Order(1)
    public void testRegisterEmployee() throws JSONException {
//        Arrange

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", "Tafadzwa");
        jsonObject.put("lastName", "Muungani");
        jsonObject.put("email", "tm@gmail.com");
        jsonObject.put("password", "123456");
        jsonObject.put("confirmPassword", "123456");
        jsonObject.put("department", "ADMINISTRATION");
        jsonObject.put("cellNumber", "1234567890");
        jsonObject.put("username", "tmuungani");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);

//        Act

        ResponseEntity<ServiceResponse<?>> response = restTemplate.exchange(
                "/api/v1/admin/add-employee",
                HttpMethod.POST,
                request,
                new  ParameterizedTypeReference<ServiceResponse<?>>() {});

        ServiceResponse<?> responseBody = response.getBody();

//        Assert
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Employee can be updated.")
    @Order(2)
    public void testUpdateEmployee() throws JSONException {
//        Arrange

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", "Tafadzwa");
        jsonObject.put("lastName", "Muungani");
        jsonObject.put("email", "tm@gmail.com");
        jsonObject.put("password", "123456");
        jsonObject.put("confirmPassword", "123456");
        jsonObject.put("department", "ADMINISTRATION");
        jsonObject.put("cellNumber", "1234567890");
        jsonObject.put("username", "muunganit");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);

//      Act
        ResponseEntity<ServiceResponse<?>> response = restTemplate.exchange(
                "/api/v1/admin/update-employee/"+2,
                HttpMethod.PUT,
                request,
                new   ParameterizedTypeReference<ServiceResponse<?>>() {}
        );
        ServiceResponse<?> responseBody = response.getBody();

//        Assert
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Get all employees after successful login.")
    @Order(3)
    public void testGetAllEmployeesAfterLogin() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

//        Act
        ResponseEntity<ServiceResponse<List<EmployeeDto>>> response = restTemplate.exchange(
                "/api/v1/admin/employees/all",
                HttpMethod.GET,
                request,
                new   ParameterizedTypeReference<ServiceResponse<List<EmployeeDto>>>() {}
        );
        ServiceResponse<List<EmployeeDto>> responseBody = response.getBody();

//        Assert
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Get employee by id after a successful login by admin.")
    @Order(4)
    public void testGetEmployeeByIdAfterLogin() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
//        Act
        ResponseEntity<ServiceResponse<EmployeeDto>> response = restTemplate.exchange(
                "/api/v1/admin/employee/"+2,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<ServiceResponse<EmployeeDto>>() {}
        );
        ServiceResponse<EmployeeDto> responseBody = response.getBody();

//        Assert
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(2, responseBody.data().id());
    }

    @Test
    @DisplayName("Test successfull delete of an employee.")
    @Order(5)
    public void testDeleteEmployeeById() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

//        Act
        ResponseEntity<ServiceResponse<?>> response = restTemplate.exchange(
                "/api/v1/admin/employee/delete/"+0,
                HttpMethod.DELETE,
                request,
                new    ParameterizedTypeReference<ServiceResponse<?>>() {}
        );
        ServiceResponse<?> responseBody = response.getBody();

//         Assert
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
