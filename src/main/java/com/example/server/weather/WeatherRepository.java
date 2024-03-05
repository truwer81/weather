package com.example.server.weather;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    List<Weather> findByLocalizationIdAndWeatherDateOrderByExpiryTimeDesc(Long localizationId, LocalDate weatherDate);

}