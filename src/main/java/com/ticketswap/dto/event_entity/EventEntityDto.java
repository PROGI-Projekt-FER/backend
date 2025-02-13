package com.ticketswap.dto.event_entity;

import com.ticketswap.model.EventEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventEntityDto {

    private Long id;
    private String name;
    private String type;
    private String artistId;

    public static EventEntityDto map(EventEntity eventEntity) {
        EventEntityDto dto = new EventEntityDto();
        dto.setId(eventEntity.getId());
        dto.setName(eventEntity.getName());
        dto.setType(eventEntity.getType());
        dto.setArtistId(eventEntity.getArtistId());
        return dto;
    }

    public EventEntity toEntity() {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(id);
        eventEntity.setName(name);
        eventEntity.setType(type);
        eventEntity.setArtistId(artistId);
        return eventEntity;
    }
}
