package com.example.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public WeatherApiClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public String getWeather() {
        var httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(String.format("http://worldtimeapi.org/api/timezone/Europe/Warsaw")))
                .build();

        try {
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            var responseBody = httpResponse.body();
            var dateTime = objectMapper.readValue(responseBody, DateTimeDTO.class);
            return dateTime.getDatetime();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
