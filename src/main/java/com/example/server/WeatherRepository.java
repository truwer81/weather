package com.example.server;

import java.util.List;

public interface WeatherRepository {

    List<CheckWeather> findByCityName(String cityName);

    void saveWeather(CheckWeather checkWeather);
}
