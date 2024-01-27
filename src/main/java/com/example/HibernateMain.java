package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateMain {

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // Animal newAnimal = new Animal(4L, "Sonia", 11); // transient
        // session.persist(newAnimal);

        Animal animal = session.find(Animal.class, 1L); // persistent

        System.out.println(animal.getName());
        System.out.println(animal.getAge());

        animal.setAge(6);

        transaction.commit();
        session.close();
    }
}
