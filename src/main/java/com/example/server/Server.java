package com.example.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {


    // https://start.spring.io
    // https://junit.org/junit5
    // https://joel-costigliola.github.io/assertj

    // testy jednostkowe + integracyjne SpringBoot
    // ctrl ctrl -> mvn clean test
    // napisać testy jednostkowe dla klasy LocalizationService -> createLocalization
    // napisać dowolnyu test integracyjny z @SpringBootTest

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
