package com.example.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.util.Objects;

public class Server {

    private final AnimalController animalController;

    public Server() {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var httpClient = HttpClient.newHttpClient();
        var sessionFactory = HibernateUtils.getSessionFactory();
        var animalRepository = new AnimalHibernateRepository(sessionFactory);
        var timeClient = new TimeClient(httpClient, objectMapper);
        var animalService = new AnimalService(animalRepository, timeClient);
        this.animalController = new AnimalController(animalService, objectMapper);
    }

    public String callServer(String method, String path, String json) {
        try {
            if (Objects.equals(method, "GET") && Objects.equals(path, "/animals")) {
                return "200 " + animalController.getAnimals();
            }
            if (Objects.equals(method, "POST") && Objects.equals(path, "/animals")) {
                animalController.createAnimals(json);
                return "201";
            }
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return "400";
        } catch (Exception e) {
            return "500";
        }

        return "";
    }
}
