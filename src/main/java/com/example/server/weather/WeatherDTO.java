package com.example.server.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {
    private Float temperature;
    private Float pressure;
    private Float humidity;
    private Float windSpeed;
    private Float windDeg;
}
