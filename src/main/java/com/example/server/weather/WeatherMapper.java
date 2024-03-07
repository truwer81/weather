package com.example.server.weather;

import com.example.server.localization.Localization;
import com.example.server.localization.LocalizationDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class WeatherMapper {

    // WeatherDTO: przepisanie encji Weather na obiekt DTO
    public WeatherDTO WeatherToDTO(Weather myWeather) {
        return new WeatherDTO(
                myWeather.getMessage(),
                myWeather.getMainInfo(),
                myWeather.getDescription(),
                myWeather.getTemp(),
                myWeather.getFeelsLike(),
                myWeather.getPressure(),
                myWeather.getHumidity(),
                myWeather.getWindSpeed(),
                myWeather.getWindDeg(),
                myWeather.getCloudsAll(),
                myWeather.getLocalization(),
                myWeather.getWeatherDate(),
                myWeather.getTimezone());
    }


    // WeatherFromDTO: przepisanie obiektu WeatherDTO na encję bazy danych Weather
    public Weather WeatherFromDTO(WeatherDTO myWeatherDTO) {
        return new Weather(
                null,
                myWeatherDTO.getLocalization(),
                null,
                myWeatherDTO.getMessage(),
                myWeatherDTO.getMainInfo(),
                myWeatherDTO.getDescription(),
                myWeatherDTO.getTemp(),
                myWeatherDTO.getFeelsLike(),
                myWeatherDTO.getPressure(),
                myWeatherDTO.getHumidity(),
                myWeatherDTO.getWindSpeed(),
                myWeatherDTO.getWindDeg(),
                myWeatherDTO.getCloudsAll(),
                myWeatherDTO.getWeatherDate(),
                myWeatherDTO.getTimezone());
    }
}
