package com.example.server.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @SpringBootTest
public class WeatherAPIClientTest {

    @Test
    void callWeatherAPI_returnsCorrectValue() {
        // Given
        var build = HttpClient.newBuilder().build();
        var objectMapper = new ObjectMapper();
        var weatherAPIClient = new WeatherAPIClient(build, objectMapper);
        var timestamp = Timestamp.valueOf(LocalDate.now().atTime(LocalTime.NOON));
        weatherAPIClient.setApiKey("60dee7201c6bb6b28f3894043a213dd8");
        // When
        var result = weatherAPIClient.getWeather(1.0f, 1.0f, timestamp);
        // Then
        assertNotNull(result);
        assertNotNull(result.getCloudsAll());
        assertNotNull(result.getFeelsLike());
        assertNotNull(result.getHumidity());
        assertNotNull(result.getPressure());
        assertNotNull(result.getTemp());
        assertNotNull(result.getWeatherTimeStamp());
        assertNotNull(result.getWindDeg());
        assertNotNull(result.getWindSpeed());
        assertTrue(result.getPressure() >= 0);
    }
}
