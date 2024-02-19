package com.example.server;

import com.example.server.localization.LocalizationController;
import com.example.server.localization.LocalizationRepository;
import com.example.server.localization.LocalizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import com.example.server.localization.GetLocalizationsService;
import com.example.server.localization.GetLocalizationsRepository;
import com.example.server.localization.GetLocalizationsController;

import java.net.http.HttpClient;
import java.util.Objects;

public class Server {

    private final LocalizationController localizationController;
    private final GetLocalizationsController getLocalizationsController;

    public Server() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

        // Inicjalizacja dla dodawania lokalizacji
        LocalizationRepository localizationRepository = new LocalizationRepository(sessionFactory);
        LocalizationService localizationService = new LocalizationService(localizationRepository);
        LocalizationController localizationController = new LocalizationController(objectMapper, localizationService);
        this.localizationController = localizationController;

        // Inicjalizacja dla pobierania lokalizacji
        GetLocalizationsRepository getLocalizationsRepository = new GetLocalizationsRepository(sessionFactory);
        GetLocalizationsService getLocalizationsService = new GetLocalizationsService(getLocalizationsRepository);
        this.getLocalizationsController = new GetLocalizationsController(getLocalizationsService, objectMapper);
    }

    // mapujemy requesty HTTP na metody kontrolera
    public String callServer(String method, String path, String json) throws JsonProcessingException {
        if (Objects.equals(method, "POST") && path.startsWith("/localizations")) {
            return localizationController.createLocalization(json);
        } else if (Objects.equals(method, "GET") && path.startsWith("/localizations")) {
            return getLocalizationsController.getLocalizations();
        }
        return "404";
    }
}
