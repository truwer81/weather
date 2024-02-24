package com.example;

import com.example.server.Server;
import com.example.server.localization.WeatherDataQueryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Interface {
    public static void main(String[] args) throws Server.HttpRequestException {
        Server server = new Server();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Witaj w aplikacji pogodowej!");
        ObjectMapper objectMapper = new ObjectMapper();
        while (true) {
            System.out.println("\n");
            Cl.printlnC(Cl.BLUEs, 2, "Co chcesz zrobić:");
            System.out.println(" ");
            System.out.println("1. Dodaj miasto");
            System.out.println("2. Wyświetl listę zapisanych miast");
            System.out.println("3. Sprawdź pogodę dla wybranego miasta");
            Cl.printlnC(Cl.REDs, 2, "0. Wyłącz program");
            Cl.printlnC(Cl.BLUEs, 2, "\n Twój wybór: ");
            int value = scanner.nextInt();
            scanner.nextLine();
            switch (value) {
                case 1:
                    Cl.printlnC(Cl.YELLOWs, 2, "Podaj nazwę miasta:");
                    String city = scanner.nextLine();
                    Cl.printlnC(Cl.YELLOWs, 2, "Podaj kraj:");
                    String country = scanner.nextLine();
                    Cl.printlnC(Cl.YELLOWs, 2, "Podaj region (lub wciśnij enter):");
                    String region = scanner.nextLine();
                    Cl.printlnC(Cl.YELLOWs, 2, "Podaj szerokość geograficzną:");
                    double latitude = scanner.nextDouble();
                    Cl.printlnC(Cl.YELLOWs, 2, "Podaj długość geograficzną:");
                    double longitude = scanner.nextDouble();
                    String json = "{\"city\":\"" + city + "\",\"country\":\"" + country + "\",\"region\":\"" + region + "\",\"latitude\":" + latitude + ",\"longitude\":" + longitude + "}";
                    String responsePOST = server.callServer("POST", "/localizations", json);
                    Cl.printlnC(Cl.BLUEs, 2, "\nZorbione - odpowiedź: " + responsePOST);
                    break;
                case 2:

                    String responseGET = server.callServer("GET", "/localizations", null);
                    try {
                        List<WeatherDataQueryDTO> localizations = objectMapper.readValue(responseGET, new TypeReference<List<WeatherDataQueryDTO>>() {
                        });

                        if (localizations.isEmpty()) {
                            Cl.printlnC(Cl.REDs, 2, "Brak zapisanych miast.");
                        } else {
                            Cl.printlnC(Cl.BLUEs, 2, "\nLista zapisanych miast:");
                            System.out.println("(id: miasto, kraj)");
                            localizations.forEach(localization -> Cl.printlnC(Cl.GREENs, 2, "nr " + localization.getId() + ": " + localization.getCity() + ", " + localization.getCountry()));
                        }
                    } catch (JsonProcessingException e) {
                        Cl.printlnC(Cl.REDs, 2, "Nie udało się przetworzyć odpowiedzi: " + e.getMessage());
                    }
                    break;
                case 3:
                    String responseGetLocalizations = server.callServer("GET", "/localizations", null);

                    try {
                        List<WeatherDataQueryDTO> localizations = objectMapper.readValue(responseGetLocalizations, new TypeReference<List<WeatherDataQueryDTO>>() {
                        });
                        if (localizations.isEmpty()) {
                            Cl.printlnC(Cl.REDs, 2, "Brak zapisanych miast.");
                        } else {
                            Cl.printlnC(Cl.BLUEs, 2, "\nLista zapisanych miast:");
                            localizations.forEach(localization -> Cl.printlnC(Cl.GREENs, 2, "nr " + localization.getId() + ": " + localization.getCity() + ", " + localization.getCountry()));

                            // Pytanie o wybór miasta
                            Cl.printlnC(Cl.BLUEs, 2, "\nWybierz id miasta dla którego chcesz sprawdzić pogodę:");
                            Long cityId = scanner.nextLong();
                            scanner.nextLine(); // Oczyszczenie bufora

                            String dateString = "2024-02-24";
                            // Pytanie o datę - ta funkcja wymaga subskrypcji, w razie wykupienia można ją przywrócić.
                            //Cl.printlnC(Cl.BLUEs, 2, "Podaj datę (RRRR-MM-DD):");
                            //String dateString = scanner.nextLine();

                            String path = "/weather?localization=" + cityId + "&date=" + dateString;
                            String weatherResponse = server.callServer("GET", path, null);

                            Optional<WeatherDataQueryDTO> mycity = localizations.stream()
                                    .filter(localization -> cityId == localization.getId().longValue())
                                    .findFirst();
                            mycity.ifPresent(myCity -> Cl.printlnC(Cl.GREENs, 2, "\nPogoda w lokalizacji: " + myCity.getCity()));
                            Cl.printlnC(Cl.GREENs, 3, weatherResponse);
                        }
                    } catch (JsonProcessingException e) {
                        Cl.printlnC(Cl.REDs, 2, "Nie udało się przetworzyć odpowiedzi: " + e.getMessage());
                    }
                    break;

                case 0:
                    Cl.printlnC(Cl.BLUEs, 2, "Do widzenia!");
                    return;
            }
        }
    }
}
