package com.example.server;
import java.util.List;
import java.util.Optional;
import static com.example.server.HibernateUtils.*;

public class HibernateCityRepository implements CityRepository{

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
        throw e; // Można również obsłużyć wyjątek bardziej szczegółowo
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
}
