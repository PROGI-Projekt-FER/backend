package com.ticketswap.service;

import com.ticketswap.model.Location;
import com.ticketswap.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Set<String> getCities() {
        return locationRepository.findAll().stream().map(Location::getCity).collect(Collectors.toSet());
    }

    public Set<String> getCountries() {
        return Set.of(
                "Albania",
                "Andorra",
                "Austria",
                "Belarus",
                "Belgium",
                "Bosnia and Herzegovina",
                "Bulgaria",
                "Croatia",
                "Cyprus",
                "Czech Republic",
                "Denmark",
                "Estonia",
                "Finland",
                "France",
                "Germany",
                "Greece",
                "Hungary",
                "Iceland",
                "Ireland",
                "Italy",
                "Latvia",
                "Liechtenstein",
                "Lithuania",
                "Luxembourg",
                "North Macedonia",
                "Malta",
                "Moldova",
                "Monaco",
                "Montenegro",
                "Netherlands",
                "Norway",
                "Poland",
                "Portugal",
                "Romania",
                "Russian Federation",
                "Serbia",
                "Slovakia",
                "Slovenia",
                "Spain",
                "Sweden",
                "Switzerland",
                "Ukraine",
                "United Kingdom"
        );
    }
}
