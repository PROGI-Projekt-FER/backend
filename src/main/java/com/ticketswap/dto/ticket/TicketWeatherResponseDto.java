package com.ticketswap.dto.ticket;

import com.ticketswap.dto.weather.DailyWeatherInCityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketWeatherResponseDto {

    private Timestamp time;

    private double temp;

    private String weather;

    private String weatherDescription;

    private String weatherIcon;

    public static TicketWeatherResponseDto map(DailyWeatherInCityDto forecast) {
        TicketWeatherResponseDto dto = new TicketWeatherResponseDto();
        dto.setTime(forecast.getTime());
        dto.setTemp(forecast.getMain().getTemp());
        dto.setWeather(forecast.getWeather().get(0).getMain());
        dto.setWeatherDescription(forecast.getWeather().get(0).getDescription());
        dto.setWeatherIcon(forecast.getWeather().get(0).getIcon());
        return dto;
    }

}
