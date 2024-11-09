package com.ticketswap.dto.weather;

import com.ticketswap.util.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor

public class WeatherByCityDto {
    private Coordinates coord;
    private List<WeatherByCityDescriptionDto> weather;
    private MainByCityDto main;
}
