package iqbal.javabank.entity;

import iqbal.javabank.constant.ETransaction;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction extends DateTime{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private BigDecimal amount;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Enumerated(EnumType.STRING)
    private ETransaction types;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
