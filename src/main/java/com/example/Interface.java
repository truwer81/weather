package com.example;

import com.example.server.Server;
import com.example.server.localization.LocalizationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Scanner;

public class Interface {

    public static void main(String[] args) throws JsonProcessingException {
        Server server = new Server();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Witaj w aplikacji pogodowej!");

        while (true) {
            System.out.println("Co chcesz zrobić:");
            System.out.println("1. Dodaj miasto");
            System.out.println("2. Wyświetl listę zapisanych miast");
            System.out.println("0. Wyłącz program");
            int value = scanner.nextInt();
            scanner.nextLine();
            switch (value) {
                case 1:
                    System.out.println("Podaj nazwę miasta:");
                    String city = scanner.nextLine();
                    System.out.println("Podaj kraj:");
                    String country = scanner.nextLine();
                    System.out.println("Podaj region (lub wciśnij enter):");
                    String region = scanner.nextLine();
                    System.out.println("Podaj szerokość geograficzną:");
                    double latitude = scanner.nextDouble();
                    System.out.println("Podaj długość geograficzną:");
                    double longitude = scanner.nextDouble();
                    String json = "{\"city\":\"" + city + "\",\"country\":\"" + country + "\",\"region\":\"" + region + "\",\"latitude\":" + latitude + ",\"longitude\":" + longitude + "}";
                    String responsePOST = server.callServer("POST", "/localizations", json);
                    System.out.println("Zorbione - odpowiedź: " + responsePOST);
                    break;
                case 2:
                    ObjectMapper objectMapper = new ObjectMapper();
                    String responseGET = server.callServer("GET", "/localizations", null);

                    try {
                        List<LocalizationDTO> localizations = objectMapper.readValue(responseGET, new TypeReference<List<LocalizationDTO>>(){});

                        if(localizations.isEmpty()) {
                            System.out.println("Brak zapisanych miast.");
                        } else {
                            localizations.forEach(localization -> System.out.println("Miasto: " + localization.getCity() + ", Kraj: " + localization.getCountry()));
                        }
                    } catch (JsonProcessingException e) {
                        System.err.println("Nie udało się przetworzyć odpowiedzi: " + e.getMessage());
                    }
                    break;


                case 0:
                    System.out.println("Do widzenia!");
                    return;
            }
        }
    }
}
