package iqbal.javabank.repository;

import iqbal.javabank.entity.Account;
import iqbal.javabank.entity.Customer;
import iqbal.javabank.entity.Transaction;
import iqbal.javabank.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TransactionRepositoryTest {

    private final TransactionRepository repository;

    @Autowired
    public TransactionRepositoryTest(TransactionRepository repository) {
        this.repository = repository;
    }

    @Test
    void TransactionRepository_SaveAll_ReturnTransaction() {

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

        Transaction transaction = Transaction.builder()
                .account(account)
                .build();

        Transaction save = repository.save(transaction);

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isGreaterThan("");
    }

    @Test
    void transactionRepository_FindById_ReturnTransaction() {
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

        Transaction transaction = Transaction.builder()
                .account(account)
                .build();

        repository.save(transaction);

        Transaction getTransaction = repository.findById(transaction.getId()).orElseThrow();

        Assertions.assertThat(getTransaction).isNotNull();
    }
}