package com.example.server.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseDTO {

    @JsonProperty("lon")
    private Float longitude;
    @JsonProperty("lat")
    private Float latitude;
    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("data")
    private List<WeatherForecastDTO> data; // Klasa pomocnicza dla danych data

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherForecastDTO {
        @JsonProperty("dt")
        private Long weatherTimeStamp;
        @JsonProperty("temp")
        private Float temp;
        @JsonProperty("feels_like")
        private Float feelsLike;
        @JsonProperty("pressure")
        private Float pressure;
        @JsonProperty("humidity")
        private Float humidity;
        @JsonProperty("clouds")
        private Float cloudsAll;
        @JsonProperty("wind_speed")
        private Float windSpeed;
        @JsonProperty("wind_deg")
        private Float windDeg;
        @JsonProperty("weather")
        private List<WeatherInfoDTO> weather; // Klasa pomocnicza dla danych weather

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class WeatherInfoDTO {
            @JsonProperty("main")
            private String mainInfo;
            @JsonProperty("description")
            private String descriptionInfo;
        }
    }
}

