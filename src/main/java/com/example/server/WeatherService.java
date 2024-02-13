package com.example.server;

import java.util.List;

public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherApiClient weatherApiClient;

    public WeatherService(WeatherRepository weatherRepository, WeatherApiClient weatherApiClient) {
        this.weatherRepository = weatherRepository;
        this.weatherApiClient = weatherApiClient;
    }

    public List<CheckWeather> getWeather(String cityName) {
        return weatherRepository.checkByCityName(cityName);
    }

    public void addWeather(CheckWeather checkWeather) {
        var time = weatherApiClient.getWeather(checkWeather.getCityName());


        weatherRepository.saveWeather(checkWeather);
    }
}
