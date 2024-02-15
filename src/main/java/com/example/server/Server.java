package com.example.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.util.Objects;

public class Server {

    private final WeatherController weatherController;

    public Server() {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var httpClient = HttpClient.newHttpClient();
        var sessionFactory = HibernateUtils.getSessionFactory();
        var weatherRepository = new WeatherHibernateRepository(sessionFactory);
        var weatherApiClient = new WeatherApiClient(httpClient, objectMapper);
        var checkWeather = new CheckWeather();
        var city = new City();
        var weatherService = new WeatherService(weatherRepository, weatherApiClient);
        this.weatherController = new WeatherController(weatherService, objectMapper);
    }

    public String callServer(String method, String path, String json) {
        try {
            if (Objects.equals(method, "GET") && path.startsWith("/weather/")) {
                String cityName = path.split("/")[2]; // zakładając, że ścieżka ma format "/weather/{cityName}"
                return "200 " + weatherController.getWeatherForCity(cityName);
            }
            if (Objects.equals(method, "POST") && Objects.equals(path, "/animals")) {
                weatherController.addWeather(json);
                return "201";
            }
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return "400";
        } catch (Exception e) {
            return "500";
        }

        return "";
    }
}
