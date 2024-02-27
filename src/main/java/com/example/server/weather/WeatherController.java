package com.example.server.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static com.example.server.weather.WeatherAPIClient.*;

@RestController
public class WeatherController {

    private final ObjectMapper objectMapper;
    private final WeatherService weatherService;

    public WeatherController(ObjectMapper objectMapper, WeatherService weatherService) {
        this.objectMapper = objectMapper;
        this.weatherService = weatherService;
    }

    // GET /weather?localizationId={localizationId}&date={date}
    @GetMapping("/weather")
    public String getWeather(@RequestParam Long localizationId, @RequestParam String date) throws JsonProcessingException, WeatherRetrievalException {
        LocalDate localDate = null;
        WeatherResponseWithMessageDTO weather=null;
        try {
            localDate = LocalDate.parse(date);
            weather = weatherService.getWeather(localizationId, localDate);
        } catch (DateTimeParseException e) {
            return objectMapper.writeValueAsString(new WeatherResponseWithMessageDTO(null, "Niepoprawny format daty: " + date + " Wprowadź datę w formacie YYYY-MM-DD"));
        } catch (WeatherRetrievalException e) {
            return objectMapper.writeValueAsString(new WeatherResponseWithMessageDTO(null, "Błąd podczas pobierania danych pogodowych: " + e.getMessage()));
        } catch (Exception e) {
            return objectMapper.writeValueAsString(new WeatherResponseWithMessageDTO(null, "Wystąpił błąd"));
        }

        return objectMapper.writeValueAsString(weather);

    }
}
