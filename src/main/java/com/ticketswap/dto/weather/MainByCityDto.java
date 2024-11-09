package com.ticketswap.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.ticketswap.util.TemperatureMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class MainByCityDto {
    private double temp;

    private int pressure;

    private int humidity;

    @JsonProperty("temp_min")
    private double tempMin;

    @JsonProperty("temp_max")
    private double tempMax;
}
