package iqbal.javabank.service.impl;

import iqbal.javabank.dto.UpdateCustomerRequest;
import iqbal.javabank.dto.response.CustomerResponse;
import iqbal.javabank.entity.Customer;
import iqbal.javabank.entity.User;
import iqbal.javabank.repository.CustomerRepository;
import iqbal.javabank.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private final CustomerRepository repository = mock(CustomerRepository.class);
    private final ValidationUtils validationUtils = mock(ValidationUtils.class);

    private final UserServiceImpl userService = mock(UserServiceImpl.class);

    private final CustomerServiceImpl customerService = new CustomerServiceImpl(repository, validationUtils, userService);

    @Test
    void getCustomerByUserId_ExistingCustomer() {
        // Prepare user ID
        User user = User.builder()
                .id("123")
                .build();

        // Prepare customer in repository
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId("1");
        expectedCustomer.setUser(user);

        when(repository.findByUserId(user.getId())).thenReturn(Optional.of(expectedCustomer));

        // Call the method
        Customer result = customerService.getCustomerByUserId(user.getId());

        // Verify the interaction
        verify(repository).findByUserId(user.getId());

        // Assert the result
        assertEquals(expectedCustomer, result);
    }

    @Test
    void getCustomerByUserId_CustomerNotFound() {
        // Prepare user ID
        User user = User.builder()
                .id("123")
                .build();

        // Prepare repository behavior
        when(repository.findByUserId(user.getId())).thenReturn(Optional.empty());

        // Call the method and assert exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> customerService.getCustomerByUserId(user.getId()));

        // Verify the interaction
        verify(repository).findByUserId(user.getId());

        // Assert the exception message and status
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Customer with User ID NOT_FOUND", exception.getReason());
    }

    @Test
    void getById() {
        Customer expectedCustomer = Customer.builder()
                .id("123")
                .firstName("testing")
                .lastName("test")
                .phoneNumber("12345678987")
                .address("123 AMx St")
                .build();

        when(repository.findById(expectedCustomer.getId())).thenReturn(Optional.of(expectedCustomer));

        // Call the method
        Customer result = customerService.getById(expectedCustomer.getId());

        // Verify the interaction
        verify(repository).findById(expectedCustomer.getId());

        // Assert the result
        assertEquals(expectedCustomer, result);
    }

    @Test
    void get() {

        User currentUser = new User();
        currentUser.setId("456");

        // Prepare customer in repository
        Customer customer = Customer.builder()
                .id("123")
                .firstName("testing")
                .lastName("test")
                .phoneNumber("12345678987")
                .address("123 AMx St")
                .user(currentUser)
                .build();

        when(repository.findById(customer.getId())).thenReturn(Optional.of(customer));

        // Set authenticated user in SecurityContextHolder
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(currentUser);

        // Call the method
        CustomerResponse response = customerService.get(customer.getId());

        // Verify the interaction
        verify(repository).findById(customer.getId());

        // Assert the response
        assertNotNull(response);
    }

    @Test
    void update() {
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setId("123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhoneNumber("123456789");
        request.setAddress("123 Main St");

        // Prepare authenticated user
        User currentUser = new User();
        currentUser.setId("456");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(currentUser);

        // Prepare customer in repository
        Customer customer = new Customer();
        customer.setId("123");
        customer.setUser(currentUser);

        when(repository.findById(request.getId())).thenReturn(Optional.of(customer));

        // Perform update
        CustomerResponse response = customerService.update(request);

        // Verify interaction
        verify(validationUtils).validate(request);
        verify(repository).findById(request.getId());
        verify(repository).save(customer);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(request.getAddress(), response.getAddress());
    }

    @Test
    void delete() {
        User currentUser = new User();
        currentUser.setId("456");

        // Prepare customer in repository
        Customer customer = new Customer();
        customer.setId("123");

        User customerUser = new User();
        customerUser.setId("456"); // Same user ID as currentUser
        customer.setUser(customerUser);

        when(repository.findById(customer.getId())).thenReturn(Optional.of(customer));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(currentUser);

        // Call the method
        assertDoesNotThrow(() -> customerService.delete(customer.getId()));

        // Verify the interaction
        verify(repository).findById(customer.getId());
        verify(userService).delete(customerUser.getId());
        verify(repository).delete(customer);
    }
}