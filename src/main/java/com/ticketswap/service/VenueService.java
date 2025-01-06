package com.ticketswap.service;

import com.ticketswap.dto.venue.VenueDto;
import com.ticketswap.model.Venue;
import com.ticketswap.model.Location;
import com.ticketswap.repository.LocationRepository;
import com.ticketswap.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueService {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private LocationService locationService;

    public Venue getUpdatedVenue(VenueDto venueDto) {
        Venue newVenue = venueDto.toEntity();
        Optional<Venue> existingVenue = venueRepository.findById(newVenue.getId());
        if (existingVenue.isEmpty()) {
            throw new ResourceNotFoundException("Event with id = " + venueDto.getId() + " does not exist.");
        }
        Venue oldVenue = existingVenue.get();
        oldVenue.setCapacity(newVenue.getCapacity());
        oldVenue.setName(newVenue.getName());
        oldVenue.setLocation(locationService.getUpdatedLocation(venueDto.getLocation()));
        return oldVenue;
    }

    public List<VenueDto> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(VenueDto::map)
                .collect(Collectors.toList());
    }

    public Optional<VenueDto> getVenueById(Long id) {
        return venueRepository.findById(id)
                .map(VenueDto::map);
    }

    public VenueDto createVenue(VenueDto venueDto) {
        Location location = locationRepository.findById(venueDto.getLocation().toEntity().getId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        Venue venue = venueDto.toEntity();
        venue.setLocation(location);
        venueRepository.save(venue);
        return VenueDto.map(venue);
    }

    public VenueDto updateVenue(Long id, VenueDto venueDto) {
        Venue existingVenue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        Location location = locationRepository.findById(venueDto.getLocation().toEntity().getId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        existingVenue.setName(venueDto.getName());
        existingVenue.setCapacity(venueDto.getCapacity());
        existingVenue.setLocation(location);

        venueRepository.save(existingVenue);
        return VenueDto.map(existingVenue);
    }

    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }
}
