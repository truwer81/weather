package com.example.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherController {

    private final WeatherService weatherService;
    private final ObjectMapper objectMapper;

    public WeatherController(WeatherService weatherService, ObjectMapper objectMapper) {
        this.weatherService = weatherService;
        this.objectMapper = objectMapper;
    }

    // GET: /weather
    public String getWeatherForCity(String cityName) throws JsonProcessingException {
        var weatherData = weatherService.getWeather(cityName);
        return objectMapper.writeValueAsString(weatherData); // 200 OK
    }

    // POST: /animals
    public void createAnimals(String json) throws JsonProcessingException {
        var animal = objectMapper.readValue(json, CheckWeather.class);
        weatherService.createAnimal(animal);
        // 201 CREATED
    }
}
