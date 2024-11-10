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
    private String country;
    private String city;
    private String address;

    public static LocationDto map(Location location) {
        LocationDto dto = new LocationDto();
        dto.setCountry(location.getCountry());
        dto.setCity(location.getCity());
        dto.setAddress(location.getAddress());
        return dto;
    }

    public Location toEntity() {
        Location location = new Location();
        location.setCountry(country);
        location.setCity(city);
        location.setAddress(address);
        return location;
    }
}
