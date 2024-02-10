package com.example.server;

import org.hibernate.SessionFactory;

import java.util.List;

public class AnimalHibernateRepository implements AnimalRepository {

    private final SessionFactory sessionFactory;

    public AnimalHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Animal> getAnimals() {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();

        var animals = session.createQuery("select a from Animal a", Animal.class).getResultList();

        transaction.commit();
        session.close();

        return animals;
    }

    @Override
    public void createAnimal(Animal animal) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();

        session.persist(animal);

        transaction.commit();
        session.close();
    }
}
