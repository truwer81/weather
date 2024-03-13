package com.example.server.localization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.openMocks;

public class LocalizationServiceTest {

    @InjectMocks
    LocalizationService localizationService;
    @Mock
    LocalizationRepository localizationRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void createLocalization_whenLongitudeIsLessThanMinus180_throwsAnException() {
        // Given
        // When
        assertThrows(IllegalArgumentException.class, () -> localizationService.createLocalization("city", -181, 1, "region", "country"));
        // Then
    }

    @Test
    void createLocalization_whenLongitudeIsGraterThan180_throwsAnException() {
        // Given
        // When
        assertThrows(IllegalArgumentException.class, () -> localizationService.createLocalization("city", 181, 1, "region", "country"));
        // Then
    }
}
