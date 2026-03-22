package com.example.server.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherController {

    private final ObjectMapper objectMapper;
    private final WeatherService weatherService;

    public WeatherController(ObjectMapper objectMapper, WeatherService weatherService) {
        this.objectMapper = objectMapper;
        this.weatherService = weatherService;
    }

    // TODO: add forecast support with dedicated OpenWeather forecast endpoint

    // GET /weather?localization={localizationId}
    public String getWeather(Long localizationId, String date) throws JsonProcessingException, WeatherAPIClient.WeatherRetrievalException {
        try {
            Weather weather = weatherService.getCurrentWeather(localizationId);
            return objectMapper.writeValueAsString(weather);
        } catch (WeatherAPIClient.WeatherRetrievalException e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        } catch (JsonProcessingException e) {
            return "{\"error\": \"Internal server error\"}"; //http 400
        } catch (Exception e) {
            return "{\"error\": \"Internal server error\"}"; // http 500
        }
    }
}
