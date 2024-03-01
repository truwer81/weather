package com.example.server.weather;

import com.example.server.localization.Localization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {
    private String message;
    private String mainInfo;
    private String description;
    private Float temp;
    private Float feelsLike;
    private Float pressure;
    private Float humidity;
    private Float windSpeed;
    private Float windDeg;
    private Float cloudsAll;
    private Localization localization;
    private LocalDate weatherDate;
    private String timezone;
}

