package iqbal.javabank.repository;

import iqbal.javabank.constant.EAccount;
import iqbal.javabank.entity.Account;
import iqbal.javabank.entity.Customer;
import iqbal.javabank.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AccountRepositoryTest {

    private final AccountRepository repository;

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    @Autowired
    public AccountRepositoryTest(AccountRepository repository, CustomerRepository customerRepository, UserRepository userRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Test
    void AccountRepository_SaveAll_ReturnAccount() {

        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        Customer customer = Customer.builder()
                .user(user)
                .build();

        Account account = Account.builder()
                .customer(customer)
                .build();

        Account save = repository.save(account);

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isGreaterThan("");
    }

    @Test
    void AccountRepository_FindById_ReturnAccount() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        Customer customer = Customer.builder()
                .user(user)
                .build();

        Account account = Account.builder()
                .customer(customer)
                .build();

        repository.save(account);

        Account getAccount = repository.findById(account.getId()).orElseThrow();

        Assertions.assertThat(getAccount).isNotNull();
    }

    @Test
    void findByNoRek() {

        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        userRepository.save(user);

        Customer customer = Customer.builder()
                .user(user)
                .build();
        customerRepository.save(customer);

        Account account = Account.builder()
                .noRek(12345L)
                .customer(customer)
                .build();

        repository.save(account);

        Account getAccount = repository.findByNoRek(account.getNoRek()).orElseThrow();

        Assertions.assertThat(getAccount).isNotNull();
    }

    @Test
    void findByCustomerId() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        userRepository.save(user);

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .address("ST Max")
                .user(user)
                .build();
        customerRepository.save(customer);

        Account account = Account.builder()
                .noRek(12345L)
                .balance(BigDecimal.valueOf(1000))
                .type(EAccount.TABUNGAN)
                .customer(customer)
                .build();
        repository.save(account);

        List<Object[]> result = repository.findByCustomerId(account.getCustomer().getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());

        Object[] row = result.get(0);
        assertEquals(8, row.length);
    }

    @Test
    void AccountRepository_Update_ReturnAccount() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        Customer customer = Customer.builder()
                .firstName("testing")
                .lastName("test")
                .user(user)
                .build();

        Account account = Account.builder()
                .balance(BigDecimal.valueOf(1000.00))
                .customer(customer)
                .build();

        repository.save(account);

        Account accountSaved = repository.findById(account.getId()).orElseThrow();
        accountSaved.setBalance(BigDecimal.valueOf(2000.00));

        Account updated = repository.save(accountSaved);

        Assertions.assertThat(updated.getBalance()).isNotNull();
    }

    @Test
    void AccountRepository_Delete_ReturnAccount() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        Customer customer = Customer.builder()
                .firstName("testing")
                .lastName("test")
                .user(user)
                .build();

        Account account = Account.builder()
                .customer(customer)
                .build();

        repository.save(account);

        repository.deleteById(account.getId());
        Optional<Account> byId = repository.findById(account.getId());

        Assertions.assertThat(byId).isEmpty();
    }
}