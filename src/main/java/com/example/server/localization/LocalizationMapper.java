package com.example.server.localization;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocalizationMapper {

    public LocalizationDTO asDTO(Localization localization) {
        return new LocalizationDTO(
                localization.getId(),
                localization.getCity(),
                localization.getCountry(),
                localization.getRegion(),
                localization.getLongitude(),
                localization.getLatitude(),
                null
        );
    }

    public List<LocalizationDTO> asDTO(List<Localization> localizations) {
        return localizations.stream()
                .map(this::asDTO)
                .toList();
    }
}
