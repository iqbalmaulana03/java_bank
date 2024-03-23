package iqbal.javabank.service;

import iqbal.javabank.dto.TransactionRequest;
import iqbal.javabank.dto.TransferRequest;
import iqbal.javabank.dto.WithdrawalRequest;
import iqbal.javabank.dto.response.TransactionResponse;

public interface TransactionService {

    TransactionResponse deposito(TransactionRequest request);

    TransactionResponse withdrawal(WithdrawalRequest request);

    TransactionResponse transfer(TransferRequest request);
}
