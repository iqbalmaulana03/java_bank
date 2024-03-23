package iqbal.javabank.service.impl;

import iqbal.javabank.constant.EUser;
import iqbal.javabank.dto.AuthRequest;
import iqbal.javabank.dto.response.AuthResponse;
import iqbal.javabank.dto.response.RegisterResponse;
import iqbal.javabank.entity.Customer;
import iqbal.javabank.entity.User;
import iqbal.javabank.repository.UserRepository;
import iqbal.javabank.security.JwtUtils;
import iqbal.javabank.service.AuthService;
import iqbal.javabank.service.CustomerService;
import iqbal.javabank.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final ValidationUtils validationUtils;

    private final CustomerService customerService;

    @Autowired
    public AuthServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager, ValidationUtils validationUtils, CustomerService customerService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.validationUtils = validationUtils;
        this.customerService = customerService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse register(AuthRequest request) {

        validationUtils.validate(request);

        Optional<User> byEmail = repository.findByEmail(request.getEmail());

        if (byEmail.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists for User");

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(EUser.ROLE_USER)
                .build();

        repository.saveAndFlush(user);

        Customer customer = Customer.builder()
                .user(user)
                .build();

        customerService.create(customer);

        return RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponse login(AuthRequest request) {

        validationUtils.validate(request);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = repository.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email Not Found")
        );

        Customer customer = customerService.getCustomerByUserId(user.getId());

        String generateToken = jwtUtils.generateToken(user);
        return AuthResponse.builder()
                .token(generateToken)
                .customerId(customer.getId())
                .build();
    }
}
