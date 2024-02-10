package com.example.server;

import java.util.List;

public interface AnimalRepository {

    List<Animal> getAnimals();

    void createAnimal(Animal animal);
}
