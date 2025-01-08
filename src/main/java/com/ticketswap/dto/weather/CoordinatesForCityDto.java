package com.ticketswap.dto.weather;

import com.ticketswap.util.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesForCityDto {
    private Coordinates coord;
}
