package com.ticketswap.service;

import com.ticketswap.dto.location.LocationDto;
import com.ticketswap.model.Location;
import com.ticketswap.model.Venue;
import com.ticketswap.repository.LocationRepository;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location getUpdatedLocation(LocationDto locationDto) {
        Location newLocation = locationDto.toEntity();
        Optional<Location> existingLocation = locationRepository.findById(newLocation.getId());
        if (existingLocation.isEmpty()) {
            throw new ResourceNotFoundException("Event with id = " + locationDto.getId() + " does not exist.");
        }
        Location oldLocation = existingLocation.get();
        oldLocation.setAddress(newLocation.getAddress());
        oldLocation.setCity(newLocation.getCity());
        oldLocation.setCountry(newLocation.getCountry());
        oldLocation.setPostal_code(newLocation.getPostal_code());
        return oldLocation;
    }

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
