package com.ticketswap.util;

public class TemperatureMapper {
    public static double celsiusToKelvin(double tempInCelsius){
        return tempInCelsius + 273.15;
    }

    public static double kelvinToCelsius(double tempInKelvin){
        return tempInKelvin - 273.15;
    }
}
