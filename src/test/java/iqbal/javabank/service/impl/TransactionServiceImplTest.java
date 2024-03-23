package iqbal.javabank.service.impl;

import iqbal.javabank.constant.ETransaction;
import iqbal.javabank.dto.TransactionRequest;
import iqbal.javabank.dto.TransferRequest;
import iqbal.javabank.dto.WithdrawalRequest;
import iqbal.javabank.dto.response.TransactionResponse;
import iqbal.javabank.entity.Account;
import iqbal.javabank.entity.Customer;
import iqbal.javabank.entity.Transaction;
import iqbal.javabank.entity.User;
import iqbal.javabank.repository.TransactionRepository;
import iqbal.javabank.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private AccountServiceImpl accountService;

    @Mock
    private TransactionRepository repository;

    @Mock
    private ValidationUtils utils;


    @Test
    void deposito() {

        TransactionRequest request = TransactionRequest.builder()
                .noRek(12345L)
                .amount(BigDecimal.valueOf(1000.00))
                .build();

        User user = User.builder()
                .id("123")
                .build();

        Customer customer = new Customer();
        customer.setId("1");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setAddress("mt ST");
        customer.setPhoneNumber("098878777878");
        customer.setUser(user);

        Account account = Account.builder()
                .id("1")
                .balance(BigDecimal.valueOf(5000.00))
                .customer(customer)
                .build();

        when(accountService.getByNoRek(request.getNoRek())).thenReturn(account);

        doNothing().when(utils).validate(request);

        TransactionResponse deposito = transactionService.deposito(request);

        BigDecimal expectBalance = BigDecimal.valueOf(6000.00);
        assertEquals(expectBalance, account.getBalance());
        verify(accountService).update(account);

        verify(repository).save(any(Transaction.class));

        assertNotNull(deposito);
    }

    @Test
    void withdrawal() {

        WithdrawalRequest request = WithdrawalRequest.builder()
                .accountId("1")
                .amount(BigDecimal.valueOf(1000.00))
                .build();

        User user = User.builder()
                .id("123")
                .build();

        Customer customer = new Customer();
        customer.setId("1");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setAddress("mt ST");
        customer.setPhoneNumber("098878777878");
        customer.setUser(user);

        Account account = Account.builder()
                .id("1")
                .balance(BigDecimal.valueOf(5000.00))
                .customer(customer)
                .build();

        when(accountService.getById(request.getAccountId())).thenReturn(account);

        doNothing().when(utils).validate(request);

        TransactionResponse withdrawal = transactionService.withdrawal(request);

        BigDecimal expectBalance = BigDecimal.valueOf(4000.00);
        assertEquals(expectBalance, account.getBalance());
        verify(accountService).update(account);

        verify(repository).save(any(Transaction.class));

        assertNotNull(withdrawal);
    }

    @Test
    void transfer() {

        TransferRequest request = TransferRequest.builder()
                .accountId("1")
                .noRek(12345L)
                .amount(BigDecimal.valueOf(1000))
                .build();

        User user = User.builder()
                .id("123")
                .build();

        Customer customer = new Customer();
        customer.setId("1");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setAddress("mt ST");
        customer.setPhoneNumber("098878777878");
        customer.setUser(user);

        Account fromAccount = Account.builder()
                .id("1")
                .balance(BigDecimal.valueOf(2000.00))
                .customer(customer)
                .build();

        Account toAccount = Account.builder()
                .noRek(12345L)
                .balance(BigDecimal.valueOf(2000.00))
                .customer(customer)
                .build();

        when(accountService.getById(request.getAccountId())).thenReturn(fromAccount);
        when(accountService.getByNoRek(request.getNoRek())).thenReturn(toAccount);

        TransactionResponse response = transactionService.transfer(request);

        // Assert
        assertNotNull(response);
        assertEquals(request.getAmount(), response.getAmount());
        assertEquals(ETransaction.TRANSFER.toString(), response.getTypes());

        // Verify interactions
        verify(accountService, times(2)).update(any());
        verify(repository, times(2)).saveAndFlush(any());
    }

    @Test
    void transfer_InsufficientBalance_ThrowsException() {
        // Arrange
        TransferRequest request = TransferRequest.builder()
                .accountId("1")
                .noRek(12345L)
                .amount(BigDecimal.valueOf(1000))
                .build();

        User user = User.builder()
                .id("123")
                .build();

        Customer customer = new Customer();
        customer.setId("1");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setAddress("mt ST");
        customer.setPhoneNumber("098878777878");
        customer.setUser(user);

        Account fromAccount = Account.builder()
                .id("1")
                .balance(BigDecimal.valueOf(500.00))
                .customer(customer)
                .build();

        Account toAccount = Account.builder()
                .noRek(12345L)
                .balance(BigDecimal.valueOf(2000.00))
                .customer(customer)
                .build();

        when(accountService.getById(request.getAccountId())).thenReturn(fromAccount);
        when(accountService.getByNoRek(request.getNoRek())).thenReturn(toAccount);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            transactionService.transfer(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Saldo tidak mencukupi untuk melakukan transfer", exception.getReason());
    }
}