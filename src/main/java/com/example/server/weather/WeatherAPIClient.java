package com.example.server.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final String apiKey = "60dee7201c6bb6b28f3894043a213dd8";

    public WeatherAPIClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public Weather getWeather(Float longitude, Float latitude, Timestamp dt) throws WeatherRetrievalException {
        try {
            WeatherResponseDTO response = getWeatherClient(longitude, latitude, dt);
            if (response == null) {
                throw new WeatherRetrievalException("No weather data could be retrieved.");
            }
            Weather weather = new Weather();
            LocalDate WeatherDate = dt.toLocalDateTime().toLocalDate();
            if (response.getData() != null && !response.getData().isEmpty()) {
                WeatherResponseDTO.MainData mainData = response.getData().get(0); // pierwszy element z listy
                weather.setTemp(mainData.getTemp());
                weather.setFeelsLike(mainData.getFeelsLike());
                weather.setPressure(mainData.getPressure());
                weather.setHumidity(mainData.getHumidity());
                weather.setWindSpeed(mainData.getWindSpeed());
                weather.setWindDeg(mainData.getWindDeg());
                weather.setCloudsAll(mainData.getCloudsAll());
                weather.setWeatherDate(WeatherDate);
                weather.setTimezone(response.getTimezone());
                if (!mainData.getWeather().isEmpty()) {
                    WeatherResponseDTO.MainData.WeatherInfo weatherInfo = mainData.getWeather().get(0); // pierwszy element z listy
                    weather.setDescription(weatherInfo.getDescriptionInfo());
                    weather.setMainInfo(weatherInfo.getMainInfo());
                }
            } else {
                weather.setTemp(null);
                weather.setFeelsLike(null);
                weather.setPressure(null);
                weather.setHumidity(null);
                weather.setWindSpeed(null);
                weather.setWindDeg(null);
                weather.setCloudsAll(null);
                weather.setWeatherDate(WeatherDate);
                weather.setTimezone(null);
            }
            return weather;
        } catch (Exception e) {
            throw new WeatherRetrievalException("Unexpected error occurred: " + e.getMessage(), e);
        }
    }

    public WeatherResponseDTO getWeatherClient(Float longitude, Float latitude, Timestamp dt) throws
            WeatherRetrievalException {
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

    static public class WeatherRetrievalException extends Exception {
        public WeatherRetrievalException(String message, Throwable cause) {
            super(message, cause); // Przekazanie do klasy bazowej Exception
        }

        public WeatherRetrievalException(String message) {
            super(message);
        }
    }

}