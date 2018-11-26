package com.bobocode.dao;

        import com.bobocode.model.Card;

public interface CardDao {
    void save(Card card);

    Card findById(long id);

    Card updateCard(Card card);

    void remove(Card card);
}
