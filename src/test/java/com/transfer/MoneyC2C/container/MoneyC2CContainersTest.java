package com.transfer.MoneyC2C.container;

import com.transfer.MoneyC2C.exception.InvalidData;
import com.transfer.MoneyC2C.model.Operation;
import com.transfer.MoneyC2C.model.TestModels;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MoneyC2CContainersTest {
    @Autowired
    TestRestTemplate restTemplate;
    public static final int APP_PORT = 5500;

    @Container
    private GenericContainer<?> appContainer = new GenericContainer<>("moneyc2c_money-app")
            .withExposedPorts(APP_PORT);

    TestModels testModels = new TestModels();
    Operation operation = testModels.getOperation();

    @Test
    void appResponse_test_operation_status() {
        int applicationPort = appContainer.getMappedPort(APP_PORT);
        HttpEntity<Operation> entity = new HttpEntity<>(operation);
        System.out.println("BODY " + entity.getBody());
        ResponseEntity<Operation> response = restTemplate.postForEntity("http://localhost:" + applicationPort + "/transfer", entity, Operation.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void appResponse_test_operation_response_not_null() {
        int applicationPort = appContainer.getMappedPort(APP_PORT);
        HttpEntity<Operation> entity = new HttpEntity<>(operation);
        System.out.println("BODY " + entity.getBody());
        ResponseEntity<Operation> response = restTemplate.postForEntity("http://localhost:" + applicationPort + "/transfer", entity, Operation.class);
        assertNotNull(response.getBody());
    }

    @Test
    void appResponse_test_confirm_operation() {
        int applicationPort = appContainer.getMappedPort(APP_PORT);
        HttpEntity<Operation> entity = new HttpEntity<>(operation);
        System.out.println("BODY " + entity.getBody());
        ResponseEntity<Operation> response = restTemplate.postForEntity("http://localhost:" + applicationPort + "/transfer", entity, Operation.class);
        assertNotNull(response.getBody());
    }

    @Test
    void appResponse_test_operation_response_is_correct() {
        int applicationPort = appContainer.getMappedPort(APP_PORT);
        HttpEntity<Operation> entityOne = new HttpEntity<>(operation);
        HttpEntity<Operation> entityTwo = new HttpEntity<>(testModels.getOperation());
        ResponseEntity<Operation> responseOne = restTemplate.postForEntity("http://localhost:" + applicationPort + "/transfer", entityOne, Operation.class);
        ResponseEntity<Operation> responseTwo = restTemplate.postForEntity("http://localhost:" + applicationPort + "/transfer", entityTwo, Operation.class);
        String actualId = responseTwo.getBody().getOperationId();
        int expectedId = Integer.parseInt(responseOne.getBody().getOperationId()) +1;
        assertEquals(String.valueOf(expectedId), actualId);
    }

    //test errors

    @Test
    void appResponse_test_bad_request_error_response_is_correct() {
        int applicationPort = appContainer.getMappedPort(APP_PORT);
        HttpEntity<Operation> entity = new HttpEntity<>(testModels.getCardFromInvalidCVVOperation());
        System.out.println("BODY " + entity.getBody());
        ResponseEntity<InvalidData> response = restTemplate.postForEntity("http://localhost:" + applicationPort + "/transfer", entity, InvalidData.class);
        String actualMessage = response.getBody().getMessage();
        String expectedMessage = "Error input data: Validation failed for argument";
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void appResponse_test_internal_server_error_response_is_correct() {
        int applicationPort = appContainer.getMappedPort(APP_PORT);
        Operation operation = testModels.getCardFromIncorrectCVVOperation();
        HttpEntity<Operation> entity = new HttpEntity<>(operation);
        System.out.println("BODY " + entity.getBody());
        ResponseEntity<InvalidData> response = restTemplate.postForEntity("http://localhost:" + applicationPort + "/transfer", entity, InvalidData.class);
        String actualMessage = response.getBody().getMessage();
        String expectedMessage = "Error input data: Can't transfer money from card " + operation.getCardFromNumber();
        assertEquals(500, response.getStatusCodeValue());
        assertEquals(expectedMessage, actualMessage);
    }
}
