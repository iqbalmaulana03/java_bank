package iqbal.javabank.controller;

import iqbal.javabank.dto.UpdateCustomerRequest;
import iqbal.javabank.dto.response.CustomerResponse;
import iqbal.javabank.dto.response.WebResponse;
import iqbal.javabank.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void update() {

        String customerId = "123";
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setFirstName("Updated Name");
        request.setLastName("Updated Name Last");
        request.setPhoneNumber("087667667766");
        request.setAddress("StMAx");

        // Mocking service response
        CustomerResponse expectedResponse = new CustomerResponse();
        expectedResponse.setId(customerId);
        expectedResponse.setFirstName("Updated Name");
        expectedResponse.setLastName("Updated Name Last");
        expectedResponse.setPhoneNumber("087667667766");
        expectedResponse.setAddress("StMAx");

        when(customerService.update(request)).thenReturn(expectedResponse);

        // Testing controller method
        ResponseEntity<WebResponse<CustomerResponse>> responseEntity = customerController.update(customerId, request);

        // Verifying if the service method is called with correct arguments
        verify(customerService, times(1)).update(request);

        // Verifying response status and content
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        WebResponse<CustomerResponse> responseBody = responseEntity.getBody();
        assertEquals("successfully update customer", Objects.requireNonNull(responseBody).getMessage());
        assertEquals(expectedResponse, responseBody.getData());
    }

    @Test
    void get() {

        String customerId = "123";

        // Mocking service response
        CustomerResponse expectedResponse = new CustomerResponse();
        expectedResponse.setId(customerId);
        expectedResponse.setFirstName("Updated Name");
        expectedResponse.setLastName("Updated Name Last");
        expectedResponse.setPhoneNumber("087667667766");
        expectedResponse.setAddress("StMAx");

        when(customerService.get(customerId)).thenReturn(expectedResponse);

        // Testing controller method
        ResponseEntity<WebResponse<CustomerResponse>> responseEntity = customerController.get(customerId);

        // Verifying if the service method is called with correct argument
        verify(customerService, times(1)).get(customerId);

        // Verifying response status and content
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        WebResponse<CustomerResponse> responseBody = responseEntity.getBody();
        assertEquals("successfully get customer by id", Objects.requireNonNull(responseBody).getMessage());
        assertEquals(expectedResponse, responseBody.getData());
    }

    @Test
    void delete() {

        String customerId = "123";

        ResponseEntity<WebResponse<String>> responseEntity = customerController.delete(customerId);

        verify(customerService, times(1)).delete(customerId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        WebResponse<String> responseBody = responseEntity.getBody();
        assertEquals("successfully delete customer", Objects.requireNonNull(responseBody).getMessage());
        assertEquals("OK", responseBody.getData());
    }
}