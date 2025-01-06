package com.ticketswap.controller;

import com.ticketswap.dto.weather.CityDailyForecastDto;
import com.ticketswap.dto.weather.WeatherByCityDto;
import com.ticketswap.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")

public class WeatherController {
    @Autowired
    private WeatherService weatherService;


    @GetMapping
    public ResponseEntity<CityDailyForecastDto> test() {
        CityDailyForecastDto weather = weatherService.getWeatherData("Zagreb", "Croatia", LocalDate.now());

        return ResponseEntity.ok(weather);
    }
}

