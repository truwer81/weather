package com.example.server.localization;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class LocalizationRepository {

    private final SessionFactory sessionFactory;

    public LocalizationRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Localization save(Localization localization) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(localization);

        transaction.commit();
        session.close();

        return localization;
    }
}
