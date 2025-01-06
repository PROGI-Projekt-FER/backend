package com.ticketswap.service;

import com.ticketswap.dto.event.EventDto;
import com.ticketswap.model.Event;
import com.ticketswap.repository.EventRepository;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ticketswap.model.EventEntity;
import com.ticketswap.model.Venue;
import com.ticketswap.repository.EventEntityRepository;
import com.ticketswap.repository.VenueRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private EventEntityRepository eventEntityRepository;

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

    public List<EventDto> getAllEntities() {
        return eventRepository.findAll().stream()
                .map(EventDto::map)
                .collect(Collectors.toList());
    }

    public Optional<EventDto> getEntityById(Long id) {
        return eventRepository.findById(id)
                .map(EventDto::map);
    }

    public EventDto createEntity(EventDto eventDto) {
        Venue venue = venueRepository.findById(eventDto.getVenue().getId())
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        EventEntity eventEntity = eventEntityRepository.findById(eventDto.getEventEntity().getId())
                .orElseThrow(() -> new RuntimeException("Event entity not found"));

        Event event = eventDto.toEntity();
        event.setVenue(venue);
        event.setEventEntity(eventEntity);

        eventRepository.save(event);
        return EventDto.map(event);
    }

    public EventDto updateEntity(Long id, EventDto eventDto) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Venue venue = venueRepository.findById(eventDto.getVenue().getId())
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        EventEntity eventEntity = eventEntityRepository.findById(eventDto.getEventEntity().getId())
                .orElseThrow(() -> new RuntimeException("Event entity not found"));

        existingEvent.setTitle(eventDto.getTitle());
        existingEvent.setDescription(eventDto.getDescription());
        existingEvent.setEventDate(eventDto.getEventDate());
        existingEvent.setVenue(venue);
        existingEvent.setEventEntity(eventEntity);

        eventRepository.save(existingEvent);
        return EventDto.map(existingEvent);
    }

    public void deleteEntity(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(id);
    }
}
