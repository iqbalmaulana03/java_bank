package iqbal.javabank.service.impl;

import iqbal.javabank.dto.UpdateCustomerRequest;
import iqbal.javabank.dto.response.CustomerResponse;
import iqbal.javabank.entity.Customer;
import iqbal.javabank.entity.User;
import iqbal.javabank.repository.CustomerRepository;
import iqbal.javabank.service.CustomerService;
import iqbal.javabank.service.UserService;
import iqbal.javabank.utils.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    private final ValidationUtils utils;

    private final UserService userService;

    private static final String FORBIDDEN = "FORBIDDEN";

    private static final String ROLE = "ROLE_ADMIN";

    public CustomerServiceImpl(CustomerRepository repository, ValidationUtils utils, UserService userService) {
        this.repository = repository;
        this.utils = utils;
        this.userService = userService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Customer customer) {
        repository.save(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Customer getCustomerByUserId(String userId) {
        return repository.findByUserId(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with User ID NOT_FOUND")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Customer getById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer NOT FOUND")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse get(String id) {

        Customer customer = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer NOT FOUND")
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customer.getUser();

        if (!currentUser.getId().equals(user.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        return toCustomerResponse(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse update(UpdateCustomerRequest request) {
        utils.validate(request);

        Customer customer = repository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer NOT_FOUND")
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customer.getUser();

        if (!currentUser.getId().equals(user.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setAddress(request.getAddress());

        repository.save(customer);

        return toCustomerResponse(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {

        Customer customer = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer NOT_FOUND")
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customer.getUser();

        if (!currentUser.getId().equals(user.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        userService.delete(user.getId());

        repository.delete(customer);
    }

    private CustomerResponse toCustomerResponse(Customer customer){
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .build();
    }
}
