package com.bobocode.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "card")
@EqualsAndHashCode(of = "id")

public class Card {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "number",nullable = false, unique = true)
    private String number;
    @Column(name = "cvv2")
    private int cvv2;
    @Column(name = "expiration_day")
    private LocalDate expirationDay;
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    private Person person;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Payment> payments = new ArrayList<>();

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setCard(this);
    }

    public void removePayment(Payment payment) {
        payments.add(payment);
        payment.setCard(null);
    }

    public Card(String number, BigDecimal balance, Person person) {
        this.number = number;
        this.balance = balance;
        this.person = person;
    }

}

