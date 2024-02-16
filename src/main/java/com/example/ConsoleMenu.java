package com.example;

import com.example.server.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleMenu {
    private Float stala = 273.15F;
    private WeatherService weatherService;

    public ConsoleMenu(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Cl.printlnC(Cl.BLUEs, 3,"\n1. Wyświetl listę zapisanych miast");
            Cl.printlnC(Cl.BLUEs, 3,"2. Sprawdź pogodę dla dowolnego miasta");
            Cl.printlnC(Cl.BLUEs, 3,"9. Wyjście");

            Cl.printlnC(Cl.VIOLETs, 3,"Wybierz opcję: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    checkWeatherFromList(listCity(scanner));
                    break;
                case "2":
                    checkWeather(scanner);
                case "9":
                    System.out.println("\nZamykanie aplikacji...");
                    System.exit(0);
                default:
                    System.out.println("\nNieznana opcja, spróbuj ponownie.");
            }
        }
    }

    private void checkWeather(Scanner scanner) {
        Cl.printlnC(Cl.BLUEs, 3,"\nPodaj nazwę miasta: ");
        String cityName = scanner.nextLine();

        // Wywołanie metody getWeatherFromApi(cityName) i przechowanie wyniku do wykorzystania
        Optional<WeatherResponse> weatherResponse = weatherService.getWeatherFromApi(cityName);

        // Sprawdza, czy odpowiedź istnieje
        if (weatherResponse.isPresent()) {
            // Przekształcam WeatherResponse na CheckWeather za pomocą metody serwisowej
            CheckWeather checkWeather = weatherService.saveWeatherResponse(weatherResponse.get());
            System.out.println("\nPogoda dla " + cityName + ":");
            DisplayWeather(checkWeather);
        } else {
            Cl.printlnC(Cl.REDs, 3,"\nNie udało się pobrać danych pogodowych dla miasta " + cityName);
        }
    }
    private void checkWeatherFromList(City city) {
       // Wywołanie metody getWeatherFromApi(cityName) i przechowanie wyniku do wykorzystania
        Optional<WeatherResponse> weatherResponse = weatherService.getWeatherFromApi(city.getCityName());

        // Sprawdza, czy odpowiedź istnieje
        if (weatherResponse.isPresent()) {
            // Przekształca WeatherResponse na CheckWeather za pomocą metody serwisowej
            CheckWeather checkWeather = weatherService.saveWeatherResponse(weatherResponse.get());
            System.out.println("\nPogoda dla " + city.getCityName() + ":");
            DisplayWeather(checkWeather);
        } else {
            Cl.printlnC(Cl.REDs, 3,"Nie udało się pobrać danych pogodowych dla miasta " + city.getCityName());
        }
    }

    private City listCity(Scanner scanner) {
        System.out.println("\nLista zapisanych miast:");
        List<City> cityList = weatherService.findAll();

        if (cityList.isEmpty()) {
            Cl.printlnC(Cl.REDs, 3,"\nLista miast jest pusta.");
            return null;
        } else {
            int index = 1;
            for (City city : cityList) {
                System.out.println(index++ + ". " + city);
            }

            System.out.print("\nWybierz numer miasta: ");
            int choice = scanner.nextInt();
               scanner.nextLine();

            // Walidacja wyboru
            if (choice < 1 || choice > cityList.size()) {
                Cl.printlnC(Cl.REDs, 3,"\nNieprawidłowy wybór. Proszę wybrać numer z listy.");
                return null;
            }

            // Zwrócenie wybranego miasta
            return cityList.get(choice - 1);
        }
    }
public void DisplayWeather(CheckWeather weather) {
    Cl.printlnC(Cl.GREENs, 4," \nPogoda na dzień: "+convertTimestampToLocalDateTime(weather.getForecastTimestamp()));
    Cl.printlnC(Cl.GREENs, 3,"Miasto: "+weather.getCity().getCityName()+" ("+weather.getId()+")");
    Cl.printlnC(Cl.GREENs, 3,"Opis: "+weather.getDescription());
    Cl.printlnC(Cl.GREENs, 3,"Temperatura: "+convertTempKelvinToCelsius(weather.getTemp())+"\u00B0C, odczuwalna: "+convertTempKelvinToCelsius(weather.getFeelsLike())+"\u00B0C");
    Cl.printlnC(Cl.GREENs, 3,"Zakres temperatur od: "+convertTempKelvinToCelsius(weather.getTempMin())+"\u00B0C do:"+convertTempKelvinToCelsius(weather.getTempMax())+"\u00B0C");
    Cl.printlnC(Cl.GREENs, 3,"Ciśnienie: "+weather.getPressure()+" hPa");
    Cl.printlnC(Cl.GREENs, 3,"Wilgotność: "+weather.getHumidity()+" %");
    Cl.printlnC(Cl.GREENs, 3,"Prędkość wiatru: "+weather.getWindSpeed()+" m/s");
    Cl.printlnC(Cl.GREENs, 3,"Zachmurzenie: "+weather.getCloudsAll()+" %");
    }
    // Konwersja timestamp UNIX na LocalDateTime
    public LocalDateTime convertTimestampToLocalDateTime(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    public Float convertTempKelvinToCelsius (Float Kelvin) {
        return (Kelvin-stala);
    }

}
