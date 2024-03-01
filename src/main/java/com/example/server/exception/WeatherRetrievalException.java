package com.example.server.exception;

public class WeatherRetrievalException extends RuntimeException {

    public WeatherRetrievalException(String message, Throwable cause) {
        super(message, cause); // Przekazanie do klasy bazowej Exception
    }

    public WeatherRetrievalException(String message) {
        super(message);
    }
}