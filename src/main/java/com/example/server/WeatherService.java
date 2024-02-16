package com.example.server;

import java.util.List;
import java.util.Optional;

public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherApiClient weatherApiClient;
    private final CityRepository cityRepository;

    public WeatherService(WeatherRepository weatherRepository, WeatherApiClient weatherApiClient, CityRepository cityRepository) {
        this.weatherRepository = weatherRepository;
        this.weatherApiClient = weatherApiClient;
        this.cityRepository = cityRepository;
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }
    public Optional<WeatherResponse> getWeatherFromApi(String cityName) {

        Optional<WeatherResponse> weatherResponse = weatherApiClient.getWeatherIfExists(cityName);
        weatherResponse.ifPresent(this::saveWeatherResponse);
        return weatherResponse;
    }
        public CheckWeather saveWeatherResponse(WeatherResponse response) {
        City city = new City();
        CheckWeather checkWeather = new CheckWeather();

        city.setCountryCode(response.getSystemInfo().getCountryCode());
        city.setCityName(response.getCityName());
            CityRepository cityRepository = new CityHibernateRepository();

            //sprawdzenie czy miasto istnieje w bazie danych i zapis
            Optional<City> existingCity = cityRepository.findByCityName(city.getCityName());
            if (existingCity.isPresent()) {
                checkWeather.setCity(existingCity.get());
            } else {
                cityRepository.saveCity(city);
                checkWeather.setCity(city);
            }
            //przepisanie obiektu response na encje bazy danych
            if (response.getWeather() != null && !response.getWeather().isEmpty()) {
                WeatherResponse.WeatherDescription weatherDescription = response.getWeather().get(0);
                if (weatherDescription != null) {
                    checkWeather.setDescription(weatherDescription.getDescription());
                }
            }
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
}

