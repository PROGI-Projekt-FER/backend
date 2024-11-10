package com.ticketswap.dto.ticket;

import com.ticketswap.dto.category.CategoryDto;
import com.ticketswap.dto.event_entity.EventEntityDto;
import com.ticketswap.dto.venue.VenueDto;
import com.ticketswap.model.EventEntity;
import com.ticketswap.model.TicketStatus;
import com.ticketswap.model.Venue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketSearchDto {

    private Long id;
    private TicketStatus status;
    private double price;
    private String title;
    private String description;
    private Timestamp event_date;
    private List<CategoryDto> categories;
    private VenueDto venue;
    private EventEntityDto eventEntity;

}
