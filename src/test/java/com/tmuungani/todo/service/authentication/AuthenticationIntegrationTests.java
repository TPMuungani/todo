package com.tmuungani.todo.service.authentication;

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
public class AuthenticationIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    private String email;
    private String jwtToken;

    @Test
    @DisplayName("Employee can be registered.")
    @Order(1)
    public void testRegisterEmployee() throws JSONException {
//        Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", "Tafadzwa");
        jsonObject.put("lastName", "Muungani");
        jsonObject.put("email", "tm@gmail.com");
        jsonObject.put("password", "12345");
        jsonObject.put("confirmPassword", "12345");
        jsonObject.put("department", "ADMINISTRATION");
        jsonObject.put("cellNumber", "123456789");
        jsonObject.put("username", "tmuungani");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);
        email = jsonObject.getString("email");
//        Act

        ResponseEntity<AuthenticationResponse<?>> response = restTemplate.exchange("/api/v1/all/register",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<AuthenticationResponse<?>>() {});

        AuthenticationResponse<?> authenticationResponse = response.getBody();


//        Assert
        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test user login")
    @Order(2)
    public void testUserLogin() throws JSONException {
//        Arrange

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "tmuungani");
        jsonObject.put("password", "123456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);

//        Act
        ResponseEntity<AuthenticationResponse<Map<String,String >>> response = restTemplate.exchange(
                "/api/v1/all/login",
                HttpMethod.POST,
                request,
                new  ParameterizedTypeReference<AuthenticationResponse<Map<String,String >>>() {}
        );
        AuthenticationResponse<Map<String,String>> authenticationResponse = response.getBody();



//        Assert
        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertNotNull(authenticationResponse.body());
        jwtToken = authenticationResponse.body().get("token");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void testEmployeeChangePassword() throws JSONException {

//        Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("oldPassword", "12345");
        jsonObject.put("newPassword", "123456");
        jsonObject.put("newPasswordConfirmation", "123456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);

//        Act

        ResponseEntity<AuthenticationResponse<?>> response = restTemplate.exchange(
                "/api/v1/all/changepassword",
                HttpMethod.POST,
                request,
                new  ParameterizedTypeReference<AuthenticationResponse<?>>() {});
        AuthenticationResponse<?> authenticationResponse = response.getBody();

//        Assert
        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertNotEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertNotEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotEquals(jsonObject.getString("oldPassword"), jsonObject.getString("newPassword"));
        Assertions.assertEquals(jsonObject.getString("newPassword"), jsonObject.getString("newPasswordConfirmation"));
    }

    @Test
    @Order(4)
    public void testEmployeeResetPassword(){
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);


//        Act
        ResponseEntity<AuthenticationResponse<?>> response = restTemplate.exchange(
                "/api/v1/all/resetpassword/"+email,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<AuthenticationResponse<?>>() {
        });

        AuthenticationResponse<?> authenticationResponse = response.getBody();

//        Assert

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(5)
    public void testGetAllSavedDepartments(){
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

//        Act
        ResponseEntity<ServiceResponse<List<String>>> response = restTemplate.exchange(
                "/api/v1/all/departments",
                HttpMethod.GET,
                request,
                new  ParameterizedTypeReference<ServiceResponse<List<String>>>() {}
        );
        ServiceResponse<List<String>> serviceResponse = response.getBody();


//        Assert

        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
