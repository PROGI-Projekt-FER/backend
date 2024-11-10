package com.ticketswap.dto.event;

import com.ticketswap.dto.event_entity.EventEntityDto;
import com.ticketswap.dto.location.LocationDto;
import com.ticketswap.dto.venue.VenueDto;
import com.ticketswap.model.Event;
import com.ticketswap.model.EventEntity;
import com.ticketswap.model.Venue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    private Long id;
    private String title;
    private String description;
    private Timestamp eventDate;
    private VenueDto venue;
    private EventEntityDto eventEntity;

    public static EventDto map(Event event) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setVenue(VenueDto.map(event.getVenue()));
        dto.setEventEntity(EventEntityDto.map(event.getEventEntity()));
        return dto;
    }

    public Event toEntity() {
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        event.setDescription(description);
        event.setEventDate(eventDate);
        event.setVenue(venue.toEntity());
        event.setEventEntity(eventEntity.toEntity());
        return event;
    }

}
