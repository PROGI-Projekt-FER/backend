package com.ticketswap.dto.ticket;

import com.ticketswap.dto.category.CategoryDto;
import com.ticketswap.dto.event.EventDto;
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
public class TicketSearchDto {

    private Long id;
    private TicketStatus status;
    private double price;
    private String description;
    private EventDto event;
    private List<CategoryDto> categories;

    public static TicketSearchDto map(Ticket ticket) {
        TicketSearchDto dto = new TicketSearchDto();
        dto.setId(ticket.getId());
        dto.setStatus(ticket.getStatus());
        dto.setPrice(ticket.getPrice());
        dto.setDescription(ticket.getDescription());
        dto.setEvent(EventDto.map(ticket.getEvent()));
        dto.setCategories(ticket.getCategories().stream().map(CategoryDto::map).toList());
        return dto;
    }
}
