package iqbal.javabank.service;

import iqbal.javabank.dto.AccountRequest;
import iqbal.javabank.dto.response.AccountResponse;
import iqbal.javabank.entity.Account;

import java.util.List;

public interface AccountService {

    AccountResponse create(AccountRequest request);

    AccountResponse get(String id);

    void delete(String id);

    Account getByNoRek(Long id);

    void update(Account account);

    List<AccountResponse> getCustomerId(String id);

    Account getById(String id);

}
