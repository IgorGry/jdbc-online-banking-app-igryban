package com.bobocode.dao;

import com.bobocode.model.Card;
import com.bobocode.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentDao {
    void save(Payment payment);

    Payment findById(long id);

    Payment updatePayment(Payment payment);

    void remove(Payment payment);

    List<Payment> findAllByCard(Card cardId);

    List<Payment> findAllByPerson(Long personId);

    List<Payment> findAllByPersonAndDate(Long personId, LocalDate date);

    List<Payment> findAllAmountMoreThan(Long personId, BigDecimal amount);

}