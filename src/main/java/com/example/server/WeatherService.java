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

    public List<CheckWeather> getWeatherFromDatabase(String cityName) {
        return weatherRepository.checkByCityName(cityName);
    }
    public Optional<WeatherResponse> getWeatherFromApi(String cityName) {

        Optional<WeatherResponse> weatherResponse = weatherApiClient.getWeatherIfExists(cityName);
        weatherResponse.ifPresent(this::saveWeatherResponse);
        return weatherResponse;
    }
        public CheckWeather saveWeatherResponse(WeatherResponse response) {
        City city = new City();
        CheckWeather checkWeather = new CheckWeather();
        //przepisanie obiektu response na encje bazy danych
        city.setCountryCode(response.getSystemInfo().getCountryCode());
        city.setCityName(response.getCityName());
            if (response.getWeather() != null && !response.getWeather().isEmpty()) {
                WeatherResponse.WeatherDescription weatherDescription = response.getWeather().get(0);
                if (weatherDescription != null) {
                    checkWeather.setDescription(weatherDescription.getDescription());
                }
            }
        checkWeather.setCity(city);
        checkWeather.setTemp(response.getMainInfo().getTemp());
        checkWeather.setTempMax(response.getMainInfo().getTempMax());
        checkWeather.setTempMin(response.getMainInfo().getTempMin());
        checkWeather.setFeelsLike(response.getMainInfo().getFeelsLike());
        checkWeather.setPressure(response.getMainInfo().getPressure());
        checkWeather.setHumidity(response.getMainInfo().getHumidity());
        checkWeather.setWindSpeed(response.getWindInfo().getWindSpeed());
        checkWeather.setCloudsAll(response.getCloudsInfo().getCloudsAll());
        checkWeather.setForecastTimestamp(response.getForecastTimestamp());
        addWeather(checkWeather, city);

            return checkWeather;
        }


    public void addWeather(CheckWeather checkWeather, City city) {
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

        CityRepository cityRepository = new CityHibernateRepository();

        Optional<City> existingCity = cityRepository.findByCityName(city.getCityName());
        if (existingCity.isPresent()) {
            checkWeather.setCity(existingCity.get());
        } else {
            cityRepository.saveCity(city);
            checkWeather.setCity(city);
        }

        checkWeather.setCity(city);
        //checkWeather.setDescription(weatherResponse.ggetDescription());
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
