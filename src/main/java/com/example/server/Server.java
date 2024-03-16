package com.example.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
public class Server implements CommandLineRunner {


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

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello world");
    }
}
