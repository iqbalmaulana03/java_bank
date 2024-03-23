package iqbal.javabank.controller;

import iqbal.javabank.dto.TransactionRequest;
import iqbal.javabank.dto.TransferRequest;
import iqbal.javabank.dto.WithdrawalRequest;
import iqbal.javabank.dto.response.TransactionResponse;
import iqbal.javabank.dto.response.WebResponse;
import iqbal.javabank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping("/deposito")
    ResponseEntity<WebResponse<TransactionResponse>> depositor(@RequestBody TransactionRequest request){
        TransactionResponse deposito = service.deposito(request);

        WebResponse<TransactionResponse> response = WebResponse.<TransactionResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully deposito")
                .data(deposito)
                .build();

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/withdrawal")
    ResponseEntity<WebResponse<TransactionResponse>> withdrawal(@RequestBody WithdrawalRequest request){
        TransactionResponse withdrawal = service.withdrawal(request);

        WebResponse<TransactionResponse> response = WebResponse.<TransactionResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully withdrawal")
                .data(withdrawal)
                .build();

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    ResponseEntity<WebResponse<TransactionResponse>> transfer(@RequestBody TransferRequest request){
        TransactionResponse withdrawal = service.transfer(request);

        WebResponse<TransactionResponse> response = WebResponse.<TransactionResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully transfer")
                .data(withdrawal)
                .build();

        return  ResponseEntity.ok(response);
    }
}
