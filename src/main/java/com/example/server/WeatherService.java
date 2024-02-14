package com.example.server;

import java.util.List;
import java.util.Optional;

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

    public void addWeather(CheckWeather checkWeather, City city) {
        var time = weatherApiClient.getWeatherIfExists(city.getCityName());
        checkWeather.setCity(city);
        weatherRepository.saveWeather(checkWeather);
    }

    public void addWeatherFromResponse(WeatherResponse weatherResponse) {
        City city = new City();
        // Przepisanie właściwości City na podstawie weatherResponse
        city.setCityName(weatherResponse.getCityName());
        if (weatherResponse.getSystemInfo() != null) {
            city.setCountryCode(weatherResponse.getSystemInfo().getCountryCode());
        }

        CheckWeather checkWeather = new CheckWeather();
        // Przepisanie właściwości CheckWeather na podstawie weatherResponse\

        CityRepository cityRepository = new HibernateCityRepository();

        Optional<City> existingCity = cityRepository.findByCityName(city.getCityName());
        if (existingCity.isPresent()) {
            checkWeather.setCity(existingCity.get());
        } else {
            cityRepository.saveCity(city);
            checkWeather.setCity(city);
        }

        checkWeather.setCity(city);
        checkWeather.setDescription(weatherResponse.getDescription());
        checkWeather.setForecastTimestamp(weatherResponse.getForecastTimestamp());

        if (weatherResponse.getMainInfo() != null) {
            checkWeather.setTemp(weatherResponse.getMainInfo().getTemp());
            checkWeather.setFeelsLike(weatherResponse.getMainInfo().getFeelsLike());
            checkWeather.setTempMin(weatherResponse.getMainInfo().getTempMin());
            checkWeather.setTempMax(weatherResponse.getMainInfo().getTempMax());
            checkWeather.setPressure(weatherResponse.getMainInfo().getPressure());
            checkWeather.setHumidity(weatherResponse.getMainInfo().getHumidity());
        }
        if (weatherResponse.getWindInfo() != null) {
            checkWeather.setWindSpeed(weatherResponse.getWindInfo().getWindSpeed());
        }
        if (weatherResponse.getCloudsInfo() != null) {
            checkWeather.setCloudsAll(weatherResponse.getCloudsInfo().getCloudsAll());
        }

        {

            addWeather(checkWeather, city);
        }


    }
}
