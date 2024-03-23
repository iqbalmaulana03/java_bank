package iqbal.javabank.controller;

import iqbal.javabank.dto.AccountRequest;
import iqbal.javabank.dto.response.AccountResponse;
import iqbal.javabank.dto.response.WebResponse;
import iqbal.javabank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void crate() {

        AccountRequest request = AccountRequest.builder()
                .customerId("123")
                .balance(BigDecimal.valueOf(100000))
                .type("TABUNGAN")
                .build();

        AccountResponse expectedResponse = AccountResponse.builder()
                .customerId("123")
                .balance(BigDecimal.valueOf(100000))
                .type("TABUNGAN")
                .build();

        when(accountService.create(request)).thenReturn(expectedResponse);

        ResponseEntity<WebResponse<AccountResponse>> responseEntity = accountController.crate(request);

        verify(accountService, times(1)).create(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        WebResponse<AccountResponse> responseBody = responseEntity.getBody();
        assertEquals("successfully create account", Objects.requireNonNull(responseBody).getMessage());
        assertEquals(expectedResponse, responseBody.getData());
    }

    @Test
    void get() {
        String accountId = "123";

        AccountResponse expectedResponse = AccountResponse.builder()
                .customerId("123")
                .balance(BigDecimal.valueOf(100000))
                .type("TABUNGAN")
                .build();

        when(accountService.get(accountId)).thenReturn(expectedResponse);

        ResponseEntity<WebResponse<AccountResponse>> responseEntity = accountController.get(accountId);

        verify(accountService, times(1)).get(accountId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        WebResponse<AccountResponse> responseBody = responseEntity.getBody();
        assertEquals("successfully get account by id", Objects.requireNonNull(responseBody).getMessage());
        assertEquals(expectedResponse, responseBody.getData());
    }

    @Test
    void delete() {

        String accountId = "123";

        ResponseEntity<WebResponse<String>> responseEntity = accountController.delete(accountId);

        verify(accountService, times(1)).delete(accountId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        WebResponse<String> responseBody = responseEntity.getBody();
        assertEquals("successfully delete account", Objects.requireNonNull(responseBody).getMessage());
        assertEquals("OK", responseBody.getData());
    }

    @Test
    void getByCustomer() {

        String customerId = "123";

        // Mocking service response
        List<AccountResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(new AccountResponse("1", BigDecimal.valueOf(200000), "TABUNGAN", 12345L, "123", "Acount 1", "Account Last 1", "08777676989"));
        expectedResponse.add(new AccountResponse("2", BigDecimal.valueOf(100000), "GIRO", 6789L, "456", "Acount 2", "Account Last 2", "08777676090"));

        when(accountService.getCustomerId(customerId)).thenReturn(expectedResponse);

        // Testing controller method
        ResponseEntity<WebResponse<List<AccountResponse>>> responseEntity = accountController.getByCustomer(customerId);

        // Verifying if the service method is called with correct argument
        verify(accountService, times(1)).getCustomerId(customerId);

        // Verifying response status and content
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        WebResponse<List<AccountResponse>> responseBody = responseEntity.getBody();
        assertEquals("successfully get account by customer id", Objects.requireNonNull(responseBody).getMessage());
        assertEquals(expectedResponse, responseBody.getData());
    }
}