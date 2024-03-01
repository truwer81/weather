package com.example.server.localization;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LocalizationController {

    private final LocalizationService localizationService;

    @PostMapping("/localizations")
    public ResponseEntity<WeatherDataQueryDTO> createLocalization(@RequestBody WeatherDataQueryDTO model) {
        String city = model.getCity();
        float longitude = model.getLongitude();
        float latitude = model.getLatitude();
        String region = model.getRegion();
        String country = model.getCountry();
        Localization localization = localizationService.createLocalization(city, longitude, latitude, region, country);
        WeatherDataQueryDTO weatherDataQueryDTO = asDTO(localization);
        return ResponseEntity.status(HttpStatus.OK)
                .body(weatherDataQueryDTO);
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
                localization.getLatitude(),
                null
        );
    }
}
