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
        var weatherData = weatherService.getWeatherFromApi(cityName);
        return objectMapper.writeValueAsString(weatherData); // 200 OK
    }

    // POST: /weather
    public void addWeather(String json) throws JsonProcessingException {
        var city = objectMapper.readValue(json, City.class);
        var checkWeather = objectMapper.readValue(json, CheckWeather.class);
        weatherService.addWeather(checkWeather, city);
        // 201 CREATED
    }
}
