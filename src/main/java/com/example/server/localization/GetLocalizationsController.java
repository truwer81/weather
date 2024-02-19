package com.example.server.localization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;


// @RequiredArgsConstructor - zamiast konstuktora
public class GetLocalizationsController {

    private final ObjectMapper objectMapper;
    private final GetLocalizationsService getLocalizationsService;

    public GetLocalizationsController(GetLocalizationsService getLocalizationsService, ObjectMapper objectMapper) {
        this.getLocalizationsService = getLocalizationsService;
        this.objectMapper = objectMapper;
    }

    // GET /localizations
    public String getLocalizations() throws JsonProcessingException {
        List<Localization> localizations = getLocalizationsService.getAllLocalizations();
        return objectMapper.writeValueAsString(localizations.stream().map(this::asDTO).collect(Collectors.toList()));
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
