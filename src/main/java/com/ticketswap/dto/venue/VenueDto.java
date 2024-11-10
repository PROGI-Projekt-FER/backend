package com.ticketswap.dto.venue;

import com.ticketswap.dto.location.LocationDto;
import com.ticketswap.model.Venue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VenueDto {

    private Long id;
    private String name;
    private int capacity;
    private LocationDto location;

    public static VenueDto map(Venue venue) {
        VenueDto dto = new VenueDto();
        dto.setId(venue.getId());
        dto.setName(venue.getName());
        dto.setCapacity(venue.getCapacity());
        dto.setLocation(LocationDto.map(venue.getLocation()));
        return dto;
    }

    public Venue toEntity(){
        Venue venue = new Venue();
        venue.setId(id);
        venue.setName(name);
        venue.setCapacity(capacity);
        venue.setLocation(location.toEntity());
        return venue;
    }

}
