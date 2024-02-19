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
            System.out.println("\n");
            Cl.printlnC(Cl.BLUEs, 2,"Co chcesz zrobić:");
            System.out.println(" ");
            System.out.println("1. Dodaj miasto");
            System.out.println("2. Wyświetl listę zapisanych miast");
            Cl.printlnC(Cl.REDs, 2,"0. Wyłącz program");
            Cl.printlnC(Cl.BLUEs, 2,"\n Twój wybór: ");
            int value = scanner.nextInt();
            scanner.nextLine();
            switch (value) {
                case 1:
                    Cl.printlnC(Cl.YELLOWs, 2,"Podaj nazwę miasta:");
                    String city = scanner.nextLine();
                    Cl.printlnC(Cl.YELLOWs, 2,"Podaj kraj:");
                    String country = scanner.nextLine();
                    Cl.printlnC(Cl.YELLOWs, 2,"Podaj region (lub wciśnij enter):");
                    String region = scanner.nextLine();
                    Cl.printlnC(Cl.YELLOWs, 2,"Podaj szerokość geograficzną:");
                    double latitude = scanner.nextDouble();
                    Cl.printlnC(Cl.YELLOWs, 2,"Podaj długość geograficzną:");
                    double longitude = scanner.nextDouble();
                    String json = "{\"city\":\"" + city + "\",\"country\":\"" + country + "\",\"region\":\"" + region + "\",\"latitude\":" + latitude + ",\"longitude\":" + longitude + "}";
                    String responsePOST = server.callServer("POST", "/localizations", json);
                    Cl.printlnC(Cl.BLUEs, 2,"\nZorbione - odpowiedź: " + responsePOST);
                    break;
                case 2:
                    ObjectMapper objectMapper = new ObjectMapper();
                    String responseGET = server.callServer("GET", "/localizations", null);

                    try {
                        List<LocalizationDTO> localizations = objectMapper.readValue(responseGET, new TypeReference<List<LocalizationDTO>>(){});

                        if(localizations.isEmpty()) {
                            Cl.printlnC(Cl.REDs, 2,"Brak zapisanych miast.");
                        } else {
                            Cl.printlnC(Cl.BLUEs, 2,"\nLista zapisanych miast:");
                            System.out.println("(id: miasto, kraj)");
                            localizations.forEach(localization -> Cl.printlnC(Cl.GREENs, 2,"nr " +localization.getId()+": " + localization.getCity() + ", " + localization.getCountry()));
                        }
                    } catch (JsonProcessingException e) {
                        Cl.printlnC(Cl.REDs, 2,"Nie udało się przetworzyć odpowiedzi: " + e.getMessage());
                    }
                    break;


                case 0:
                    Cl.printlnC(Cl.BLUEs, 2,"Do widzenia!");
                    return;
            }
        }
    }
}
