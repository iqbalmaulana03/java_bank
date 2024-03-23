package iqbal.javabank.controller;

import iqbal.javabank.dto.TransactionRequest;
import iqbal.javabank.dto.TransferRequest;
import iqbal.javabank.dto.WithdrawalRequest;
import iqbal.javabank.dto.response.TransactionResponse;
import iqbal.javabank.dto.response.WebResponse;
import iqbal.javabank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void depositor() {

        TransactionRequest request = TransactionRequest.builder()
                .noRek(12345L)
                .amount(BigDecimal.valueOf(20000))
                .build();

        TransactionResponse expectedResponse = TransactionResponse.builder()
                .noRek(12345L)
                .amount(BigDecimal.valueOf(20000))
                .build();

        when(transactionService.deposito(request)).thenReturn(expectedResponse);

        ResponseEntity<WebResponse<TransactionResponse>> responseEntity = transactionController.depositor(request);

        verify(transactionService, times(1)).deposito(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        WebResponse<TransactionResponse> responseBody = responseEntity.getBody();
        assertEquals("successfully deposito", Objects.requireNonNull(responseBody).getMessage());
        assertEquals(expectedResponse, responseBody.getData());
    }

    @Test
    void withdrawal() {

        WithdrawalRequest request = WithdrawalRequest.builder()
                .accountId("123")
                .amount(BigDecimal.valueOf(20000))
                .build();

        TransactionResponse expectedResponse = TransactionResponse.builder()
                .amount(BigDecimal.valueOf(20000))
                .build();

        when(transactionService.withdrawal(request)).thenReturn(expectedResponse);

        ResponseEntity<WebResponse<TransactionResponse>> responseEntity = transactionController.withdrawal(request);

        verify(transactionService, times(1)).withdrawal(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        WebResponse<TransactionResponse> responseBody = responseEntity.getBody();
        assertEquals("successfully withdrawal", Objects.requireNonNull(responseBody).getMessage());
        assertEquals(expectedResponse, responseBody.getData());
    }

    @Test
    void transfer() {

        TransferRequest request = TransferRequest.builder()
                .accountId("123")
                .noRek(12345L)
                .amount(BigDecimal.valueOf(20000))
                .build();

        TransactionResponse expectedResponse = TransactionResponse.builder()
                .noRek(12345L)
                .amount(BigDecimal.valueOf(20000))
                .build();

        when(transactionService.transfer(request)).thenReturn(expectedResponse);

        ResponseEntity<WebResponse<TransactionResponse>> responseEntity = transactionController.transfer(request);

        verify(transactionService, times(1)).transfer(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        WebResponse<TransactionResponse> responseBody = responseEntity.getBody();
        assertEquals("successfully transfer", Objects.requireNonNull(responseBody).getMessage());
        assertEquals(expectedResponse, responseBody.getData());
    }
}