package com.example.server.weather;

import com.example.server.exception.NoLocalizationFoundException;
import com.example.server.localization.Localization;
import com.example.server.localization.LocalizationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherAPIClient weatherAPIClient;
    private final LocalizationRepository localizationRepository;
    private final WeatherRepository weatherRepository;


    @Value("${weather.forecast.ttl}")
    private Duration forecastTtl;

    public WeatherService(WeatherAPIClient weatherAPIClient, LocalizationRepository localizationRepository, WeatherRepository weatherRepository) {
        this.weatherAPIClient = weatherAPIClient;
        this.localizationRepository = localizationRepository;
        this.weatherRepository = weatherRepository;

    }

    public WeatherResponseWithMessageDTO getWeather(Long localizationId, LocalDate weatherDate) {
        Localization localization = localizationRepository.findById(localizationId).orElse(null);
        WeatherResponseWithMessageDTO weatherResponseWithMessageDTO = new WeatherResponseWithMessageDTO();
        LocalDateTime chosenDateTime = weatherDate.atTime(LocalTime.NOON);
        if (localization != null) {
            Float longitude = localization.getLongitude();
            Float latitude = localization.getLatitude();
            Optional<Weather> savedWeathers = weatherRepository.findByLocalizationIdAndWeatherDateOrderByExpiryTimeDesc(localizationId, weatherDate).stream().findFirst();
            Weather savedWeather =savedWeathers.orElse(null);
            if (savedWeather != null && SavedWeatherIsCurrent(savedWeather) != null) {
                savedWeather.setLocalization(localization);
                weatherResponseWithMessageDTO.setWeather(WeatherToDTO(savedWeather));
                weatherResponseWithMessageDTO.setGeneralMessage(checkDate(chosenDateTime.toLocalDate()));
            } else {
                System.out.println("Nie udało się pobrać aktualnych danych pogodowych z repozytorium, sprawdzam pogodę w API");
                Timestamp dt = Timestamp.valueOf(chosenDateTime);
                WeatherDTO weatherDTO = weatherAPIClient.getWeather(longitude, latitude, dt);
                weatherDTO.setLocalization(localization);
                weatherResponseWithMessageDTO.setWeather(weatherDTO);
                weatherResponseWithMessageDTO.setGeneralMessage(checkDate(chosenDateTime.toLocalDate()));
                LocalDateTime newExpiryDateTime = LocalDateTime.now().plus(forecastTtl);
                Weather newWeather = WeatherFromDTO(weatherDTO);
                newWeather.setExpiryTime(newExpiryDateTime);
                weatherRepository.save(newWeather);
            }
            return weatherResponseWithMessageDTO;
        } else {
            throw new NoLocalizationFoundException(localizationId);
        }
    }

    private WeatherDTO WeatherToDTO(Weather myWeather) {
        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setMessage(myWeather.getMessage());
        weatherDTO.setMainInfo(myWeather.getMainInfo());
        weatherDTO.setDescription(myWeather.getDescription());
        weatherDTO.setTemp(myWeather.getTemp());
        weatherDTO.setFeelsLike(myWeather.getFeelsLike());
        weatherDTO.setPressure(myWeather.getPressure());
        weatherDTO.setHumidity(myWeather.getHumidity());
        weatherDTO.setWindSpeed(myWeather.getWindSpeed());
        weatherDTO.setWindDeg(myWeather.getWindDeg());
        weatherDTO.setCloudsAll(myWeather.getCloudsAll());
        weatherDTO.setLocalization(myWeather.getLocalization());
        weatherDTO.setWeatherDate(myWeather.getWeatherDate());
        weatherDTO.setTimezone(myWeather.getTimezone());

        return weatherDTO;
    }

    private Weather WeatherFromDTO(WeatherDTO myWeatherDTO) {
        Weather myWeather = new Weather();
        myWeather.setMessage(myWeatherDTO.getMessage());
        myWeather.setMainInfo(myWeatherDTO.getMainInfo());
        myWeather.setDescription(myWeatherDTO.getDescription());
        myWeather.setTemp(myWeatherDTO.getTemp());
        myWeather.setFeelsLike(myWeatherDTO.getFeelsLike());
        myWeather.setPressure(myWeatherDTO.getPressure());
        myWeather.setHumidity(myWeatherDTO.getHumidity());
        myWeather.setWindSpeed(myWeatherDTO.getWindSpeed());
        myWeather.setWindDeg(myWeatherDTO.getWindDeg());
        myWeather.setCloudsAll(myWeatherDTO.getCloudsAll());
        myWeather.setLocalization(myWeatherDTO.getLocalization());
        myWeather.setWeatherDate(myWeatherDTO.getWeatherDate());
        myWeather.setTimezone(myWeatherDTO.getTimezone());
        myWeather.setLocalization(myWeatherDTO.getLocalization());
        return myWeather;
    }

    public String checkDate(LocalDate inputDate) {
        LocalDate today = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(today, inputDate);
        if (daysBetween < 0) {
            return "Wybrano datę historyczną, prezentowane dane pogodowe z przeszłości.";
        } else if (daysBetween > 5) {
            return "Wybrano zbyt późną datę. Prognoza pogody jest możliwa maksymalnie na 5 dni do przodu.";
        } else {
            return null;
        }
    }

    Weather SavedWeatherIsCurrent(Weather savedWeather) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime savedWeatherExpiryTime = savedWeather.getExpiryTime();
        if (currentDateTime.isAfter(savedWeatherExpiryTime)) {
            return null;
        } else {
            return savedWeather;
        }
    }

}

