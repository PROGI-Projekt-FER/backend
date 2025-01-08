package com.ticketswap.service;

import com.ticketswap.dto.event_entity.EventEntityDto;
import com.ticketswap.model.EventEntity;
import com.ticketswap.model.Venue;
import com.ticketswap.repository.EventEntityRepository;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
