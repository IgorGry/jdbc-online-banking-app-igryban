package com.bobocode.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column (name ="birthday")
    private LocalDate birthday;
    @Column(name = "passport_number", nullable = false, unique = true)
    private String passportNumber;
    @Column(name = "tax_number", nullable = false, unique = true)
    private String taxNumber;
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> bankCards = new ArrayList<>();

    public void addCard(Card card) {
        bankCards.add(card);
        card.setPerson(this);
    }

    public void removeCard(Card card) {
        bankCards.remove(card);
        card.setPerson(null);
    }

    public Person(String firstName, String lastName, String passportNumber, String taxNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.taxNumber = taxNumber;
    }

}
