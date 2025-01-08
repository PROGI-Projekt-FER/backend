package com.ticketswap.service;

import com.ticketswap.dto.weather.CityDailyForecastDto;
import com.ticketswap.dto.weather.CoordinatesForCityDto;
import com.ticketswap.dto.weather.DailyWeatherInCityDto;
import com.ticketswap.util.Coordinates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class WeatherService {

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";

    private static final String FORECAST_API_URL = "https://api.openweathermap.org/data/2.5/forecast";

    @Value("${openweather.api-key}")
    private String apiKey;

    public DailyWeatherInCityDto getWeatherData(String city, String country, LocalDateTime eventTime) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("q", city + "," + country)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        CoordinatesForCityDto result = restTemplate.getForObject(url, CoordinatesForCityDto.class);

        Coordinates coordinates = result.getCoord();

        restTemplate = new RestTemplate();

        url = UriComponentsBuilder.fromHttpUrl(FORECAST_API_URL)
                .queryParam("lat", coordinates.getLat())
                .queryParam("lon", coordinates.getLon())
                .queryParam("appid", apiKey)
                .queryParam("exclude", "current,minutely,hourly")
                .queryParam("units", "metric")
                .toUriString();

        CityDailyForecastDto forecast = restTemplate.getForObject(url, CityDailyForecastDto.class);
        DailyWeatherInCityDto closestWeatherToEvent = forecast.getList().stream().min((forecast1, forecast2) -> {
            long diff1 = Duration.between(eventTime, forecast1.getTime().toLocalDateTime()).abs().toMillis();
            long diff2 = Duration.between(eventTime, forecast2.getTime().toLocalDateTime()).abs().toMillis();
            return Long.compare(diff1, diff2);
        }).get();
        System.out.printf("[TEST] %s, %s", closestWeatherToEvent.getWeather().get(0).getMain(), closestWeatherToEvent.getTime().toLocalDateTime());
        if (Duration.between(closestWeatherToEvent.getTime().toLocalDateTime(), eventTime).abs().toHours() <= 24) {
            return closestWeatherToEvent;
        } else {
            return null;
        }
    }
}
