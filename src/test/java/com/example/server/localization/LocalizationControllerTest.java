package com.example.server.localization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class LocalizationControllerTest {

    @Mock
    private LocalizationService localizationService;
    @Mock
    private LocalizationMapper localizationMapper;
    @InjectMocks
    private LocalizationController localizationController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void createLocalization_ReturnsCorrectResponseEntity() {
        
        // Given
        var city = "Test City";
        var longitude = 2.2F;
        var latitude = 2.2F;
        var region = "Test Region";
        var country = "Test Country";
        
        LocalizationDTO model = new LocalizationDTO();
        model.setCity(city);
        model.setLongitude(longitude);
        model.setLatitude(latitude);
        model.setRegion(region);
        model.setCountry(country);

        Localization localization = new Localization();

        when(localizationService.createLocalization(anyString(), anyFloat(), anyFloat(), anyString(), anyString()))
                .thenReturn(localization);
        when(localizationMapper.asDTO(localization)).thenReturn(model);

        // When
        ResponseEntity<LocalizationDTO> response = localizationController.createLocalization(model);

        // Then
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(model, response.getBody())
        );

        verify(localizationService, times(1)).createLocalization(model.getCity(), model.getLongitude(),
                model.getLatitude(), model.getRegion(), model.getCountry());
        verify(localizationMapper, times(1)).asDTO(localization);
    }
}
