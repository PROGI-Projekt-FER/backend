package com.ticketswap.service;

import com.google.gson.Gson;
import com.ticketswap.dto.weather.WeatherByCityDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
@Service
public class WeatherService {

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${openweather.api-key}")
    private String apiKey;

    public WeatherByCityDto getWeatherData(String city, String country) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("q", city + "," + country)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        WeatherByCityDto result = restTemplate.getForObject(url, WeatherByCityDto.class);

        return result;
    }
}
