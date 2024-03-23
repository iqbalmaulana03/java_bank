package iqbal.javabank.controller;

import iqbal.javabank.dto.UpdateCustomerRequest;
import iqbal.javabank.dto.response.CustomerResponse;
import iqbal.javabank.dto.response.WebResponse;
import iqbal.javabank.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<WebResponse<CustomerResponse>> update(@PathVariable("customerId") String id,
                                                                @RequestBody UpdateCustomerRequest request){

        request.setId(id);

        CustomerResponse update = service.update(request);

        WebResponse<CustomerResponse> response = WebResponse.<CustomerResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully update customer")
                .data(update)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<WebResponse<CustomerResponse>> get(@PathVariable("customerId") String id){
        CustomerResponse update = service.get(id);

        WebResponse<CustomerResponse> response = WebResponse.<CustomerResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get customer by id")
                .data(update)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("customerId") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete customer")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}
