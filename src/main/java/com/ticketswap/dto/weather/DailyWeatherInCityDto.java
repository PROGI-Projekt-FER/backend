package com.ticketswap.dto.weather;

import java.sql.Timestamp;
import java.util.List;

public class DailyWeatherInCityDto {
    Timestamp dt;
    String summary;

    TemperatureDto temp;

    List<WeatherByCityDescriptionDto> weather;

}
