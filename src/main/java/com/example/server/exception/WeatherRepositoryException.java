package com.example.server.exception;

public class WeatherRepositoryException extends RuntimeException {

    public WeatherRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherRepositoryException(String message) {
        super(message);
    }
}