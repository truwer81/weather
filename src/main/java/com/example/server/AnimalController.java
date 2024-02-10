package com.example.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AnimalController {

    private final AnimalService animalService;
    private final ObjectMapper objectMapper;

    public AnimalController(AnimalService animalService, ObjectMapper objectMapper) {
        this.animalService = animalService;
        this.objectMapper = objectMapper;
    }

    // GET: /animals
    public String getAnimals() throws JsonProcessingException {
        var animals = animalService.getAnimals();
        return objectMapper.writeValueAsString(animals); // 200 OK
    }

    // POST: /animals
    public void createAnimals(String json) throws JsonProcessingException {
        var animal = objectMapper.readValue(json, Animal.class);
        animalService.createAnimal(animal);
        // 201 CREATED
    }
}
