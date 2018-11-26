package com.bobocode.dao;

import com.bobocode.model.Person;
import com.bobocode.util.EntityManagerUtil;

import javax.persistence.EntityManagerFactory;
import java.util.Objects;

public class PersonDaoImpl implements PersonDao {
    private EntityManagerUtil emUtil;

    public PersonDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.emUtil = new EntityManagerUtil(entityManagerFactory);
    }

    @Override
    public void save(Person person) {
        Objects.requireNonNull(person);
        emUtil.performWithinTx(entityManager -> entityManager.persist(person));
    }

    @Override
    public Person findById(long id) {
        return emUtil.performReturningWithinTx(entityManager -> entityManager.find(Person.class, id));
    }

    @Override
    public Person updatePerson(Person person) {
        return emUtil.performReturningWithinTx(entityManager -> entityManager.merge(person));
    }

    @Override
    public void remove(Person person) {
        emUtil.performWithinTx(entityManager -> {
            Person managedPerson = entityManager.find(Person.class, person.getId());
            entityManager.remove(managedPerson);
        });
    }
}
