package com.example.server.weather;

import com.example.server.exception.NoLocalizationFoundException;
import com.example.server.localization.Localization;
import com.example.server.localization.LocalizationRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class WeatherService {

    private final WeatherAPIClient weatherAPIClient;
    private final LocalizationRepository localizationRepository;

    public WeatherService(WeatherAPIClient weatherAPIClient, LocalizationRepository localizationRepository) {
        this.weatherAPIClient = weatherAPIClient;
        this.localizationRepository = localizationRepository;
    }

    public WeatherResponseWithMessageDTO getWeather(Long localizationId, LocalDate date) {
        Localization localization = localizationRepository.findById(localizationId).orElse(null);
        WeatherResponseWithMessageDTO weatherResponseWithMessageDTO = new WeatherResponseWithMessageDTO();
        if (localization != null) {
            Float longitude = localization.getLongitude();
            Float latitude = localization.getLatitude();
            LocalDateTime localDateTime = date.atStartOfDay();
            Timestamp dt = Timestamp.valueOf(localDateTime);
            WeatherDTO weather = weatherAPIClient.getWeather(longitude, latitude, dt);
            weather.setLocalization(localization);
            weatherResponseWithMessageDTO.setWeather(weather);
            weatherResponseWithMessageDTO.setGeneralMessage(checkDate(localDateTime.toLocalDate()));
        } else {
            throw new NoLocalizationFoundException(localizationId);
        }
        return weatherResponseWithMessageDTO;
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
}

