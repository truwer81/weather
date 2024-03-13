package com.example.server.localization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class LocalizationServiceTest {

    @InjectMocks
    LocalizationService localizationService;
    @Mock
    LocalizationRepository localizationRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
        when(localizationRepository.save(any(Localization.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    void createLocalization_whenLongitudeIsLessThanMinus180_throwsAnException() {
        // Given
        String city = "Test_City";
        Float longitude = -181F;
        Float latitude = 1F;
        String country = "Test_Country";
        String region = "Test_region";
        // When
        assertThrows(IllegalArgumentException.class, () -> localizationService.createLocalization(city, longitude, latitude, region, country));
        // Then
    }

    @Test
    void createLocalization_whenLongitudeIsGraterThan180_throwsAnException() {
        // Given
        String city = "Test_City";
        Float longitude = 181F;
        Float latitude = 1F;
        String country = "Test_Country";
        String region = "Test_region";
        // When
        assertThrows(IllegalArgumentException.class, () -> localizationService.createLocalization(city, longitude, latitude, region, country));
        // Then
    }

    @Test
    void createLocalization_whenLatitudeIsLessThanMinus90_throwsAnException() {
        // Given
        String city = "Test_City";
        Float longitude = 1F;
        Float latitude = -91F;
        String country = "Test_Country";
        String region = "Test_region";
        // When
        assertThrows(IllegalArgumentException.class, () -> localizationService.createLocalization(city, longitude, latitude, region, country));
        // Then
    }

    @Test
    void createLocalization_whenLatitudeIsGraterThan90_throwsAnException() {
        // Given
        String city = "Test_City";
        Float longitude = 1F;
        Float latitude = 91F;
        String country = "Test_Country";
        String region = "Test_region";
        // When
        assertThrows(IllegalArgumentException.class, () -> localizationService.createLocalization(city, longitude, latitude, region, country));
        // Then
    }

    @Test
    void createLocalization_whenLongitudeIsNull_throwsAnException() {
        // Given
        String city = "Test_City";
        Float longitude = null;
        Float latitude = 1F;
        String country = "Test_Country";
        String region = "Test_region";
        // When
        assertThrows(IllegalArgumentException.class, () -> localizationService.createLocalization(city, longitude, latitude, region, country));
        // Then
    }

    @Test
    void createLocalization_whenLatitudeIsNull_throwsAnException() {
        // Given
        String city = "Test_City";
        Float longitude = 1F;
        Float latitude = null;
        String country = "Test_Country";
        String region = "Test_region";
        // When
        assertThrows(IllegalArgumentException.class, () -> localizationService.createLocalization(city, longitude, latitude, region, country));
        // Then
    }

    @Test
    void createLocalization_withNullRegion_returnsLocalizationObject() {
        // Given
        String city = "Test_City";
        Float longitude = 1F;
        Float latitude = 1F;
        String country = "Test_Country";
        String region = null;
        // When
        Localization result = localizationService.createLocalization(city, longitude, latitude, region, country);
        // Then
        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertEquals(longitude, result.getLongitude());
        assertEquals(latitude, result.getLatitude());
        assertEquals(country, result.getCountry());
        assertNull(result.getRegion());
    }

    @Test
    void createLocalization_withSpacesAsRegion_treatsItAsNull() {
        // Given
        String city = "Test_City";
        Float longitude = 1F;
        Float latitude = 1F;
        String country = "Test_Country";
        String region = " ";
        // When
        Localization result = localizationService.createLocalization(city, longitude, latitude, region, country);
        // Then
        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertEquals(longitude, result.getLongitude());
        assertEquals(latitude, result.getLatitude());
        assertNull(result.getRegion(), "Region with space should be treated as null");
        assertEquals(country, result.getCountry());

    }

    @Test
    void createLocalization_withNullAsCity_throwsAnException() {
        // Given
        String city = null;
        Float longitude = 1F;
        Float latitude = 1F;
        String country = "Test_Country";
        String region = "Test_region";
        // When
        assertThrows(IllegalArgumentException.class, () -> localizationService.createLocalization(city, longitude, latitude, region, country));
        // Then
    }
}
