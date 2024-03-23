package iqbal.javabank.service;

import iqbal.javabank.dto.AuthRequest;
import iqbal.javabank.dto.response.AuthResponse;
import iqbal.javabank.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(AuthRequest request);

    AuthResponse login(AuthRequest request);
}
