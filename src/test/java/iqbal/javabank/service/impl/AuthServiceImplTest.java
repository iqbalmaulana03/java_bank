package iqbal.javabank.service.impl;

import iqbal.javabank.constant.EUser;
import iqbal.javabank.dto.AuthRequest;
import iqbal.javabank.dto.response.AuthResponse;
import iqbal.javabank.dto.response.RegisterResponse;
import iqbal.javabank.entity.Customer;
import iqbal.javabank.entity.User;
import iqbal.javabank.repository.UserRepository;
import iqbal.javabank.security.JwtUtils;
import iqbal.javabank.service.CustomerService;
import iqbal.javabank.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerService customerService;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    void register() {

        AuthRequest authRequest = AuthRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        doNothing().when(validationUtils).validate(authRequest);

        User user = User.builder()
                .email(authRequest.getEmail())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .role(EUser.ROLE_USER)
                .build();

        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(authRequest.getPassword())).thenReturn("encoded_password");
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);

        RegisterResponse response = authService.register(authRequest);

        verify(validationUtils).validate(authRequest);

        assertEquals(user.getId(), response.getId());
        assertEquals(user.getEmail(), response.getEmail());
    }

    @Test
    void login() {

        AuthRequest authRequest = AuthRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        // Mocking the behavior of validationUtils
        doNothing().when(validationUtils).validate(authRequest);

        User user = User.builder()
                .email(authRequest.getEmail())
                .password("encoded_password")
                .role(EUser.ROLE_USER)
                .build();

        Customer customer = Customer.builder()
                .user(user)
                .build();

        String token = "generated_token";

        // Mocking the behavior of authenticationManager
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Mocking the behavior of repository
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(user));

        // Mocking the behavior of customerService
        when(customerService.getCustomerByUserId(user.getId())).thenReturn(customer);

        // Mocking the behavior of jwtUtils
        when(jwtUtils.generateToken(user)).thenReturn(token);

        // Call the login method
        AuthResponse response = authService.login(authRequest);

        // Verify the interactions and assertions
        verify(validationUtils).validate(authRequest);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(authRequest.getEmail());
        verify(customerService).getCustomerByUserId(user.getId());
        verify(jwtUtils).generateToken(user);

        assertEquals(token, response.getToken());
        assertEquals(customer.getId(), response.getCustomerId());
    }

    @Test
    void login_InvalidCredentials() {
        AuthRequest request = new AuthRequest("test@example.com", "password");

        // Mocking the behavior of validationUtils
        doNothing().when(validationUtils).validate(request);

        // Mocking the behavior of authenticationManager to throw AuthenticationException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(ResponseStatusException.class);

        // Call the login method and assert it throws ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> authService.login(request));

        // Verify the interactions
        verify(validationUtils).validate(request);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(customerService);
        verifyNoInteractions(jwtUtils);
    }
}