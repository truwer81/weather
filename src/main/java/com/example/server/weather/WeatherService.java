package com.example.server.weather;

import com.example.server.localization.Localization;
import com.example.server.localization.LocalizationRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeatherService {

    private WeatherAPIClient weatherAPIClient;
    private LocalizationRepository localizationRepository;

    public WeatherService(WeatherAPIClient weatherAPIClient, LocalizationRepository localizationRepository) {
        this.weatherAPIClient = weatherAPIClient;
        this.localizationRepository = localizationRepository;
    }

    public Weather getWeather(Long localizationId, LocalDate date) throws WeatherAPIClient.WeatherRetrievalException {
        Float longitude = null;
        Float latitude = null;
        Timestamp dt = null;

        try {
            Localization localization = localizationRepository.findOne(localizationId);
            longitude = localization.getLongitude();
            latitude = localization.getLatitude();
            LocalDateTime localDateTime = date.atStartOfDay();
            dt = Timestamp.valueOf(localDateTime);
        } catch (Exception e) {
            throw new WeatherAPIClient.WeatherRetrievalException("404");
        }
        return weatherAPIClient.getWeather(longitude, latitude, dt);
    }
}

