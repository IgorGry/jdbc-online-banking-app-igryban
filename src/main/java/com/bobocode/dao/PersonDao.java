package com.bobocode.dao;

import com.bobocode.model.Person;

public interface PersonDao {
    void save(Person person);

    Person findById(long id);

    Person updatePerson(Person person);

    void remove(Person person);
}