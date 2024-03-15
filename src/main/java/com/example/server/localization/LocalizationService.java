package com.example.server.localization;

import com.example.server.exception.NoLocalizationFoundException;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalizationService {

    private final LocalizationRepository localizationRepository;

    public LocalizationService(LocalizationRepository localizationRepository) {
        this.localizationRepository = localizationRepository;
    }

    public Localization createLocalization(String city, Float longitude, Float latitude, String region, String country) {
        if (longitude == null || latitude == null) {
            throw new IllegalArgumentException("Longitude and latitude cannot be null");
        }
        if (longitude < -180 || longitude > 180 || latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Invalid longitude or latitude");
        }
        if (city == null || city.isBlank() || country == null || country.isBlank()) {
            throw new IllegalArgumentException("Invalid city or country");
        }
        if (region != null && region.isBlank()) {
            region = null;
        }

        var localization = new Localization(null, city, country, region, longitude, latitude);

        return localizationRepository.save(localization);
    }

    public List<Localization> getAllLocalizations() {
        return localizationRepository.findAll();
    }

    public Localization getLocalization(long localizationId) {
        try {
            return localizationRepository.findById(localizationId).orElseThrow(() -> new NoLocalizationFoundException(localizationId));
        } catch (NoResultException e) {
            throw new NoLocalizationFoundException(localizationId);
        }
    }
}