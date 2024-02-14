package com.example.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class WeatherApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey = "13f86a736285b9535206b2294119cb9d";

    public WeatherApiClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    public Optional<WeatherResponse> getWeatherIfExists(String cityName) {
        try {
            WeatherResponse response = getWeather(cityName);
            if (response != null) {
                return Optional.of(response);
            }
        } catch (Exception e) {
            // Obsługa wyjątków
        }
        return Optional.empty();
    }

    public WeatherResponse getWeather(String cityName) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey))
                .build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(httpResponse.body(), WeatherResponse.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

            return null;
        }
    }
}

