package com.example;

import com.example.server.CheckWeather;
import com.example.server.WeatherResponse;
import com.example.server.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.Scanner;

public class ConsoleMenu {

    private WeatherService weatherService;





    public ConsoleMenu(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Sprawdź pogodę dla miasta");
            System.out.println("2. Wyjście");

            System.out.print("Wybierz opcję: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    checkWeather(scanner);
                    break;
                case "2":
                    System.out.println("Zamykanie aplikacji...");
                    System.exit(0);
                default:
                    System.out.println("Nieznana opcja, spróbuj ponownie.");
            }
        }
    }

    private void checkWeather(Scanner scanner) {
        System.out.print("Podaj nazwę miasta: ");
        String cityName = scanner.nextLine();

        // Wywołanie metody getWeatherFromApi(cityName) i przechowanie wyniku do wykorzystania
        Optional<WeatherResponse> weatherResponse = weatherService.getWeatherFromApi(cityName);

        // Sprawdzamy, czy odpowiedź istnieje
        if (weatherResponse.isPresent()) {
            // Przekształcam WeatherResponse na CheckWeather za pomocą metody serwisowej
            CheckWeather checkWeather = weatherService.saveWeatherResponse(weatherResponse.get());

            // Teraz możemy wywołać toString() na obiekcie CheckWeather
            System.out.println("Pogoda dla " + cityName + ":");
            System.out.println(checkWeather.toString());
        } else {
            System.out.println("Nie udało się pobrać danych pogodowych dla miasta " + cityName);
        }
    }


}
