package com.example.server.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

public class WeatherController {
    private ObjectMapper objectMapper;
    private WeatherService weatherService;

    public WeatherController(ObjectMapper objectMapper, WeatherService weatherService) {
        this.objectMapper = objectMapper;
        this.weatherService = weatherService;
    }

    public String getWeather(Long localizationId, String date) throws JsonProcessingException, WeatherAPIClient.WeatherRetrievalException {
        try {
            LocalDate localDate = LocalDate.parse(date);
            Weather weather = weatherService.getWeather(localizationId, localDate);
            return objectMapper.writeValueAsString(weather);
        } catch (JsonProcessingException e) {
            return "{\"error\": \"Internal server error\"}"; //http 400
        } catch (Exception e) {
            return "{\"error\": \"Internal server error\"}"; // http 500
        }

    }

}
