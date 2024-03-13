package com.example.server.localization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LocalizationServiceIntegrationTest {

    @Autowired
    LocalizationService localizationService;
    @Autowired
    LocalizationRepository localizationRepository;

    @Test
    void createLocalization_addsNewLocalization() {
        // Given
        localizationRepository.deleteAll();
        // When
        localizationService.createLocalization("city", 1, 1, "region", "country");
        // Then
        List<Localization> result = localizationRepository.findAll();
        assertEquals(result.size(), 1);
    }
}
