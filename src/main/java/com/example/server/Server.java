package com.example.server;

import com.example.server.localization.LocalizationController;
import com.example.server.localization.LocalizationRepository;
import com.example.server.localization.LocalizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;

import java.net.http.HttpClient;
import java.util.Objects;

public class Server {

    private final LocalizationController localizationController;

    public Server() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        LocalizationRepository localizationRepository = new LocalizationRepository(sessionFactory);
        LocalizationService localizationService = new LocalizationService(localizationRepository);
        LocalizationController localizationController = new LocalizationController(objectMapper, localizationService);
        this.localizationController = localizationController;
    }

    // mapujemy requesty HTTP na metody kontrolera
    public String callServer(String method, String path, String json) {
        if (Objects.equals(method, "POST") && path.startsWith("/localizations")) {
            return localizationController.createLocalization(json);
        }
        return "404";
    }
}
