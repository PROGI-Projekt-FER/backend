package com.ticketswap.dto.ticket;

import com.ticketswap.dto.category.CategoryDto;
import com.ticketswap.dto.event.EventDto;
import com.ticketswap.dto.user.UsernameDto;
import com.ticketswap.model.Category;
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
public class TicketInsertDto {

    private Long id;
    private EventDto event;
    private TicketStatus status;
    private String description;
    private double price;
    private List<Long> categoryIds;
    private List<Long> interestedInCategoryIds;

    public static TicketInsertDto map(Ticket ticket) {
        TicketInsertDto dto = new TicketInsertDto();
        dto.setId(ticket.getId());
        dto.setEvent(EventDto.map(ticket.getEvent()));
        dto.setStatus(ticket.getStatus());
        dto.setDescription(ticket.getDescription());
        dto.setPrice(ticket.getPrice());
        dto.setCategoryIds(ticket.getCategories().stream().map(Category::getId).toList());
        dto.setInterestedInCategoryIds(ticket.getInterestedInCategories().stream().map(Category::getId).toList());
        return dto;
    }

    public Ticket toEntity() {
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setEvent(event.toEntity());
        ticket.setStatus(status);
        ticket.setDescription(description);
        ticket.setPrice(price);
        ticket.setCategories(categoryIds.stream().map(id -> new Category(id, null, null, null)).toList());
        ticket.setInterestedInCategories(interestedInCategoryIds.stream().map(id -> new Category(id, null, null, null)).toList());
        return ticket;
    }
}
