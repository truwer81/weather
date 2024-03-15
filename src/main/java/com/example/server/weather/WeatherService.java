package com.example.server.weather;

import com.example.server.exception.NoLocalizationFoundException;
import com.example.server.localization.Localization;
import com.example.server.localization.LocalizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherAPIClient weatherAPIClient;
    private final LocalizationRepository localizationRepository;
    private final WeatherRepository weatherRepository;

    @Value("${weather.forecast.ttl}")
    private Duration forecastTtl;

    public Weather getWeather(Long localizationId, LocalDate weatherDate) {
        var localization = localizationRepository.findById(localizationId).orElseThrow(() -> new NoLocalizationFoundException(localizationId));
        return weatherRepository.findFirstByLocalizationIdAndWeatherDateOrderByExpiryTimeDesc(localizationId, weatherDate)
                .filter(weather -> weather.getExpiryTime().isAfter(LocalDateTime.now()))
                .orElseGet(() -> checkCurrentWeather(localization, weatherDate));
    }

    private Weather checkCurrentWeather(Localization localization, LocalDate weatherDate) {
        var date = weatherDate.atTime(LocalTime.NOON);
        var dateTimestamp = Timestamp.valueOf(date);

        var longitude = localization.getLongitude();
        var latitude = localization.getLatitude();

        var weatherResponse = weatherAPIClient.getWeather(longitude, latitude, dateTimestamp);

        var weather = Weather.builder()
                .id(null)
                .humidity(weatherResponse.getHumidity())
                .temp(weatherResponse.getTemp())
                .pressure(weatherResponse.getPressure())
                .windSpeed(weatherResponse.getWindSpeed())
                .windDeg(weatherResponse.getWindDeg())
                .weatherDate(weatherDate)
                .localization(localization)
                .expiryTime(LocalDateTime.now().plus(forecastTtl))
                .weatherDate(weatherDate)
                .build();

        return weatherRepository.save(weather);
    }
}

