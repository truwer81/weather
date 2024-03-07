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
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherAPIClient weatherAPIClient;
    private final LocalizationRepository localizationRepository;
    private final WeatherRepository weatherRepository;
    private final WeatherMapper weatherMapper;


    @Value("${weather.forecast.ttl}")
    private Duration forecastTtl;

    public WeatherService(WeatherAPIClient weatherAPIClient, LocalizationRepository localizationRepository, WeatherRepository weatherRepository, WeatherMapper weatherMapper) {
        this.weatherAPIClient = weatherAPIClient;
        this.localizationRepository = localizationRepository;
        this.weatherRepository = weatherRepository;
        this.weatherMapper = weatherMapper;
    }

    public WeatherResponseWithMessageDTO getWeather(Long localizationId, LocalDate weatherDate) {
        var localization = localizationRepository.findById(localizationId).orElse(null);
        var weatherResponseWithMessageDTO = new WeatherResponseWithMessageDTO();
        var chosenDateTime = weatherDate.atTime(LocalTime.NOON);
        var dt = Timestamp.valueOf(chosenDateTime);
        Localization myLocalization = new Localization();
        if (localization != null) {
            myLocalization = localization;
        } else {
            throw new NoLocalizationFoundException(localizationId);
        }
        var longitude = myLocalization.getLongitude();
        var latitude = myLocalization.getLatitude();
        Optional<Weather> savedWeatherOptional = weatherRepository.findByLocalizationIdAndWeatherDateOrderByExpiryTimeDesc(localizationId, weatherDate).stream().findFirst();
        var savedWeather = savedWeatherOptional.orElse(null);

        if (savedWeather != null) {
            savedWeather.setLocalization(myLocalization);
            weatherResponseWithMessageDTO.setWeather(weatherMapper.WeatherToDTO(savedWeather));
            weatherResponseWithMessageDTO.setGeneralMessage(MessageAccordingToWeatherDate(chosenDateTime.toLocalDate()) + " Źródło danych: bufor w repozytorium.");
            if (CheckIfHistoricalDate(savedWeather.getWeatherDate())) {  //sprawdzenie czy data dotyczy pogody z przeszłości
                System.out.println("\nWykonano zapytanie o dane historyczne, które było zapisane w repozytorium bazy danych.");
                System.out.println("Dane dla localizationId: " + savedWeather.getLocalization().getId() + ", (" + savedWeather.getLocalization().getCity() + "), na dzień: " + savedWeather.getWeatherDate() + ", źródło danych: bufor w repozytorium.");
            } else if (SavedWeatherIsCurrent(savedWeather) != null) {  //sprawdzenie czy buforowana informacja o pogodzie na przyszłość jest aktualna (w zakresie TTL)
                System.out.println("\nWykonano zapytanie dane pogodowe, które były zapisane w repozytorium i były aktualne.");
                System.out.println("Dane dla localizationId: " + savedWeather.getLocalization().getId() + ", (" + savedWeather.getLocalization().getCity() + "), na dzień: " + savedWeather.getWeatherDate() + ", źródło danych: bufor w repozytorium.");
            } else {
                System.out.println("\nWykonano zapytanie o dane pogodowe, które były zapisane w repozytorium, \n ale się przedawniły, wysyłam zapytanie o aktualne dane do API.");
                System.out.println("Dane dla localizationId: " + savedWeather.getLocalization().getId() + ", (" + savedWeather.getLocalization().getCity() + "), na dzień: " + savedWeather.getWeatherDate() + ", źródło danych: nowe dane z API.");
                var weatherDTO = weatherAPIClient.getWeather(longitude, latitude, dt);
                weatherDTO.setLocalization(myLocalization);
                weatherResponseWithMessageDTO.setWeather(weatherDTO);
                weatherResponseWithMessageDTO.setGeneralMessage(MessageAccordingToWeatherDate(chosenDateTime.toLocalDate()) + ", źródło danych: uaktualnione dane z API.");
                var newExpiryDateTime = LocalDateTime.now().plus(forecastTtl);
                var newWeather = weatherMapper.WeatherFromDTO(weatherDTO);
                newWeather.setExpiryTime(newExpiryDateTime);
                if (newWeather.getTemp() != null) {
                    weatherRepository.save(newWeather);
                }
            }
        } else {
            System.out.println("\nNie znaleziono danych pogodowych w buforze w repozytorium, sprawdzam pogodę w API.");
            var weatherDTO = weatherAPIClient.getWeather(longitude, latitude, dt);
            weatherDTO.setLocalization(myLocalization);
            weatherResponseWithMessageDTO.setWeather(weatherDTO);
            weatherResponseWithMessageDTO.setGeneralMessage(MessageAccordingToWeatherDate(chosenDateTime.toLocalDate()) + " Źródło: nowe dane z API.");
            var newExpiryDateTime = LocalDateTime.now().plus(forecastTtl);
            var newWeather = weatherMapper.WeatherFromDTO(weatherDTO);
            newWeather.setExpiryTime(newExpiryDateTime);
            if (newWeather.getTemp() != null) {
                weatherRepository.save(newWeather);
            }
        }
        return weatherResponseWithMessageDTO;

    }

    // MessageAccordingToWeatherDate: Wygenerowanie odpowiedzi w weatherResponseWithMessageDTO: "General message" w z informacją o typie danych:
    // (prognoza na przyszlość/dane historyczne/poza zakresem 5 dni od dziś)
    public String MessageAccordingToWeatherDate(LocalDate inputDate) {
        var today = LocalDate.now();
        var daysBetween = ChronoUnit.DAYS.between(today, inputDate);
        if (daysBetween < 0) {
            return "Wybrano datę historyczną, prezentowane dane pogodowe z przeszłości.";
        } else if (daysBetween > 5) {
            return "Wybrano zbyt późną datę. Prognoza pogody jest możliwa maksymalnie na 5 dni do przodu.";
        } else {
            return "Aktualna prognoza pogody";
        }
    }

    // CheckDate: Sprawdzenie, czy przekazana data jest z przeszłości, jeśli tak zwraca true.
    public Boolean CheckIfHistoricalDate(LocalDate inputDate) {
        var today = LocalDate.now();
        var daysBetween = ChronoUnit.DAYS.between(today, inputDate);
        return daysBetween < 0;
    }

    // SavedWeatherIsCurrent: Sprawdzenie, czy prognoza w buforze jest aktualna (w zakresie TTL)
    Weather SavedWeatherIsCurrent(Weather savedWeather) {
        var currentDateTime = LocalDateTime.now();
        var savedWeatherExpiryTime = savedWeather.getExpiryTime();
        if (currentDateTime.isAfter(savedWeatherExpiryTime)) {
            return null;
        } else {
            return savedWeather;
        }
    }

}

