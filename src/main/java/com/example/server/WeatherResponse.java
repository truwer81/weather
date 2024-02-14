package com.example.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    @JsonProperty("sys")
    private SystemInfo systemInfo; // Klasa pomocnicza dla danych sys
    public static class SystemInfo {
        @JsonProperty("country")
        private String countryCode;

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }
    }
    @JsonProperty("dt")
    private Long forecastTimestamp;
    @JsonProperty("name")
    private String cityName;

    @JsonProperty("description")
    private String description;
    @JsonProperty("main")
    private MainInfo mainInfo; // Klasa pomocnicza dla danych main
    public static class MainInfo {
        @JsonProperty("temp")
        private Float temp;
        @JsonProperty("feels_like")
        private Float feelsLike;
        @JsonProperty("temp_min")
        private Float tempMin;
        @JsonProperty("temp_max")
        private Float tempMax;
        @JsonProperty("pressure")
        private Float pressure;
        @JsonProperty("humidity")
        private Float humidity;

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
    }
    @JsonProperty("wind")
    private WindInfo windInfo; // Klasa pomocnicza dla danych wind
    public static class WindInfo {
        @JsonProperty("speed")
        private Float windSpeed;

        public Float getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(Float windSpeed) {
            this.windSpeed = windSpeed;
        }
    }
    @JsonProperty("clouds")
    private CloudsInfo cloudsInfo; // Klasa pomocnicza dla danych clouds
    public static class CloudsInfo {
        @JsonProperty("all")
        private Float cloudsAll;

        public Float getCloudsAll() {
            return cloudsAll;
        }

        public void setCloudsAll(Float cloudsAll) {
            this.cloudsAll = cloudsAll;
        }
    }

    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    public Long getForecastTimestamp() {
        return forecastTimestamp;
    }

    public void setForecastTimestamp(Long forecastTimestamp) {
        this.forecastTimestamp = forecastTimestamp;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MainInfo getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(MainInfo mainInfo) {
        this.mainInfo = mainInfo;
    }

    public WindInfo getWindInfo() {
        return windInfo;
    }

    public void setWindInfo(WindInfo windInfo) {
        this.windInfo = windInfo;
    }

    public CloudsInfo getCloudsInfo() {
        return cloudsInfo;
    }

    public void setCloudsInfo(CloudsInfo cloudsInfo) {
        this.cloudsInfo = cloudsInfo;
    }
}
