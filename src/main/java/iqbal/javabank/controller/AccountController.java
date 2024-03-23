package iqbal.javabank.controller;

import iqbal.javabank.dto.AccountRequest;
import iqbal.javabank.dto.response.AccountResponse;
import iqbal.javabank.dto.response.WebResponse;
import iqbal.javabank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<WebResponse<AccountResponse>> crate(@RequestBody AccountRequest request){
        AccountResponse accountResponse = service.create(request);

        WebResponse<AccountResponse> response = WebResponse.<AccountResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create account")
                .data(accountResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<WebResponse<AccountResponse>> get(@PathVariable("accountId") String id){
        AccountResponse accountResponse = service.get(id);

        WebResponse<AccountResponse> response = WebResponse.<AccountResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get account by id")
                .data(accountResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("accountId") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete account")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<WebResponse<List<AccountResponse>>> getByCustomer(@PathVariable("customerId") String id){
        List<AccountResponse> customerId = service.getCustomerId(id);

        WebResponse<List<AccountResponse>> response = WebResponse.<List<AccountResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get account by customer id")
                .data(customerId)
                .build();

        return ResponseEntity.ok(response);
    }
}
