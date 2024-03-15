package com.example.server.weather;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    Optional<Weather> findFirstByLocalizationIdAndWeatherDateOrderByExpiryTimeDesc(Long localizationId, LocalDate weatherDate);
}