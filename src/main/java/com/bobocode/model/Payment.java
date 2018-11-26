package com.bobocode.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment")
@EqualsAndHashCode(of = "id")

public class Payment {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "payment_timestamp")
    private LocalDateTime paymentTimestamp;
    @ManyToOne(optional = false)
    @JoinColumn(name = "card_id")
    private Card card;

    public Payment(BigDecimal amount, Card card) {
        this.amount = amount;
        this.paymentTimestamp = LocalDateTime.now();
        this.card = card;
    }

}
