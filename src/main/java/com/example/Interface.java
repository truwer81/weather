package com.example;

import com.example.server.Server;

import java.util.Scanner;

public class Interface {

    public static void main(String[] args) {
        var server = new Server();

        var scanner = new Scanner(System.in);
        System.out.println("Wpisz imie psa: ");
        var name = scanner.next();
        System.out.println("Wpisz wiek psa: ");
        var age = scanner.nextInt();
        var createRequest = "{\"name\": \"" + name + "\", \"age\": \"" + age + "\"}";
        var response = server.callServer("POST", "/animals", createRequest);
        System.out.println("Odpowiedź z serwera: " + response);

        System.out.println("Informacje o wszystkich psach:");
        response = server.callServer("GET", "/animals", null);
        System.out.println("Odpowiedź z serwera: " + response);
    }
}
