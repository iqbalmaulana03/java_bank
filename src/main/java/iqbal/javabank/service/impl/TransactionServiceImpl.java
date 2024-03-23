package iqbal.javabank.service.impl;

import iqbal.javabank.constant.ETransaction;
import iqbal.javabank.dto.TransactionRequest;
import iqbal.javabank.dto.TransferRequest;
import iqbal.javabank.dto.WithdrawalRequest;
import iqbal.javabank.dto.response.TransactionResponse;
import iqbal.javabank.entity.Account;
import iqbal.javabank.entity.Transaction;
import iqbal.javabank.repository.TransactionRepository;
import iqbal.javabank.service.AccountService;
import iqbal.javabank.service.TransactionService;
import iqbal.javabank.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    private final ValidationUtils utils;

    private final AccountService accountService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse deposito(TransactionRequest request) {
        utils.validate(request);

        Account accountByNoRek = accountService.getByNoRek(request.getNoRek());

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .account(accountByNoRek)
                .types(ETransaction.DEPOSITO)
                .transactionDate(new Date())
                .build();

        var count = accountByNoRek.getBalance().add(request.getAmount());

        accountByNoRek.setBalance(count);

        accountService.update(accountByNoRek);

        repository.save(transaction);

        return toTransactionResponse(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse withdrawal(WithdrawalRequest request) {
        utils.validate(request);

        Account account = accountService.getById(request.getAccountId());

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .account(account)
                .types(ETransaction.WITHDRAWAL)
                .transactionDate(new Date())
                .build();

        var count = account.getBalance().subtract(request.getAmount());

        account.setBalance(count);

        accountService.update(account);

        repository.save(transaction);

        return toTransactionResponse(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse transfer(TransferRequest request) {

        utils.validate(request);

        Account fromAccount = accountService.getById(request.getAccountId());
        Account toAccount = accountService.getByNoRek(request.getNoRek());

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Saldo tidak mencukupi untuk melakukan transfer");

        var newBalanceFrom = fromAccount.getBalance().subtract(request.getAmount());
        var newBalanceTo = toAccount.getBalance().add(request.getAmount());
        fromAccount.setBalance(newBalanceFrom);
        toAccount.setBalance(newBalanceTo);

        accountService.update(fromAccount);
        accountService.update(toAccount);

        Transaction transactionFrom = Transaction.builder()
                .amount(request.getAmount())
                .transactionDate(new Date())
                .types(ETransaction.TRANSFER)
                .account(fromAccount)
                .build();

        Transaction transactionTo = Transaction.builder()
                .amount(request.getAmount())
                .transactionDate(new Date())
                .types(ETransaction.TRANSFER)
                .account(toAccount)
                .build();

        repository.saveAndFlush(transactionFrom);
        repository.saveAndFlush(transactionTo);

        return toTransactionResponse(transactionTo);
    }

    private TransactionResponse toTransactionResponse(Transaction transaction){
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .types(transaction.getTypes().name())
                .transactionDate(transaction.getTransactionDate())
                .noRek(transaction.getAccount().getNoRek())
                .firstName(transaction.getAccount().getCustomer().getFirstName())
                .lastName(transaction.getAccount().getCustomer().getLastName())
                .build();
    }
}
