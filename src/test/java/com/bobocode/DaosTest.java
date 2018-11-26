package com.bobocode;

import com.bobocode.dao.*;
import com.bobocode.model.Card;
import com.bobocode.model.Payment;
import com.bobocode.model.Person;
import com.bobocode.util.EntityManagerUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DaosTest {
    private EntityManagerUtil emUtil;
    private EntityManagerFactory entityManagerFactory;
    private PersonDao personDao;
    private CardDao cardDao;
    private PaymentDao paymentDao;

    @BeforeEach
    public void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("BasicEntitiesPostgres");
        emUtil = new EntityManagerUtil(entityManagerFactory);
        personDao = new PersonDaoImpl(entityManagerFactory);
        cardDao = new CardDaoImpl(entityManagerFactory);
        paymentDao = new PaymentDaoImpl(entityManagerFactory);
    }

    @AfterEach
    public void destroy() {
        entityManagerFactory.close();
    }

    @Test
    public void testSavePerson() {
        Person person = new Person("Griban", "Igor", "MV24", "381000");

        personDao.save(person);

        Person foundedPhoto = emUtil.performReturningWithinTx(entityManager -> entityManager.find(Person.class, person.getId()));
        assertThat(foundedPhoto, equalTo(person));
    }

    @Test
    public void testUpdatePerson() {
        Person person = new Person("Lebedeva", "Tatiana", "LG24", "411000");
        emUtil.performWithinTx(entityManager -> entityManager.persist(person));

        person.setFirstName("Gryban");
        personDao.updatePerson(person);

        Person foundPerson = personDao.findById(person.getId());

        assertThat(foundPerson.getFirstName(), equalTo("Gryban"));
    }

    @Test
    public void testRemovePerson() {
        Person person = new Person("Not", "Valid", "Person", "1");
        emUtil.performWithinTx(entityManager -> entityManager.persist(person));

        personDao.remove(person);

        Person removedPerson = emUtil.performReturningWithinTx(entityManager -> entityManager.find(Person.class, person.getId()));
        assertThat(removedPerson, nullValue());
    }

    @Test
    public void testSaveCard() {
        Person person = new Person("Griban", "Igor", "MV24", "381000");
        Card card = new Card("888", BigDecimal.valueOf(1000), person);
        emUtil.performWithinTx(entityManager -> entityManager.persist(person));
        cardDao.save(card);


        Card foundedCard = emUtil.performReturningWithinTx(entityManager -> entityManager.find(Card.class, card.getId()));
        assertThat(foundedCard, equalTo(card));
    }

    @Test
    public void testUpdateCard() {

        Person person = new Person("Lebedeva", "Tatiana", "LG24", "411000");
        Card card = new Card("888", BigDecimal.valueOf(1000), person);
        emUtil.performWithinTx(entityManager -> entityManager.persist(person));
        cardDao.save(card);

        card.setNumber("777");
        cardDao.updateCard(card);

        Card foundedCard = emUtil.performReturningWithinTx(entityManager -> entityManager.find(Card.class, card.getId()));
        assertThat(foundedCard, equalTo(card));

        assertThat(foundedCard.getNumber(), equalTo("777"));
    }

    @Test
    public void testRemoveCard() {
        Person person = new Person("Lebedeva", "Tatiana", "LG24", "411000");
        Card card = new Card("888", BigDecimal.valueOf(1000), person);
        emUtil.performWithinTx(entityManager -> entityManager.persist(person));
        emUtil.performWithinTx(entityManager -> entityManager.persist(card));
        cardDao.remove(card);

        Card removedPhoto = emUtil.performReturningWithinTx(entityManager -> entityManager.find(Card.class, card.getId()));
        assertThat(removedPhoto, nullValue());
    }

    @Test
    public void testSavePayment() {
        Person person = new Person("Griban", "Igor", "MV24", "381000");
        emUtil.performWithinTx(entityManager -> entityManager.persist(person));
        Card card = new Card("888", BigDecimal.valueOf(1000), person);
        emUtil.performWithinTx(entityManager -> entityManager.persist(card));
        Payment payment = new Payment(BigDecimal.valueOf(100), card);

        paymentDao.save(payment);

        Payment foundedPayment = emUtil.performReturningWithinTx(entityManager -> entityManager.find(Payment.class, payment.getId()));
        assertThat(foundedPayment, equalTo(payment));
    }

    @Test
    public void testUpdatePayment() {

        Person person = new Person("Lebedeva", "Tatiana", "LG24", "411000");
        Card card = new Card("888", BigDecimal.valueOf(1000), person);
        emUtil.performWithinTx(entityManager -> entityManager.persist(person));
        Payment payment = new Payment(BigDecimal.valueOf(100), card);
        emUtil.performWithinTx(entityManager -> entityManager.persist(card));

        paymentDao.save(payment);

        BigDecimal newAmount = BigDecimal.valueOf(200);
        payment.setAmount(newAmount);
        paymentDao.updatePayment(payment);

        Payment foundedPayment = emUtil.performReturningWithinTx(entityManager -> entityManager.find(Payment.class, payment.getId()));
        assertThat(foundedPayment, equalTo(payment));

        assertThat(foundedPayment.getAmount().doubleValue(), equalTo(newAmount.doubleValue()));
    }

    @Test
    public void testRemovePayment() {
        Person person = new Person("Lebedeva", "Tatiana", "LG24", "411000");
        emUtil.performWithinTx(entityManager -> entityManager.persist(person));

        Card card = new Card("888", BigDecimal.valueOf(1000), person);
        emUtil.performWithinTx(entityManager -> entityManager.persist(card));

        Payment payment = new Payment(BigDecimal.valueOf(100), card);

        emUtil.performWithinTx(entityManager -> entityManager.persist(payment));

        paymentDao.remove(payment);

        Payment removedPhoto = emUtil.performReturningWithinTx(entityManager -> entityManager.find(Payment.class, payment.getId()));
        assertThat(removedPhoto, nullValue());
    }

    @Test
    public void testFindAllByCard() {
        Person igor = new Person("Griban", "Igor", "MV24", "381000");
        Person tetiana = new Person("Lebedeva", "Tatiana", "LG24", "411000");
        emUtil.performWithinTx(entityManager -> entityManager.persist(igor));
        emUtil.performWithinTx(entityManager -> entityManager.persist(tetiana));

        Card igorCard = new Card("888", BigDecimal.valueOf(10000), igor);
        Card tanyaCard = new Card("777", BigDecimal.valueOf(1000), tetiana);

        emUtil.performWithinTx(entityManager -> entityManager.persist(igorCard));
        emUtil.performWithinTx(entityManager -> entityManager.persist(tanyaCard));

        List<Payment> igorPayments = new ArrayList<>();
        igorPayments.add(new Payment(BigDecimal.valueOf(100), igorCard));
        igorPayments.add(new Payment(BigDecimal.valueOf(200), igorCard));
        igorPayments.add(new Payment(BigDecimal.valueOf(300), igorCard));

        List<Payment> tanyaPayments = new ArrayList<>();
        tanyaPayments.add(new Payment(BigDecimal.valueOf(400), tanyaCard));
        tanyaPayments.add(new Payment(BigDecimal.valueOf(500), tanyaCard));


        igorPayments.forEach(paymentDao::save);
        tanyaPayments.forEach(paymentDao::save);

        List<Payment> igorFoundPayment = paymentDao.findAllByCard(igorCard);
        List<Payment> tanyaFoundPayment = paymentDao.findAllByCard(tanyaCard);

        assertThat(igorFoundPayment, containsInAnyOrder(igorPayments.toArray()));
        assertThat(tanyaFoundPayment, containsInAnyOrder(tanyaPayments.toArray()));

        assertThat(igorFoundPayment.size(), equalTo(igorPayments.size()));
        assertThat(tanyaFoundPayment.size(), equalTo(tanyaPayments.size()));
    }

    @Test
    public void testFindAllByPerson() {
        Person igor = new Person("Griban", "Igor", "MV24", "381000");
        Person tetiana = new Person("Lebedeva", "Tatiana", "LG24", "411000");
        emUtil.performWithinTx(entityManager -> entityManager.persist(igor));
        emUtil.performWithinTx(entityManager -> entityManager.persist(tetiana));

        Card igorCard = new Card("888", BigDecimal.valueOf(10000), igor);
        Card tanyaCard = new Card("777", BigDecimal.valueOf(1000), tetiana);
        emUtil.performWithinTx(entityManager -> entityManager.persist(igorCard));
        emUtil.performWithinTx(entityManager -> entityManager.persist(tanyaCard));

        List<Payment> igorPayments = new ArrayList<>();
        igorPayments.add(new Payment(BigDecimal.valueOf(100), igorCard));
        igorPayments.add(new Payment(BigDecimal.valueOf(200), igorCard));
        igorPayments.add(new Payment(BigDecimal.valueOf(300), igorCard));

        List<Payment> tanyaPayments = new ArrayList<>();
        tanyaPayments.add(new Payment(BigDecimal.valueOf(400), tanyaCard));
        tanyaPayments.add(new Payment(BigDecimal.valueOf(500), tanyaCard));


        igorPayments.forEach(paymentDao::save);
        tanyaPayments.forEach(paymentDao::save);

        List<Payment> igorFoundPayment = paymentDao.findAllByPerson(igor.getId());
        List<Payment> tanyaFoundPayment = paymentDao.findAllByPerson(tetiana.getId());

        assertThat(igorFoundPayment, containsInAnyOrder(igorPayments.toArray()));
        assertThat(tanyaFoundPayment, containsInAnyOrder(tanyaPayments.toArray()));

        assertThat(igorFoundPayment.size(), equalTo(igorPayments.size()));
        assertThat(tanyaFoundPayment.size(), equalTo(tanyaPayments.size()));
    }

    @Test
    public void findAllByPersonAndDate() {
        Person igor = new Person("Griban", "Igor", "MV24", "381000");
        emUtil.performWithinTx(entityManager -> entityManager.persist(igor));

        Card igorCard = new Card("888", BigDecimal.valueOf(10000), igor);
        emUtil.performWithinTx(entityManager -> entityManager.persist(igorCard));


        Payment oldPayment = new Payment(BigDecimal.valueOf(300), igorCard);

        oldPayment.setPaymentTimestamp(LocalDateTime.of(2018, 1, 1, 1, 1, 00));

        List<Payment> igorPayments = new ArrayList<>();
        igorPayments.add(new Payment(BigDecimal.valueOf(100), igorCard));
        igorPayments.add(new Payment(BigDecimal.valueOf(200), igorCard));
        igorPayments.add(oldPayment);

        igorPayments.forEach(paymentDao::save);

        igorPayments.remove(oldPayment);

        List<Payment> igorFoundPayment = paymentDao.findAllByPersonAndDate(igor.getId(), LocalDate.now());

        assertThat(igorFoundPayment, containsInAnyOrder(igorPayments.toArray()));
        assertThat(igorFoundPayment.size(), equalTo(igorPayments.size()));
    }

    @Test
    public void findAllAmountMoreThan() {
        Person igor = new Person("Griban", "Igor", "MV24", "381000");
        emUtil.performWithinTx(entityManager -> entityManager.persist(igor));

        Card igorCard = new Card("888", BigDecimal.valueOf(10000), igor);
        emUtil.performWithinTx(entityManager -> entityManager.persist(igorCard));

        Payment minorPayment = new Payment(BigDecimal.valueOf(5), igorCard);

        List<Payment> igorPayments = new ArrayList<>();
        igorPayments.add(new Payment(BigDecimal.valueOf(100), igorCard));
        igorPayments.add(new Payment(BigDecimal.valueOf(200), igorCard));
        igorPayments.add(minorPayment);

        igorPayments.forEach(paymentDao::save);

        igorPayments.remove(minorPayment);

        List<Payment> igorFoundPayment = paymentDao.findAllAmountMoreThan(igor.getId(), BigDecimal.valueOf(50));

        assertThat(igorFoundPayment, containsInAnyOrder(igorPayments.toArray()));
        assertThat(igorFoundPayment.size(), equalTo(igorPayments.size()));

    }
}
