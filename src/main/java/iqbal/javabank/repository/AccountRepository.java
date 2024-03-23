package iqbal.javabank.repository;

import iqbal.javabank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByNoRek(Long noRek);

    @Query(value = """
            select a.id, a.balance, a.type, a.number_rekening, a.customer_id, mc.first_name, mc.last_name, mc.phone_number
            from m_account a
            join m_customer mc on a.customer_id = mc.id
            where mc.id = :customerId
            """, nativeQuery = true)
    List<Object[]> findByCustomerId(@Param("customerId") String id);
}
