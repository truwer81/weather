package com.example.server.localization;

import com.example.server.login.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocalizationController {

    private final LocalizationService localizationService;
    private final LocalizationMapper localizationMapper;

    @PostMapping("/localizations")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<LocalizationDTO> createLocalization(@RequestBody LocalizationDTO model) {
        var city = model.getCity();
        var longitude = model.getLongitude();
        var latitude = model.getLatitude();
        var region = model.getRegion();
        var country = model.getCountry();
        var localization = localizationService.createLocalization(city, longitude, latitude, region, country);
        return ResponseEntity.status(HttpStatus.OK)
                .body(localizationMapper.asDTO(localization));
    }

    @GetMapping("/localizations")
    public ResponseEntity<List<LocalizationDTO>> getLocalizations(HttpServletRequest request) {
        var session = request.getSession();
        var user = session.getAttribute("user");
        if (user != null) {
            System.out.println("Lokalizacje pobiera user: " + ((User) user).getUsername());
        }
        var localizations = localizationService.getAllLocalizations();
        return ResponseEntity.status(HttpStatus.OK)
                .body(localizationMapper.asDTO(localizations));
    }

    @GetMapping("/localizations/{localizationId}")
    public ResponseEntity<LocalizationDTO> getLocalization(@PathVariable long localizationId) {
        Localization localizations = localizationService.getLocalization(localizationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(localizationMapper.asDTO(localizations));
    }


}
