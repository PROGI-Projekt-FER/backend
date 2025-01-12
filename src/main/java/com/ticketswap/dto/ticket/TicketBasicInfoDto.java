package com.ticketswap.dto.ticket;

import com.ticketswap.dto.category.CategoryDto;
import com.ticketswap.dto.event.EventDto;
import com.ticketswap.dto.user.UserDto;
import com.ticketswap.model.Ticket;
import com.ticketswap.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketBasicInfoDto {

    private Long id;
    private double price;
    private String description;
    private EventDto event;
    public static TicketBasicInfoDto map(Ticket ticket) {
        TicketBasicInfoDto dto = new TicketBasicInfoDto();
        dto.setId(ticket.getId());
        dto.setPrice(ticket.getPrice());
        dto.setDescription(ticket.getDescription());
        dto.setEvent(EventDto.map(ticket.getEvent()));
        return dto;
    }
}
