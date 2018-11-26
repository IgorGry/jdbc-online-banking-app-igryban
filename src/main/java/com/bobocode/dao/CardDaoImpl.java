package com.bobocode.dao;

import com.bobocode.model.Card;
import com.bobocode.model.Person;
import com.bobocode.util.EntityManagerUtil;

import javax.persistence.EntityManagerFactory;
import java.util.Objects;

public class CardDaoImpl implements CardDao {
    private EntityManagerUtil emUtil;

    public CardDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.emUtil = new EntityManagerUtil(entityManagerFactory);
    }

    @Override
    public void save(Card card) {
        Objects.requireNonNull(card);
        emUtil.performWithinTx(entityManager -> {
            Person personReference = entityManager.getReference(Person.class, card.getPerson().getId());
            card.setPerson(personReference);
            entityManager.persist(card);
        });
    }

    @Override
    public Card findById(long id) {
        return emUtil.performReturningWithinTx(entityManager -> entityManager.find(Card.class, id));
    }

    @Override
    public Card updateCard(Card card) {
        return emUtil.performReturningWithinTx(entityManager -> entityManager.merge(card));
    }

    @Override
    public void remove(Card card) {
        emUtil.performWithinTx(entityManager -> {
            Card managedCard = entityManager.find(Card.class, card.getId());
            entityManager.remove(managedCard);
        });
    }
}
