package com.example.server.weather;

import com.example.server.localization.Localization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "weather")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localization_id", referencedColumnName = "id")
    private Localization localization;
    @Column(name = "expiry_time", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime expiryTime;
    private String message;
    private String mainInfo;
    private String description;
    private Float temp;
    private Float feelsLike;
    private Float pressure;
    private Float humidity;
    private Float windSpeed;
    private Float windDeg;
    private Float cloudsAll;
    private LocalDate weatherDate;
    private String timezone;
}


