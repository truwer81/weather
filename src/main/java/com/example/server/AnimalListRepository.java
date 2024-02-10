package com.example.server;

import java.util.ArrayList;
import java.util.List;

public class AnimalListRepository implements AnimalRepository {

    private static final List<Animal> animals = new ArrayList<>();
    private static Long counter = 1L;

    public AnimalListRepository() {
        animals.add(new Animal(counter++, "Reksio", 4));
        animals.add(new Animal(counter++, "Sonia", 5));
    }

    @Override
    public List<Animal> getAnimals() {
        return animals;
    }

    @Override
    public void createAnimal(Animal animal) {
        animal.setId(counter++);
        animals.add(animal);
    }
}
