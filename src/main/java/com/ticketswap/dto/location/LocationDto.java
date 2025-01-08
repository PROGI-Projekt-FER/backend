package com.ticketswap.dto.location;

import com.ticketswap.model.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Long id;
    private String country;
    private String city;
    private String address;

    public static LocationDto map(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setCountry(location.getCountry());
        dto.setCity(location.getCity());
        dto.setAddress(location.getAddress());
        return dto;
    }

    public Location toEntity() {
        Location location = new Location();
        location.setId(id);
        location.setCountry(country);
        location.setCity(city);
        location.setAddress(address);
        return location;
    }
}
