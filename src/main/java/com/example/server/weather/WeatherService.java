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
        Timestamp dt = Timestamp.valueOf(chosenDateTime);
        Localization myLocalization = new Localization();
        if (localization != null) {
            myLocalization = localization;
        } else {
            throw new NoLocalizationFoundException(localizationId);
        }
        Float longitude = myLocalization.getLongitude();
        Float latitude = myLocalization.getLatitude();
        Optional<Weather> savedWeatherOptional = weatherRepository.findByLocalizationIdAndWeatherDateOrderByExpiryTimeDesc(localizationId, weatherDate).stream().findFirst();
        Weather savedWeather = savedWeatherOptional.orElse(null);

        if (savedWeather != null) {
            savedWeather.setLocalization(myLocalization);
            weatherResponseWithMessageDTO.setWeather(WeatherToDTO(savedWeather));
            weatherResponseWithMessageDTO.setGeneralMessage(MessageAccordingToWeatherDate(chosenDateTime.toLocalDate()) + " Źródło: bufor.");
            if (CheckDate(savedWeather.getWeatherDate())) {  //sprawdzenie czy data dotyczy pogody z przeszłości
                System.out.println("\nZapytanie o dane historyczne, które było zapisane w buforze bazy danych.");
                System.out.println("Dane dla localizationId: " + savedWeather.getLocalization().getId() + ", (" + savedWeather.getLocalization().getCity() + "), na dzień: " + savedWeather.getWeatherDate() + ", źródło danych: repozytorium.");
            } else if (SavedWeatherIsCurrent(savedWeather) != null) {  //sprawdzenie czy buforowana informacja o pogodzie na przyszłość jest aktualna (w zakresie TTL)
                System.out.println("\nWykonano zapytanie o aktualne dane, które były zapisane w buforze.");
                System.out.println("Dane dla localizationId: " + savedWeather.getLocalization().getId() + ", (" + savedWeather.getLocalization().getCity() + "), na dzień: " + savedWeather.getWeatherDate() + ", źródło danych: repozytorium.");
            } else {
                System.out.println("\nDane pogodowe w repozytorium się przedawniły, wysyłam zapytanie o aktualne dane do API.");
                System.out.println("Dane dla localizationId: " + savedWeather.getLocalization().getId() + ", (" + savedWeather.getLocalization().getCity() + "), na dzień: " + savedWeather.getWeatherDate() + ", źródło danych: API.");
                WeatherDTO weatherDTO = weatherAPIClient.getWeather(longitude, latitude, dt);
                weatherDTO.setLocalization(myLocalization);
                weatherResponseWithMessageDTO.setWeather(weatherDTO);
                weatherResponseWithMessageDTO.setGeneralMessage(MessageAccordingToWeatherDate(chosenDateTime.toLocalDate()) + ". Źródło: uaktualnione dane z API.");
                LocalDateTime newExpiryDateTime = LocalDateTime.now().plus(forecastTtl);
                Weather newWeather = WeatherFromDTO(weatherDTO);
                newWeather.setExpiryTime(newExpiryDateTime);
                if (newWeather.getTemp() != null) {
                    weatherRepository.save(newWeather);
                }
            }
        } else {
            System.out.println("\nNie znaleziono danych pogodowych z repozytorium, sprawdzam pogodę w API.");
            WeatherDTO weatherDTO = weatherAPIClient.getWeather(longitude, latitude, dt);
            weatherDTO.setLocalization(myLocalization);
            weatherResponseWithMessageDTO.setWeather(weatherDTO);
            weatherResponseWithMessageDTO.setGeneralMessage(MessageAccordingToWeatherDate(chosenDateTime.toLocalDate()) + " Źródło: nowe dane z API.");
            LocalDateTime newExpiryDateTime = LocalDateTime.now().plus(forecastTtl);
            Weather newWeather = WeatherFromDTO(weatherDTO);
            newWeather.setExpiryTime(newExpiryDateTime);
            if (newWeather.getTemp() != null) {
                weatherRepository.save(newWeather);
            }
        }
        return weatherResponseWithMessageDTO;

    }

    // WeatherDTO: przepisanie encji Weather na obiekt DTO
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

    // WeatherFromDTO: przepisanie obiektu WeatherDTO na encję bazy danych Weather
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

    // MessageAccordingToWeatherDate: Wygenerowanie odpowiedzi w weatherResponseWithMessageDTO: "General message" w z informacją o typie danych:
    // (prognoza na przyszlość/dane historyczne/poza zakresem 5 dni od dziś)
    public String MessageAccordingToWeatherDate(LocalDate inputDate) {
        LocalDate today = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(today, inputDate);
        if (daysBetween < 0) {
            return "Wybrano datę historyczną, prezentowane dane pogodowe z przeszłości.";
        } else if (daysBetween > 5) {
            return "Wybrano zbyt późną datę. Prognoza pogody jest możliwa maksymalnie na 5 dni do przodu.";
        } else {
            return "Aktualna prognoza pogody.";
        }
    }

    // CheckDate: Sprawdzenie, czy przekazana data jest z przeszłości, jeśli tak zwraca true.
    public Boolean CheckDate(LocalDate inputDate) {
        LocalDate today = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(today, inputDate);
        return daysBetween < 0;
    }

    // SavedWeatherIsCurrent: Sprawdzenie, czy prognoza w buforze jest aktualna (w zakresie TTL)
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

