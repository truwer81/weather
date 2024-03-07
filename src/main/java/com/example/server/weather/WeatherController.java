package com.example.server.weather;

import com.example.server.localization.Localization;
import com.example.server.localization.LocalizationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final WeatherMapper weatherMapper;

    // GET /weather?localizationId={localizationId}&date={date}
    @GetMapping("/weather")
    public ResponseEntity<WeatherResponseWithMessageDTO> getWeather(@RequestParam Long localizationId, @RequestParam String date) {
        var localDate = LocalDate.parse(date);
        return ResponseEntity.status(HttpStatus.OK)
                .body(weatherService.getWeather(localizationId, localDate));
    }
}
