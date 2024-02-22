package com.example.server.weather;

import com.example.server.localization.Localization;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;


public class WeatherAPIClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey = "13f86a736285b9535206b2294119cb9d";

    public WeatherAPIClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Weather getWeather(Float longitude, Float latitude, Timestamp dt) throws WeatherRetrievalException {
        Weather weather = new Weather();
        try {
            WeatherResponseDTO response = getWeatherClient(longitude, latitude, dt);
            if (response != null) {
                weather.setTemp(response.getMainInfo().getTemp());
                weather.setFeelsLike(response.getMainInfo().getFeelsLike());
                weather.setPressure(response.getMainInfo().getPressure());
                weather.setHumidity(response.getMainInfo().getHumidity());
                weather.setWindSpeed(response.getWindInfo().getWindSpeed());
                weather.setWindDeg(response.getWindInfo().getWindDeg());
                weather.setCloudsAll(response.getCloudsInfo().getCloudsAll());
                weather.setForecastTimestamp(response.getForecastTimestamp());
                Localization localization = new Localization();
                localization.setLatitude(response.getCoordinates().getLatitude());
                localization.setLongitude(response.getCoordinates().getLongitude());
                localization.setCountry(response.getSystemInfo().getCountryCode());
                weather.setLocalization(localization);
                return weather;
            } else {
                throw new WeatherRetrievalException("Getting weather error.");
            }
        } catch (Exception e) {
            throw new WeatherRetrievalException("Getting weather error: " + e.getMessage());
        }
    }

    public WeatherResponseDTO getWeatherClient(Float longitude, Float latitude, Timestamp dt) throws WeatherRetrievalException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                // zapytanie z podaniem daty:   .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&dt=" + dt + "&appid=" + apiKey))
                .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey))
                .build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(httpResponse.body(), WeatherResponseDTO.class);
        } catch (IOException | InterruptedException e) {
            throw new WeatherRetrievalException("Error while fetching weather data");
        }
    }


    static public class WeatherRetrievalException extends Exception {
        public WeatherRetrievalException(String s) {
        }
    }
}