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
        return weatherRepository.findByCityName(cityName);
    }

    public void createAnimal(CheckWeather checkWeather) {
        var age = checkWeather.getAge();
        var time = weatherApiClient.getWeather();
        checkWeather.setCreatedDate(time);

        weatherRepository.saveWeather(checkWeather);
    }
}
