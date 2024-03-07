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

    public Localization createLocalization(String city, float longitude, float latitude, String region, String country) {
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
            return localizationRepository.findById(localizationId).orElse(null);
        } catch (NoResultException e) {
            throw new NoLocalizationFoundException(localizationId);
        }
    }
}