package com.example.server.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponseWithMessageDTO {
    private WeatherDTO weather;
    private String generalMessage;
}

