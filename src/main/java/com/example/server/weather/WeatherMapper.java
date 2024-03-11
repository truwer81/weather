package com.example.server.weather;

import org.springframework.stereotype.Component;

@Component
public class WeatherMapper {

    public WeatherDTO asDTO(Weather weather) {
        return new WeatherDTO(
                weather.getTemp(),
                weather.getPressure(),
                weather.getHumidity(),
                weather.getWindSpeed(),
                weather.getWindDeg()
        );
    }
}
