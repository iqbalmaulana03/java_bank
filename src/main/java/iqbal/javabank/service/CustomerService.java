package iqbal.javabank.service;

import iqbal.javabank.dto.UpdateCustomerRequest;
import iqbal.javabank.dto.response.CustomerResponse;
import iqbal.javabank.entity.Customer;

public interface CustomerService {

    void create(Customer customer);

    Customer getCustomerByUserId(String userId);

    Customer getById(String id);

    CustomerResponse get(String id);

    CustomerResponse update(UpdateCustomerRequest request);

    void delete(String id);
}
