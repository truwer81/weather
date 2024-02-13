package com.example.server;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class City {
    @Id
    private String cityName;
    private String countryCode;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public City(String cityName, String countryCode) {
        this.cityName = cityName;
        this.countryCode = countryCode;
    }

    public City() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City city)) return false;
        return Objects.equals(getCityName(), city.getCityName()) && Objects.equals(getCountryCode(), city.getCountryCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCityName(), getCountryCode());
    }
}

