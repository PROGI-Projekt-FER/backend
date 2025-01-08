package com.ticketswap.service;

import com.ticketswap.dto.event.EventDto;
import com.ticketswap.dto.venue.VenueDto;
import com.ticketswap.model.Event;
import com.ticketswap.model.Venue;
import com.ticketswap.repository.VenueRepository;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VenueService {

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
}
