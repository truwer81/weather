package com.example;
import org.hibernate.SessionFactory;
import com.example.server.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;


public class Interface {

    public static void main(String[] args) {
        // Inicjalizacja zależności
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        HttpClient httpClient = HttpClient.newHttpClient();
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory(); // Upewnij się, że HibernateUtils jest odpowiednio skonfigurowane
        WeatherRepository weatherRepository = new WeatherHibernateRepository(sessionFactory);
        WeatherApiClient weatherApiClient = new WeatherApiClient(httpClient, objectMapper);
        WeatherService weatherService = new WeatherService(weatherRepository, weatherApiClient);

        // Inicjalizacja ConsoleMenu z WeatherService
        ConsoleMenu consoleMenu = new ConsoleMenu(weatherService);

        // Uruchomienie menu
        consoleMenu.displayMenu();
    }
}
