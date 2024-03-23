package iqbal.javabank.controller;

import iqbal.javabank.dto.AuthRequest;
import iqbal.javabank.dto.response.AuthResponse;
import iqbal.javabank.dto.response.RegisterResponse;
import iqbal.javabank.dto.response.WebResponse;
import iqbal.javabank.service.AuthService;
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

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void register() {

        AuthRequest request = new AuthRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password");

        // Mocking service response
        RegisterResponse expectedResponse = new RegisterResponse();
        expectedResponse.setEmail("test@gmail.com");

        when(authService.register(request)).thenReturn(expectedResponse);

        // Testing controller method
        ResponseEntity<WebResponse<RegisterResponse>> responseEntity = authController.register(request);

        // Verifying if the service method is called with correct arguments
        verify(authService, times(1)).register(request);

        // Verifying response status and content
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        WebResponse<RegisterResponse> responseBody = responseEntity.getBody();
        assertEquals("successfully register user", Objects.requireNonNull(responseBody).getMessage());
        assertEquals(expectedResponse, responseBody.getData());
    }

    @Test
    void login() {

        AuthRequest request = new AuthRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password");

        // Mocking service response
        AuthResponse expectedResponse = new AuthResponse();
        expectedResponse.setToken("token");

        when(authService.login(request)).thenReturn(expectedResponse);

        // Testing controller method
        ResponseEntity<WebResponse<AuthResponse>> responseEntity = authController.login(request);

        // Verifying if the service method is called with correct arguments
        verify(authService, times(1)).login(request);

        // Verifying response status and content
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        WebResponse<AuthResponse> responseBody = responseEntity.getBody();
        assertEquals("successfully login user", Objects.requireNonNull(responseBody).getMessage());
        assertEquals(expectedResponse, responseBody.getData());
    }
}