package com.example.server.localization;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

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

    public List<Localization> findAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            String hql = "FROM Localization";
            Query<Localization> query = session.createQuery(hql, Localization.class);

            List<Localization> localizations = query.getResultList();
            transaction.commit();
            return localizations;
        } finally {

            session.close();
        }
    }

    public Localization findOne(long localizationId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            String hql = "FROM Localization WHERE id=" + localizationId;
            Query<Localization> query = session.createQuery(hql, Localization.class);

            Localization localization = query.getSingleResult();
            transaction.commit();
            return localization;
        } finally {

            session.close();
        }
    }
}
