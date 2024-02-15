package com.example.server;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import jakarta.persistence.*;


@Entity
public class CheckWeather {
    @ManyToOne
    @JoinColumn(name = "cityName", referencedColumnName = "cityName")
    private City city;

    public void setCity(City city) {
        this.city = city;
    }

    @Id
    //@GeneratedValue(strategy = SEQUENCE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Float temp;
    @Column(name = "feels_like")
    private Float feelsLike;
    @Column(name = "temp_min")
    private Float tempMin;
    @Column(name = "temp_max")
    private Float tempMax;
    private Float pressure;
    private Float humidity;
    private Float windSpeed;
    @Column(name = "clouds_all")
    private Float cloudsAll;
    @Column(name = "forecastTimestamp")
    private Long forecastTimestamp;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Float feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Float getTempMin() {
        return tempMin;
    }

    public void setTempMin(Float tempMin) {
        this.tempMin = tempMin;
    }

    public Float getTempMax() {
        return tempMax;
    }

    public void setTempMax(Float tempMax) {
        this.tempMax = tempMax;
    }

    public Float getPressure() {
        return pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Float getCloudsAll() {
        return cloudsAll;
    }

    public void setCloudsAll(Float cloudsAll) {
        this.cloudsAll = cloudsAll;
    }

    public City getCity() {
        return city;
    }

    public Long getForecastTimestamp() {
        return forecastTimestamp;
    }

    public void setForecastTimestamp(Long forecastTimestamp) {
        this.forecastTimestamp = forecastTimestamp;
    }

    // Konwersja timestamp UNIX na LocalDateTime
    public LocalDateTime convertTimestampToLocalDateTime(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    public CheckWeather(City city, Long id, String description, Float temp, Float feelsLike, Float tempMin, Float tempMax, Float pressure, Float humidity, Float windSpeed, Float cloudsAll, Long forecastTimestamp) {
        this.city = city;
        this.id = id;
        this.description = description;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.cloudsAll = cloudsAll;
        this.forecastTimestamp = forecastTimestamp;
    }

    public CheckWeather() {
    }

    @Override
    public String toString() {
        return "CheckWeather{" +
                "id=" + id +
                ", city=" + city +
                ", description='" + description + '\'' +
                ", temp=" + temp +
                ", feels_like=" + feelsLike +
                ", temp_min=" + tempMin +
                ", temp_max=" + tempMax +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", clouds_all='" + cloudsAll + '\'' +
                '}';
    }
}

