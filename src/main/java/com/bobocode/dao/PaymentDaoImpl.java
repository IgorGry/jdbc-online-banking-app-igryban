package com.bobocode.dao;

import com.bobocode.model.Card;
import com.bobocode.model.Payment;
import com.bobocode.util.EntityManagerUtil;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PaymentDaoImpl implements PaymentDao {
    private EntityManagerUtil emUtil;

    public PaymentDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.emUtil = new EntityManagerUtil(entityManagerFactory);
    }

    @Override
    public void save(Payment payment) {
        Objects.requireNonNull(payment);
        emUtil.performWithinTx(entityManager -> {
            Card cardReference = entityManager.getReference(Card.class, payment.getCard().getId());
            payment.setCard(cardReference);
            entityManager.persist(payment);
        });
    }

    @Override
    public Payment findById(long id) {
        return emUtil.performReturningWithinTx(entityManager -> entityManager.find(Payment.class, id));
    }

    @Override
    public Payment updatePayment(Payment payment) {
        return emUtil.performReturningWithinTx(entityManager -> entityManager.merge(payment));
    }

    @Override
    public void remove(Payment payment) {
        emUtil.performWithinTx(entityManager -> {
            Payment managedPayment = entityManager.find(Payment.class, payment.getId());
            entityManager.remove(managedPayment);
        });
    }

    @Override
    public List<Payment> findAllByCard(Card cardId) {
        return emUtil.performReturningWithinTx(entityManager ->
                entityManager
                        .createQuery("select p from Payment p where p.card = :card", Payment.class)
                        .setParameter("card", cardId)
                        .getResultList()
        );
    }

    @Override
    public List<Payment> findAllByPerson(Long personId) {
        return emUtil.performReturningWithinTx(entityManager ->
                entityManager
                        .createQuery("select p from Payment p where p.card.person.id = :personid", Payment.class)
                        .setParameter("personid", personId)
                        .getResultList()
        );
    }

    @Override
    public List<Payment> findAllByPersonAndDate(Long personId, LocalDate date) {
        return emUtil.performReturningWithinTx(entityManager ->
                entityManager
                        .createQuery("select p from Payment p where p.card.person.id = :personid and cast(p.paymentTimestamp as date) = :providedDate", Payment.class)
                        .setParameter("personid", personId)
                        .setParameter("providedDate", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        .getResultList()
        );
    }

    @Override
    public List<Payment> findAllAmountMoreThan(Long personId, BigDecimal amount) {
        return emUtil.performReturningWithinTx(entityManager ->
                entityManager
                        .createQuery("select p from Payment p where p.card.person.id = :personid and p.amount > :amount", Payment.class)
                        .setParameter("personid", personId)
                        .setParameter("amount", amount)
                        .getResultList()
        );
    }


}
