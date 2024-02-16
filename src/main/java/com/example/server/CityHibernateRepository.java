package com.example.server;
import java.util.List;
import java.util.Optional;
import static com.example.server.HibernateUtils.*;

public class CityHibernateRepository implements CityRepository{

@Override
public Optional<City> findByCityName(String cityName) {
    var session = sessionFactory.openSession();
    var transaction = session.beginTransaction();

    try {
        // Zapytanie o miasto na podstawie nazwy miasta
        City city = session.createQuery("SELECT c FROM City c WHERE c.cityName = :cityName", City.class)
                .setParameter("cityName", cityName)
                .uniqueResult(); // Metoda uniqueResult zwraca pojedynczy wynik lub null, jeśli nie znaleziono

        transaction.commit();
        session.close();

        // Zwraca Optional<City>
        return Optional.ofNullable(city); // Konwertuje wynik na Optional
    } catch (Exception e) {
        // W przypadku błędu cofa transakcję i zamyka sesję
        if (transaction != null) transaction.rollback();
        session.close();
        throw e;
    }
   }
    public List<City> findAll() {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();

        try {
            // Zapytanie o miasto na podstawie nazwy miasta
            List<City> cityList = session.createQuery("SELECT c FROM City c", City.class)
                    .getResultList();
            transaction.commit();
            session.close();

            return cityList;
        } catch (Exception e) {
            // W przypadku błędu cofa transakcję i zamyka sesję
            if (transaction != null) transaction.rollback();
            session.close();
            throw e;
        }
    }
    public void saveCity(City city) {
        var session = sessionFactory.openSession();
        var tx = session.beginTransaction();
        try {
            // Sprawdzenie, czy miasto już istnieje
            City existingCity = session.get(City.class, city.getCityName());
            if (existingCity == null) {
                // Miasto nie istnieje, więc je zapisujemy
                session.save(city);
            } else {
                // Miasto już istnieje
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

}
