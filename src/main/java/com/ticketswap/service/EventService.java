package com.ticketswap.service;

import com.ticketswap.dto.event.EventDto;
import com.ticketswap.model.Event;
import com.ticketswap.repository.EventRepository;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueService venueService;

    @Autowired
    private EventEntityService eventEntityService;

    public Event getUpdatedEvent(EventDto eventDto) {
        Event newEvent = eventDto.toEntity();
        Optional<Event> existingEvent = eventRepository.findById(newEvent.getId());
        if (existingEvent.isEmpty()) {
            throw new ResourceNotFoundException("Event with id = " + eventDto.getId() + " does not exist.");
        }
        Event oldEvent = existingEvent.get();
        oldEvent.setEventDate(newEvent.getEventDate());
        oldEvent.setTitle(newEvent.getTitle());
        oldEvent.setDescription(newEvent.getDescription());
        oldEvent.setVenue(venueService.getUpdatedVenue(eventDto.getVenue()));
        oldEvent.setEventEntity(eventEntityService.getUpdateEventEntity(eventDto.getEventEntity()));
        return oldEvent;
    }
}
