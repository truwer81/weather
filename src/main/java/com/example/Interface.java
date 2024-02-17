package com.example;

import com.example.server.Server;

import java.util.Scanner;

public class Interface {

    public static void main(String[] args) {
        Server server = new Server();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Witaj w aplikacji pogodowej!");

        while (true) {
            System.out.println("Co chcesz zrobić:");
            System.out.println("1. Dodaj miasto");
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
                    String response = server.callServer("POST", "/localizations", json);
                    System.out.println("Zorbione - odpowiedź: " + response);
                    break;
                case 0:
                    System.out.println("Do widzenia!");
                    return;
            }
        }
    }
}
