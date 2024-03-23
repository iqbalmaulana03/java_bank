package iqbal.javabank.controller;

import iqbal.javabank.dto.AuthRequest;
import iqbal.javabank.dto.response.AuthResponse;
import iqbal.javabank.dto.response.RegisterResponse;
import iqbal.javabank.dto.response.WebResponse;
import iqbal.javabank.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<WebResponse<RegisterResponse>> register(@RequestBody AuthRequest request){
        RegisterResponse register = authService.register(request);

        WebResponse<RegisterResponse> response = WebResponse.<RegisterResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully register user")
                .data(register)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<AuthResponse>> login(@RequestBody AuthRequest request){
        AuthResponse login = authService.login(request);

        WebResponse<AuthResponse> response = WebResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully login user")
                .data(login)
                .build();

        return ResponseEntity.ok(response);
    }
}
