package com.example.server.localization;

import java.util.List;

public class GetLocalizationsService {
    private final GetLocalizationsRepository getLocalizationsRepository;

    public GetLocalizationsService(GetLocalizationsRepository getLocalizationsRepository) {
        this.getLocalizationsRepository = getLocalizationsRepository;
    }

    public List<Localization> getAllLocalizations() {
        return getLocalizationsRepository.findAll();
    }
}
