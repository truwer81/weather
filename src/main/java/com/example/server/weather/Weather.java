package com.example.server.weather;

import com.example.server.localization.Localization;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "weather")
@Builder
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localization_id", referencedColumnName = "id")
    private Localization localization;
    @Column(name = "expiry_time", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime expiryTime;
    private Float temp;
    private Float pressure;
    private Float humidity;
    private Float windSpeed;
    private Float windDeg;
    private LocalDate weatherDate;
}


