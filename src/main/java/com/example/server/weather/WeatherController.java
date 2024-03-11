package com.example.server.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final WeatherMapper weatherMapper;

    // GET /weather?localizationId={localizationId}&date={date}
    @GetMapping("/weather")
    public ResponseEntity<WeatherDTO> getWeather(@RequestParam Long localizationId, @RequestParam String date) {
        var localDate = LocalDate.parse(date);
        var weather = weatherService.getWeather(localizationId, localDate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(weatherMapper.asDTO(weather));
    }
}
