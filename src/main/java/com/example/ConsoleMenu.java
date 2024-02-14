package com.example;

import com.example.server.WeatherService;
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
        weatherService.getWeather(cityName);
        System.out.println("Pogoda dla " + cityName + ":");
        System.out.println(weatherService.getWeather(cityName));


    }
}
