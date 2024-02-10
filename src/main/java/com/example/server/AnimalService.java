package com.example.server;

import java.util.List;

public class AnimalService {

    private final AnimalRepository animalRepository;
    private final TimeClient timeClient;

    public AnimalService(AnimalRepository animalRepository, TimeClient timeClient) {
        this.animalRepository = animalRepository;
        this.timeClient = timeClient;
    }

    public List<Animal> getAnimals() {
        return animalRepository.getAnimals();
    }

    public void createAnimal(Animal animal) {
        var age = animal.getAge();
        if (age < 0) {
            throw new IllegalArgumentException("Wiek nie może być ujemny: " + age);
        }

        var time = timeClient.getTime();
        animal.setCreatedDate(time);

        animalRepository.createAnimal(animal);
    }
}
