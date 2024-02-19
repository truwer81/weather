package com.example.server.localization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

// @RequiredArgsConstructor - zamiast konstuktora
public class LocalizationController {

    private final ObjectMapper objectMapper;
    private final LocalizationService localizationService;
private final GetLocalizationsService getLocalizationsService;

    public LocalizationController(ObjectMapper objectMapper, LocalizationService localizationService, GetLocalizationsService getLocalizationsService) {
        this.objectMapper = objectMapper;
        this.localizationService = localizationService;
        this.getLocalizationsService = getLocalizationsService;
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
    // GET /localizations
    public String getLocalizations() throws JsonProcessingException {
        try{
            List<Localization> localizations = getLocalizationsService.getAllLocalizations();
            return objectMapper.writeValueAsString(localizations
                    .stream().map(this::asDTO)
                    .collect(Collectors
                            .toList()));
        } catch (JsonProcessingException e) {
            return "{\"error\": \"Internal server error\"}"; //http 400
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
