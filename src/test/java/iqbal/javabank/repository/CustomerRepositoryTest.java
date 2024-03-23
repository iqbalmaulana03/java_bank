package iqbal.javabank.repository;

import iqbal.javabank.entity.Customer;
import iqbal.javabank.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CustomerRepositoryTest {

    private final CustomerRepository repository;

    private final UserRepository userRepository;

    @Autowired
    public CustomerRepositoryTest(CustomerRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Test
    void CustomerRepository_SaveAll_ReturnCustomer() {

        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        Customer customer = Customer.builder()
                .user(user)
                .build();

        Customer save = repository.save(customer);

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isGreaterThan("");
    }

    @Test
    void CustomerRepository_GetAll_ReturnMoreOneThanCustomer() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        User user2 = User.builder()
                .email("test2@gmail.com")
                .password("test")
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        Customer customer = Customer.builder()
                .user(user)
                .build();

        Customer customer2 = Customer.builder()
                .user(user2)
                .build();

        repository.save(customer);
        repository.save(customer2);

        List<Customer> all = repository.findAll();

        Assertions.assertThat(all).hasSize(2).isNotNull();
    }

    @Test
    void CustomerRepository_FindById_ReturnCustomer() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        Customer customer = Customer.builder()
                .user(user)
                .build();

        repository.save(customer);

        Customer getCustomer = repository.findById(customer.getId()).orElseThrow();

        Assertions.assertThat(getCustomer).isNotNull();
    }

    @Test
    void findByUserId() {

        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        userRepository.save(user);

        Customer customer = Customer.builder()
                .user(user)
                .build();

        repository.save(customer);

        Customer getCustomer = repository.findByUserId(customer.getUser().getId()).orElseThrow();

        Assertions.assertThat(getCustomer).isNotNull();
    }

    @Test
    void CustomerRepository_Update_ReturnCustomer() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        Customer customer = Customer.builder()
                .firstName("testing")
                .lastName("test")
                .user(user)
                .build();

        repository.save(customer);

        Customer customerSaved = repository.findById(customer.getId()).orElseThrow();
        customerSaved.setFirstName("TESTING");
        customerSaved.setLastName("TEST");

        Customer updated = repository.save(customerSaved);

        Assertions.assertThat(updated.getFirstName()).isNotNull();
        Assertions.assertThat(updated.getLastName()).isNotNull();
    }

    @Test
    void UserRepository_Delete_ReturnCustomerIsEmpty() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        Customer customer = Customer.builder()
                .firstName("testing")
                .lastName("test")
                .user(user)
                .build();

        repository.save(customer);

        repository.deleteById(customer.getId());
        Optional<Customer> byId = repository.findById(customer.getId());

        Assertions.assertThat(byId).isEmpty();
    }
}