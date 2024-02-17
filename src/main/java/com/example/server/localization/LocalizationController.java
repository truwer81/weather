package com.example.server.localization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// @RequiredArgsConstructor - zamiast konstuktora
public class LocalizationController {

    private final ObjectMapper objectMapper;
    private final LocalizationService localizationService;

    public LocalizationController(ObjectMapper objectMapper, LocalizationService localizationService) {
        this.objectMapper = objectMapper;
        this.localizationService = localizationService;
    }

    // POST /localizations
    public String createLocalization(String json) {
        try {
            LocalizationDTO model = objectMapper.readValue(json, LocalizationDTO.class);
            String city = model.getCity();
            float longitude = model.getLongitude();
            float latitude = model.getLatitude();
            String region = model.getRegion();
            String country = model.getCountry();
            Localization localization = localizationService.createLocalization(city, longitude, latitude, region, country);
            LocalizationDTO localizationDTO = asDTO(localization);
            return objectMapper.writeValueAsString(localizationDTO);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return "{\"error\": \"Invalid data\"}"; // http 400
        } catch (Exception e) {
            return "{\"error\": \"Internal server error\"}"; // http 500
        }
    }

    public LocalizationDTO asDTO(Localization localization) {
        return new LocalizationDTO(
                localization.getId(),
                localization.getCity(),
                localization.getCountry(),
                localization.getRegion(),
                localization.getLongitude(),
                localization.getLatitude()
        );
    }
}
