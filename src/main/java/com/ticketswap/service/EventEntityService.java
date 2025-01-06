package com.ticketswap.service;

import com.ticketswap.dto.event_entity.EventEntityDto;
import com.ticketswap.model.EventEntity;
import com.ticketswap.repository.EventEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.ticketswap.model.Venue;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventEntityService {

    @Autowired
    private EventEntityRepository eventEntityRepository;

    public EventEntity getUpdateEventEntity(EventEntityDto eventEntityDto) {
        EventEntity newEventEntity = eventEntityDto.toEntity();
        Optional<EventEntity> existingEntity = eventEntityRepository.findById(newEventEntity.getId());
        if (existingEntity.isEmpty()) {
            throw new ResourceNotFoundException("Event with id = " + eventEntityDto.getId() + " does not exist.");
        }
        EventEntity oldEventEntity = existingEntity.get();
        oldEventEntity.setName(newEventEntity.getName());
        oldEventEntity.setType(newEventEntity.getType());
        return oldEventEntity;
    }

    public List<EventEntityDto> getAllEntities() {
        return eventEntityRepository.findAll()
                .stream()
                .map(EventEntityDto::map)
                .collect(Collectors.toList());
    }

    public Optional<EventEntityDto> getEntityById(Long id) {
        return eventEntityRepository.findById(id)
                .map(EventEntityDto::map);
    }

    public EventEntityDto createEntity(EventEntityDto eventEntityDto) {
        EventEntity eventEntity = eventEntityDto.toEntity();
        EventEntity savedEntity = eventEntityRepository.save(eventEntity);
        return EventEntityDto.map(savedEntity);
    }

    public EventEntityDto updateEntity(Long id, EventEntityDto eventEntityDto) {
        Optional<EventEntity> existingEntity = eventEntityRepository.findById(id);
        if (existingEntity.isEmpty()) {
            throw new RuntimeException("EventEntity not found");
        }

        EventEntity eventEntity = existingEntity.get();
        eventEntity.setType(eventEntityDto.getType());
        eventEntity.setName(eventEntityDto.getName());
        EventEntity updatedEntity = eventEntityRepository.save(eventEntity);

        return EventEntityDto.map(updatedEntity);
    }

    public void deleteEntity(Long id) {
        if (!eventEntityRepository.existsById(id)) {
            throw new RuntimeException("EventEntity not found");
        }
        eventEntityRepository.deleteById(id);
    }
}
