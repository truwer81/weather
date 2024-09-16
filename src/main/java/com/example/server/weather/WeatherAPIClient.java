package com.example.server.weather;

import com.example.server.exception.WeatherRetrievalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class WeatherAPIClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Setter
    @Value("${api.key}")
    private String apiKey;

    public WeatherResponseDTO.WeatherForecastDTO getWeather(Float longitude, Float latitude, Timestamp dt) throws WeatherRetrievalException {
        var unixTimestamp = dt.getTime() / 1000; // Konwersja na sekundy
        var httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.openweathermap.org/data/3.0/onecall/timemachine?lat=" + latitude + "&lon=" + longitude + "&exclude=hourly&dt=" + unixTimestamp + "&units=metric&appid=" + apiKey))
                .build();
        try {
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            var weatherResponseDTO = objectMapper.readValue(httpResponse.body(), WeatherResponseDTO.class);
            var data = weatherResponseDTO.getData();
            if (data.size() != 1) {
                throw new WeatherRetrievalException("Error while fetching weather data, expected 1 result, got " + data.size());
            }
            return data.get(0);
        } catch (IOException | InterruptedException e) {
            throw new WeatherRetrievalException("Error while fetching weather data");
        }
    }
}