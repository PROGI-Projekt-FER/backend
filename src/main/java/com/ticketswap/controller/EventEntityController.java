package com.ticketswap.controller;

import com.ticketswap.dto.event_entity.EventEntityDto;
import com.ticketswap.service.EventEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-entities")
public class EventEntityController {

    @Autowired
    private EventEntityService eventEntityService;

    @GetMapping
    public ResponseEntity<List<EventEntityDto>> getAllEntities() {
        List<EventEntityDto> entities = eventEntityService.getAllEntities();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntityDto> getEntityById(@PathVariable Long id) {
        return eventEntityService.getEntityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventEntityDto> createEntity(@RequestBody EventEntityDto eventEntityDto) {
        EventEntityDto createdEntity = eventEntityService.createEntity(eventEntityDto);
        return ResponseEntity.ok(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventEntityDto> updateEntity(@PathVariable Long id, @RequestBody EventEntityDto eventEntityDto) {
        try {
            EventEntityDto updatedEntity = eventEntityService.updateEntity(id, eventEntityDto);
            return ResponseEntity.ok(updatedEntity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        try {
            eventEntityService.deleteEntity(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
