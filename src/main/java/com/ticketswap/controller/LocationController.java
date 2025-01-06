package com.ticketswap.controller;

import com.ticketswap.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities() {
        return ResponseEntity.ok(locationService.getCities().stream().toList());
    }

    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountries() {
        return ResponseEntity.ok(locationService.getCountries().stream().toList());
    }
}
