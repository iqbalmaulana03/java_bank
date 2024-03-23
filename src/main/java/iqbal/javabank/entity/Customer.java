package iqbal.javabank.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_customer")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Customer extends DateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
