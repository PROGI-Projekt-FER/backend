package com.ticketswap.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ticketswap.util.TimestampSecondsDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class DailyWeatherInCityDto {
    @JsonProperty("dt")
    @JsonDeserialize(using = TimestampSecondsDeserializer.class)
    private Timestamp time;

    private MainByCityDto main;

    private List<WeatherByCityDescriptionDto> weather;

}
