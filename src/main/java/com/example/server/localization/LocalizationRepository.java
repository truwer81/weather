package com.example.server.localization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalizationRepository extends JpaRepository<Localization, Long> {

    List<Localization> findAllByCountry(String country);


    Localization findOneById(Float id);

    //Void save(String city, Float longitude, Float latitude, String  region,String  country);
}