package iqbal.javabank.service.impl;

import iqbal.javabank.constant.EAccount;
import iqbal.javabank.dto.AccountRequest;
import iqbal.javabank.dto.response.AccountResponse;
import iqbal.javabank.entity.Account;
import iqbal.javabank.entity.Customer;
import iqbal.javabank.entity.User;
import iqbal.javabank.repository.AccountRepository;
import iqbal.javabank.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private CustomerServiceImpl customerService;

    @Mock
    private AccountRepository repository;

    @Mock
    private ValidationUtils utils;

    @Test
    void create() {

        AccountRequest request = new AccountRequest();
        request.setCustomerId("1");
        request.setType("TABUNGAN");
        request.setBalance(BigDecimal.valueOf(1000000));

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

        Account account = new Account();
        account.setId("1");
        account.setBalance(BigDecimal.valueOf(1000000));
        account.setNoRek(2412976832288655245L);
        account.setType(EAccount.TABUNGAN);
        account.setCustomer(customer);

        when(customerService.getById(customer.getId())).thenReturn(customer);
        when(repository.save(any(Account.class))).thenReturn(account);

        AccountResponse response = accountService.create(request);

        verify(utils).validate(request);

        assertEquals(account.getBalance(), response.getBalance());
        assertEquals(account.getType().name(), response.getType());
        assertEquals(account.getCustomer().getFirstName(), response.getFirstName());
    }

    @Test
    void get() {

        User user = User.builder()
                .id("123")
                .build();

        Customer customer = Customer.builder()
                .id("456")
                .user(user)
                .build();

        Account account = Account.builder()
                .id("789")
                .noRek(123232L)
                .type(EAccount.TABUNGAN)
                .balance(BigDecimal.valueOf(10000.00))
                .customer(customer)
                .build();

        when(repository.findById(account.getId())).thenReturn(Optional.of(account));

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(user);

        AccountResponse response = accountService.get(account.getId());

        verify(repository).findById(account.getId());

        assertNotNull(response);
        assertEquals(account.getId(), response.getId());
        assertEquals(account.getNoRek(), response.getNoRek());

    }

    @Test
    void delete() {

        User user = User.builder()
                .id("123")
                .build();

        Customer customer = Customer.builder()
                .id("456")
                .user(user)
                .build();

        Account account = Account.builder()
                .id("789")
                .noRek(123232L)
                .type(EAccount.TABUNGAN)
                .balance(BigDecimal.valueOf(10000.00))
                .customer(customer)
                .build();

        when(repository.findById(account.getId())).thenReturn(Optional.of(account));

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(user);

        assertDoesNotThrow(() -> accountService.delete(account.getId()));

        verify(repository).findById(account.getId());
        verify(repository).delete(account);
    }

    @Test
    void getByNoRek() {

        Account account = Account.builder()
                .id("789")
                .noRek(123232L)
                .type(EAccount.TABUNGAN)
                .balance(BigDecimal.valueOf(10000.00))
                .build();

        when(repository.findByNoRek(account.getNoRek())).thenReturn(Optional.of(account));

        Account result = accountService.getByNoRek(account.getNoRek());

        verify(repository).findByNoRek(account.getNoRek());

        assertEquals(account, result);
    }

    @Test
    void update() {
            User user = User.builder()
                    .id("123")
                    .build();

            Customer customer = Customer.builder()
                    .id("456")
                    .user(user)
                    .build();

            Account account = Account.builder()
                    .id("789")
                    .noRek(123232L)
                    .type(EAccount.TABUNGAN)
                    .balance(BigDecimal.valueOf(10000.00))
                    .customer(customer)
                    .build();

            Account existingAccount = Account.builder()
                    .id("789")
                    .noRek(123232L)
                    .type(EAccount.TABUNGAN)
                    .balance(BigDecimal.valueOf(20000.00))
                    .customer(customer)
                    .build();

            when(repository.findByNoRek(account.getNoRek())).thenReturn(Optional.of(existingAccount));

            assertDoesNotThrow(() -> accountService.update(account));

            verify(repository).findByNoRek(account.getNoRek());
            verify(repository).save(existingAccount);

            assertEquals(account.getBalance(), existingAccount.getBalance());
    }

    @Test
    void getCustomerId() {

        Object[] customerData1 = {"123", BigDecimal.valueOf(10000.00), "TABUNGAN", 123232L, "456", "John", "Doe", "123456789"};
        Object[] customerData2 = {"456", BigDecimal.valueOf(20000.00), "GIRO", 987654L, "456", "John", "Doe", "123456789"};
        List<Object[]> dataFromRepository = new ArrayList<>();
        dataFromRepository.add(customerData1);
        dataFromRepository.add(customerData2);

        // Prepare expected responses
        List<AccountResponse> expectedResponses = new ArrayList<>();
        AccountResponse response1 = new AccountResponse();
        response1.setId("123");
        response1.setBalance(BigDecimal.valueOf(10000.00));
        response1.setType("TABUNGAN");
        response1.setNoRek(123232L);
        response1.setCustomerId("456");
        response1.setFirstName("John");
        response1.setLastName("Doe");
        response1.setPhoneNumber("123456789");
        AccountResponse response2 = new AccountResponse();
        response2.setId("456");
        response2.setBalance(BigDecimal.valueOf(20000.00));
        response2.setType("GIRO");
        response2.setNoRek(987654L);
        response2.setCustomerId("456");
        response2.setFirstName("John");
        response2.setLastName("Doe");
        response2.setPhoneNumber("123456789");
        expectedResponses.add(response1);
        expectedResponses.add(response2);

        // Mock the repository
        when(repository.findByCustomerId("456")).thenReturn(dataFromRepository);

        // Call the method
        List<AccountResponse> actualResponses = accountService.getCustomerId("456");

        // Verify the interaction
        verify(repository).findByCustomerId("456");

        // Assert the responses
        assertEquals(expectedResponses.size(), actualResponses.size());
        for (int i = 0; i < expectedResponses.size(); i++) {
            AccountResponse expectedResponse = expectedResponses.get(i);
            AccountResponse actualResponse = actualResponses.get(i);
            assertEquals(expectedResponse.getId(), actualResponse.getId());
            assertEquals(expectedResponse.getBalance(), actualResponse.getBalance());
            assertEquals(expectedResponse.getType(), actualResponse.getType());
            assertEquals(expectedResponse.getNoRek(), actualResponse.getNoRek());
            assertEquals(expectedResponse.getCustomerId(), actualResponse.getCustomerId());
            assertEquals(expectedResponse.getFirstName(), actualResponse.getFirstName());
            assertEquals(expectedResponse.getLastName(), actualResponse.getLastName());
            assertEquals(expectedResponse.getPhoneNumber(), actualResponse.getPhoneNumber());
        }
    }

    @Test
    void getById() {

        User user = User.builder()
                .id("123")
                .build();

        Customer customer = Customer.builder()
                .id("456")
                .user(user)
                .build();

        Account account = Account.builder()
                .id("789")
                .noRek(123232L)
                .type(EAccount.TABUNGAN)
                .balance(BigDecimal.valueOf(10000.00))
                .customer(customer)
                .build();

        when(repository.findById(account.getId())).thenReturn(Optional.of(account));

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(user);

        Account result = accountService.getById(account.getId());

        verify(repository).findById(account.getId());

        assertEquals(account, result);

    }
}