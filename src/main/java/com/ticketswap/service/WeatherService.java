package com.ticketswap.service;

import com.google.gson.Gson;
import com.ticketswap.dto.weather.CityDailyForecastDto;
import com.ticketswap.dto.weather.WeatherByCityDto;
import com.ticketswap.util.Coordinates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class WeatherService {

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";

    private static final String ONE_CALL_API_URL = "https://api.openweathermap.org/data/2.5/forecast";

    @Value("${openweather.api-key}")
    private String apiKey;

    public CityDailyForecastDto getWeatherData(String city, String country, LocalDate date) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("q", city + "," + country)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        WeatherByCityDto result = restTemplate.getForObject(url, WeatherByCityDto.class);

        Coordinates coordinates = result.getCoord();

        restTemplate = new RestTemplate();

        url = UriComponentsBuilder.fromHttpUrl(ONE_CALL_API_URL)
                .queryParam("lat", coordinates.getLat())
                .queryParam("lon", coordinates.getLon())
                .queryParam("appid", apiKey)
                .queryParam("exclude", "current,minutely,hourly")
                .queryParam("units", "metric")
                .toUriString();

        CityDailyForecastDto forecast = restTemplate.getForObject(url, CityDailyForecastDto.class);

        return forecast;
    }
}
