package com.tmuungani.todo.service.department;


import com.tmuungani.todo.controller.authentication.dto.AuthenticationResponse;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepartmentControllerIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;

    private final String name = "FINANCE";

    @Test
    @DisplayName("Test get all departments")
    public void testGetAllDepartments(){
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

//        Act
        ResponseEntity<ServiceResponse<List<String>>> response = restTemplate.exchange(
               "/api/v1/all/all-departments",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<ServiceResponse<List<String>>>() {}
        );

        ServiceResponse<List<String>> serviceResponse = response.getBody();


//        Assert
        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Successful Login")
    @Order(1)
    public void testLogin() throws JSONException {
//        Arrange

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "tmuungani");
        jsonObject.put("password", "123456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

//        Act
        ResponseEntity<AuthenticationResponse<Map<String,String>>> response = restTemplate.exchange(
                "/api/v1/all/login",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<AuthenticationResponse<Map<String,String>>>() {}
        );
        AuthenticationResponse<Map<String,String>> authenticationResponse = response.getBody();


//        Assert
        Assertions.assertNotNull(authenticationResponse);
        jwtToken = authenticationResponse.body().get("token");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test register new department.")
    @Order(2)
    public void testRegisterNewDepartment() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

//        Act
        ResponseEntity<ServiceResponse<?>> response = restTemplate.exchange(
                "/api/v1/admin/department/add?name="+name,
                HttpMethod.POST,
                entity,
                new  ParameterizedTypeReference<ServiceResponse<?>>() {}
        );
        ServiceResponse<?> serviceResponse = response.getBody();

//        Assert
        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test Delete Department")
    @Order(3)
    public void testDeleteDepartment() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

//        Act
        ResponseEntity<ServiceResponse<?>> response = restTemplate.exchange(
                "/api/v1/admin/department/delete/"+name,
                HttpMethod.DELETE,
                entity,
                new  ParameterizedTypeReference<ServiceResponse<?>>() {}
        );
        ServiceResponse<?> serviceResponse = response.getBody();

//        Assert
        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
