package com.ticketswap.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class WeatherByCityDescriptionDto {
    private String main;
    private String description;
    private String icon;
}
