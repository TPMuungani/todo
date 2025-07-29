package com.tmuungani.todo.service.task;

import com.tmuungani.todo.controller.authentication.dto.AuthenticationResponse;
import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.dto.task.TaskResponse;
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
public class TaskIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    private String jwtToken;

    @BeforeEach
    public void setup() throws JSONException {
//        Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","tmuungani");
        jsonObject.put("password", "123456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
//        Act
        ResponseEntity<AuthenticationResponse<Map<String,String>>> response = restTemplate.exchange(
                "/api/v1/all/login",
                HttpMethod.POST,
                entity,
                new  ParameterizedTypeReference<AuthenticationResponse<Map<String,String>>>() {});

        AuthenticationResponse<Map<String,String>> authenticationResponse = response.getBody();

//        Assert
        Assertions.assertNotNull(authenticationResponse);
        jwtToken = authenticationResponse.body().get("token");
    }

    @Test
    @DisplayName("Employee can add task.")
    @Order(1)
    public void employeeCanAddTask() {

//        Arrange
        String curl = """
                {
                  "taskName": "string",
                  "description": "string",
                  "department": "ADMINISTRATION",
                  "assignedIndividuals": [
                    "1"
                  ],
                  "assignedEmployee": "muunganit",
                  "sharedDepartments": [
                    "1"
                  ],
                  "subTasks": [
                    {
                      "name": "string",
                      "description": "string",
                      "startTime": "2025-07-29T14:34:12.190Z",
                      "dueTime": "2025-07-29T14:34:12.190Z",
                      "comment": "string"
                    }
                  ],
                  "dueDate": "2025-07-30"
                }""";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(curl, headers);

//        Act
        ResponseEntity<ServiceResponse<?>>  response = restTemplate.exchange(
                "/api/v1/task/add",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<ServiceResponse<?>>() {}
        );
        ServiceResponse<?> serviceResponse = response.getBody();

//        Assert

        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test employee updating a task's status.")
    @Order(2)
    public void employeeUpdatingTaskStatus() {
//        Arrange

        String curl = """
                {
                  "taskName": "string",
                  "description": "string",
                  "department": "ADMINISTRATION",
                  "assignedIndividuals": [
                    "1","2"
                  ],
                  "assignedEmployee": "tmuungani",
                  "sharedDepartments": [
                    "1","2"
                  ],
                  "subTasks": [
                    {
                      "name": "string",
                      "description": "string",
                      "startTime": "2025-07-29T14:34:12.190Z",
                      "dueTime": "2025-07-29T14:34:12.190Z",
                      "comment": "string"
                    }
                  ],
                  "dueDate": "2025-07-30",
                  "taskStatus": "COMPLETED"
                }""";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(curl, headers);

//        Act
        ResponseEntity<ServiceResponse<?>>  response = restTemplate.exchange(
                "/api/v1/task/update/"+1,
                HttpMethod.PUT,
                entity,
                new  ParameterizedTypeReference<ServiceResponse<?>>() {}
        );
        ServiceResponse<?> serviceResponse = response.getBody();

//        Assert
        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Get all statuses in the system.")
    @Order(3)
    public void getAllTaskStatuses() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

//        Act
        ResponseEntity<ServiceResponse<List<String>>> response = restTemplate.exchange(
                "/api/v1/task/statuses",
                HttpMethod.GET,
                entity,
                new  ParameterizedTypeReference<ServiceResponse<List<String>>>() {}
        );
        ServiceResponse<List<String>> serviceResponse = response.getBody();

//        Assert
        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(4,serviceResponse.data().size());
    }

    @Test
    @DisplayName("Get tasks by department for example administration")
    @Order(4)
    public void getTasksByDepartmentForExampleAdministration() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
//        Act
        ResponseEntity<ServiceResponse<List<TaskResponse>>> response = restTemplate.exchange(
                "/api/v1/task/by-department?department="+"ADMINISTRATION",
                HttpMethod.GET,
                entity,
                new   ParameterizedTypeReference<ServiceResponse<List<TaskResponse>>>() {}
        );

        ServiceResponse<List<TaskResponse>> serviceResponse = response.getBody();

//        Assertions
        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Get employee's own tasks")
    @Order(5)
    public void getEmployeeOwnTasks() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

//        Act
        ResponseEntity<ServiceResponse<List<TaskResponse>>> response = restTemplate.exchange(
                "/api/v1/task/assigned-to-me",
                HttpMethod.GET,
                entity,
                new   ParameterizedTypeReference<ServiceResponse<List<TaskResponse>>>() {}
        );
        ServiceResponse<List<TaskResponse>> serviceResponse = response.getBody();

//        Assert
        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Get other employees tasks once you are logged in.")
    @Order(6)
    public void getOtherEmployeesTasksOnceYouAreLoggedIn() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
//        Act
        ResponseEntity<ServiceResponse<List<TaskResponse>>> response = restTemplate.exchange(
             "/api/v1/task/assigned-to-someone/"+"tmuungani",
             HttpMethod.GET,
             entity,
             new   ParameterizedTypeReference<ServiceResponse<List<TaskResponse>>>() {}
        );
        ServiceResponse<List<TaskResponse>> serviceResponse = response.getBody();

//        Assert
        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test delete a task.")
    @Order(7)
    public void testDeleteTask() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
//        Act
        ResponseEntity<ServiceResponse<?>> response = restTemplate.exchange(
                "/api/v1/task/delete/"+1,
                HttpMethod.DELETE,
                entity,
                new   ParameterizedTypeReference<ServiceResponse<?>>() {}
        );
        ServiceResponse<?> serviceResponse = response.getBody();

//        Assert
        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
