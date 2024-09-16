package com.example.server.localization;

import com.example.server.exception.NoLocalizationFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LocalizationServiceIntegrationTest {

    @Autowired
    LocalizationService localizationService;
    @Autowired
    LocalizationRepository localizationRepository;

    @Test
    void createLocalization_addsNewLocalization() {
        // Given
        String city = "Test_City";
        Float longitude = 1F;
        Float latitude = 1F;
        String country = "Test_Country";
        String region = "Test_region";
        localizationRepository.deleteAll();
        // When
        localizationService.createLocalization(city, longitude, latitude, region, country);
        // Then
        List<Localization> result = localizationRepository.findAll();
        assertEquals(result.size(), 1);
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
    void getLocalization_returnsCorrectLocalization() {
        // Given
        String city = "Test_City";
        Float longitude = 1F;
        Float latitude = 1F;
        String country = "Test_Country";
        String region = "Test_region";
        localizationRepository.deleteAll();
        Localization savedLocalization = localizationService.createLocalization(city, longitude, latitude, region, country);

        // When
        Localization foundLocalization = localizationService.getLocalization(savedLocalization.getId());

        // Then
        assertNotNull(foundLocalization);
        assertEquals(city, foundLocalization.getCity());
        assertEquals(country, foundLocalization.getCountry());
        assertEquals(longitude, foundLocalization.getLongitude());
        assertEquals(latitude, foundLocalization.getLatitude());
    }

    @Test
    void getAllLocalizations_returnsAllLocalizations() {
        // Given
        String city1 = "Test_City1";
        Float longitude1 = 1F;
        Float latitude1 = 1F;
        String country1 = "Test_Country1";
        String region1 = "Test_region1";
        String city2 = "Test_City2";
        Float longitude2 = 2F;
        Float latitude2 = 2F;
        String country2 = "Test_Country2";
        String region2 = "Test_region2";
        localizationRepository.deleteAll();
        Localization localization1 = localizationService.createLocalization(city1, longitude1, latitude1, region1, country1);
        Localization localization2 = localizationService.createLocalization(city2, longitude2, latitude2, region2, country2);
        localizationRepository.save(localization1);
        localizationRepository.save(localization2);
        // When
        List<Localization> foundLocalizations = localizationService.getAllLocalizations();

        // Then
        assertTrue(foundLocalizations.stream().anyMatch(loc ->
                city1.equals(loc.getCity()) &&
                        country1.equals(loc.getCountry()) &&
                        region1.equals(loc.getRegion()) &&
                        longitude1.equals(loc.getLongitude()) &&
                        latitude1.equals(loc.getLatitude())
        ));

        assertTrue(foundLocalizations.stream().anyMatch(loc ->
                city2.equals(loc.getCity()) &&
                        country2.equals(loc.getCountry()) &&
                        region2.equals(loc.getRegion()) &&
                        longitude2.equals(loc.getLongitude()) &&
                        latitude2.equals(loc.getLatitude())
        ));
    }


    @Test
    void getLocalization_returnsNoLocalization_ThrowsException() {
        // Given
        long nonExistentLocalizationId = -1000L;
        localizationRepository.deleteAll();
        // When
        assertThrows(NoLocalizationFoundException.class, () -> localizationService.getLocalization(nonExistentLocalizationId));
        // Then
    }

}
