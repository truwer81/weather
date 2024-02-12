package com.example.server;

import java.util.ArrayList;
import java.util.List;

public class WeatherListRepository implements WeatherRepository {

    private static final List<CheckWeather> CHECK_WEATHERS = new ArrayList<>();
    private static Long counter = 1L;

    public WeatherListRepository() {
        CHECK_WEATHERS.add(new CheckWeather(counter++, "Reksio", 4));
        CHECK_WEATHERS.add(new CheckWeather(counter++, "Sonia", 5));
    }

    @Override
    public List<CheckWeather> findByCityName() {
        return CHECK_WEATHERS;
    }

    @Override
    public void saveWeather(CheckWeather checkWeather) {
        checkWeather.setId(counter++);
        CHECK_WEATHERS.add(checkWeather);
    }
}
