package com.example.server.weather;

import com.example.server.exception.WeatherRetrievalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.LocalDate;

@Service
public class WeatherAPIClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${api.key}")
    private String apiKey;

    public WeatherAPIClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public WeatherDTO getWeather(Float longitude, Float latitude, Timestamp dt) {
        try {
            WeatherResponseDTO response = getWeatherClient(longitude, latitude, dt);
            if (response == null) {
                throw new WeatherRetrievalException("No weather data could be retrieved.");
            }
            WeatherDTO weatherDTO = new WeatherDTO();
            LocalDate WeatherDate = dt.toLocalDateTime().toLocalDate();
            if (response.getData() != null && !response.getData().isEmpty()) {
                WeatherResponseDTO.MainData mainData = response.getData().get(0); // pierwszy element z listy
                weatherDTO.setTemp(mainData.getTemp());
                weatherDTO.setFeelsLike(mainData.getFeelsLike());
                weatherDTO.setPressure(mainData.getPressure());
                weatherDTO.setHumidity(mainData.getHumidity());
                weatherDTO.setWindSpeed(mainData.getWindSpeed());
                weatherDTO.setWindDeg(mainData.getWindDeg());
                weatherDTO.setCloudsAll(mainData.getCloudsAll());
                weatherDTO.setWeatherDate(WeatherDate);
                weatherDTO.setTimezone(response.getTimezone());
                if (!mainData.getWeather().isEmpty()) {
                    WeatherResponseDTO.MainData.WeatherInfo weatherInfo = mainData.getWeather().get(0); // pierwszy element z listy
                    weatherDTO.setDescription(weatherInfo.getDescriptionInfo());
                    weatherDTO.setMainInfo(weatherInfo.getMainInfo());
                }
            } else {
                weatherDTO.setTemp(null);
                weatherDTO.setFeelsLike(null);
                weatherDTO.setPressure(null);
                weatherDTO.setHumidity(null);
                weatherDTO.setWindSpeed(null);
                weatherDTO.setWindDeg(null);
                weatherDTO.setCloudsAll(null);
                weatherDTO.setWeatherDate(WeatherDate);
                weatherDTO.setTimezone(null);
            }
            return weatherDTO;
        } catch (Exception e) {
            throw new WeatherRetrievalException("Unexpected error occurred: " + e.getMessage(), e);
        }
    }

    public WeatherResponseDTO getWeatherClient(Float longitude, Float latitude, Timestamp dt) throws WeatherRetrievalException {
        long unixTimestamp = dt.getTime() / 1000; // Konwersja na sekundy
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.openweathermap.org/data/3.0/onecall/timemachine?lat=" + latitude + "&lon=" + longitude + "&exclude=hourly&dt=" + unixTimestamp + "&units=metric&appid=" + apiKey))
                .build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(httpResponse.body(), WeatherResponseDTO.class);
        } catch (IOException | InterruptedException e) {
            throw new WeatherRetrievalException("Error while fetching weather data");
        }
    }
}