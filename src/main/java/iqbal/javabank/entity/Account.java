package iqbal.javabank.entity;

import iqbal.javabank.constant.EAccount;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_account")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Account extends DateTime{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private EAccount type;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "number_rekening")
    private Long noRek;
}
