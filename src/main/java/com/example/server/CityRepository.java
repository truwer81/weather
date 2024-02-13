package com.example.server;
import java.util.List;
import java.util.Optional;

public interface CityRepository {
    List<City> findAll();
    Optional<City> findByCityName(String name);

}
