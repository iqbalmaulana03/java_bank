package iqbal.javabank.service.impl;

import iqbal.javabank.constant.EAccount;
import iqbal.javabank.dto.AccountRequest;
import iqbal.javabank.dto.response.AccountResponse;
import iqbal.javabank.entity.Account;
import iqbal.javabank.entity.Customer;
import iqbal.javabank.entity.User;
import iqbal.javabank.repository.AccountRepository;
import iqbal.javabank.service.AccountService;
import iqbal.javabank.service.CustomerService;
import iqbal.javabank.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    private final ValidationUtils utils;

    private final CustomerService customerService;

    private static final String FORBIDDEN = "FORBIDDEN";

    private static final String ROLE = "ROLE_ADMIN";

    private final Random random = new Random();

    public AccountServiceImpl(AccountRepository repository, ValidationUtils utils, CustomerService customerService) {
        this.repository = repository;
        this.utils = utils;
        this.customerService = customerService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountResponse create(AccountRequest request) {

        utils.validate(request);

        Customer customer = customerService.getById(request.getCustomerId());

        Account account = new Account();

        switch (request.getType()){
            case "TABUNGAN":
                account.setBalance(request.getBalance());
                account.setNoRek(generateRandomLong());
                account.setType(EAccount.TABUNGAN);
                account.setCustomer(customer);
                repository.save(account);
                break;
            case "GIRO":
                account.setBalance(request.getBalance());
                account.setNoRek(generateRandomLong());
                account.setType(EAccount.GIRO);
                account.setCustomer(customer);
                repository.save(account);
                break;
            case "DEPOSITO":
                account.setBalance(request.getBalance());
                account.setNoRek(generateRandomLong());
                account.setType(EAccount.DEPOSITO);
                account.setCustomer(customer);
                repository.save(account);
                break;
            default:
                log.info("Account Type tidak terserdia");
        }

        return toAccountResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse get(String id) {

        Account account = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not_Found")
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = account.getCustomer().getUser();

        if (!currentUser.getId().equals(user.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        return toAccountResponse(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {

        Account account = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found")
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = account.getCustomer().getUser();

        if (!currentUser.getId().equals(user.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        repository.delete(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Account getByNoRek(Long id) {
        return repository.findByNoRek(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Account account) {
        Account byNoRek = repository.findByNoRek(account.getNoRek()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Number Rekening Not Found"));

        byNoRek.setBalance(account.getBalance());

        repository.save(byNoRek);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getCustomerId(String id) {

        List<Object[]> byCustomerId = repository.findByCustomerId(id);

        List<AccountResponse> responses = new ArrayList<>();

        for (Object[] customer : byCustomerId){
            AccountResponse response = new AccountResponse();
            response.setId((String) customer[0]);
            response.setBalance((BigDecimal) customer[1]);
            response.setType((String) customer[2]);
            response.setNoRek((Long) customer[3]);
            response.setCustomerId((String) customer[4]);
            response.setFirstName((String) customer[5]);
            response.setLastName((String) customer[6]);
            response.setPhoneNumber((String) customer[7]);

            responses.add(response);
        }

        return responses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Account getById(String id) {

        Account account = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account_Not_Found")
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = account.getCustomer().getUser();

        if (!currentUser.getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        return account;
    }

    private AccountResponse toAccountResponse(Account account){
        return AccountResponse.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .type(account.getType().name())
                .noRek(account.getNoRek())
                .customerId(account.getCustomer().getId())
                .firstName(account.getCustomer().getFirstName())
                .lastName(account.getCustomer().getLastName())
                .phoneNumber(account.getCustomer().getPhoneNumber())
                .build();
    }

    public Long generateRandomLong() {
        return Math.abs(random.nextLong());
    }
}
