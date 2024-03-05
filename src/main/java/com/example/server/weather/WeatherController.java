package com.example.server.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    // GET /weather?localizationId={localizationId}&date={date}
    @GetMapping("/weather")
    public WeatherResponseWithMessageDTO getWeather(@RequestParam Long localizationId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return weatherService.getWeather(localizationId, localDate);
    }
}
