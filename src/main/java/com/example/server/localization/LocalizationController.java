package com.example.server.localization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocalizationController {

    private ObjectMapper objectMapper;
    private LocalizationService localizationService;

    public LocalizationController(ObjectMapper objectMapper, LocalizationService localizationService) {
        this.objectMapper = objectMapper;
        this.localizationService = localizationService;
    }

    @PostMapping("/localizations")
    public WeatherDataQueryDTO createLocalization(@RequestBody WeatherDataQueryDTO model) {
        String city = model.getCity();
        float longitude = model.getLongitude();
        float latitude = model.getLatitude();
        String region = model.getRegion();
        String country = model.getCountry();
        Localization localization = localizationService.createLocalization(city, longitude, latitude, region, country);
        WeatherDataQueryDTO weatherDataQueryDTO = asDTO(localization);
        return weatherDataQueryDTO;
    }

    @GetMapping("/localizations")
    public List<Localization> getLocalizations() {
        List<Localization> localizations = localizationService.getAllLocalizations();
        return localizations;
    }

    @GetMapping("/localizations/{localizationId}")
    public Localization getLocalization(@PathVariable long localizationId) {
        Localization localizations = localizationService.getLocalization(localizationId);
        return localizations;
    }

    public WeatherDataQueryDTO asDTO(Localization localization) {
        return new WeatherDataQueryDTO(
                localization.getId(),
                localization.getCity(),
                localization.getCountry(),
                localization.getRegion(),
                localization.getLongitude(),
                localization.getLatitude()
        );
    }
}
