package com.example.server;

import org.hibernate.SessionFactory;

import java.util.List;

public class WeatherHibernateRepository implements WeatherRepository {

    private final SessionFactory sessionFactory;

    public WeatherHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<CheckWeather> findByCityName(String cityName) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();

        var city = session.createQuery("select a from City a", CheckWeather.class).getResultList();
        var weatherData = session.createQuery("select w from CheckWeather w where w.city.cityName = :cityName", CheckWeather.class)
                .setParameter("cityName", cityName)
                .getResultList();
        transaction.commit();
        session.close();

        return weatherData;
    }

    @Override
    public void saveWeather(CheckWeather checkWeather) {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();

        session.persist(checkWeather);

        transaction.commit();
        session.close();
    }
}
