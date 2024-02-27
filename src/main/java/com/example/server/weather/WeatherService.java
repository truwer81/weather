package com.example.server.weather;

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

    public WeatherResponseWithMessageDTO getWeather(Long localizationId, LocalDate date) throws WeatherAPIClient.WeatherRetrievalException {
        Localization localization = localizationRepository.findOne(localizationId);
        WeatherResponseWithMessageDTO weatherResponseWithMessageDTO = new WeatherResponseWithMessageDTO();
        if (localization != null) {
            Float longitude = localization.getLongitude();
            Float latitude = localization.getLatitude();
            LocalDateTime localDateTime = date.atStartOfDay();
            Timestamp dt = Timestamp.valueOf(localDateTime);
            Weather weather = weatherAPIClient.getWeather(longitude, latitude, dt);
            weather.setLocalization(localization);
            weatherResponseWithMessageDTO.setWeather(weather);
            weatherResponseWithMessageDTO.setGeneralMessage(checkDate(localDateTime.toLocalDate()));
        } else {
            weatherResponseWithMessageDTO.setGeneralMessage("Wybrano błędną lokalizację, nie można sprawdzić pogody.");
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

