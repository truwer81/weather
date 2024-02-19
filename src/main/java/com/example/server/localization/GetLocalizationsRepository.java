package com.example.server.localization;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class GetLocalizationsRepository {

    private final SessionFactory sessionFactory;

    public GetLocalizationsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Localization> findAll() {
        Session session = sessionFactory.openSession();
        try {
            // Utworzenie zapytania HQL do pobrania wszystkich lokalizacji
            String hql = "FROM Localization";
            Query<Localization> query = session.createQuery(hql, Localization.class);

            // Wykonanie zapytania i pobranie wyników
            List<Localization> localizations = query.getResultList();
            return localizations;
        } finally {

            session.close();
        }
    }

}

