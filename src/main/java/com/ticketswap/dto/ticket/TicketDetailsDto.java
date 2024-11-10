package com.ticketswap.dto.ticket;

import com.ticketswap.dto.category.CategoryDto;
import com.ticketswap.dto.event.EventDto;
import com.ticketswap.dto.user.UsernameDto;
import com.ticketswap.model.Ticket;
import com.ticketswap.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailsDto {

    private Long id;
    private UsernameDto postedByUser;
    private EventDto event;
    private TicketStatus status;
    private String description;
    private double price;
    private LocalDateTime lastChangedAt;
    private LocalDateTime postedAt;
    private List<CategoryDto> categories;

    public static TicketDetailsDto map(Ticket ticket) {
        TicketDetailsDto dto = new TicketDetailsDto();
        dto.setId(ticket.getId());
        dto.setPostedByUser(UsernameDto.map(ticket.getUser()));
        dto.setEvent(EventDto.map(ticket.getEvent()));
        dto.setStatus(ticket.getStatus());
        dto.setDescription(ticket.getDescription());
        dto.setPrice(ticket.getPrice());
        dto.setLastChangedAt(ticket.getUpdatedAt());
        dto.setPostedAt(ticket.getCreatedAt());
        dto.setCategories(ticket.getCategories().stream().map(CategoryDto::map).toList());
        return dto;
    }

    public Ticket toEntity() {
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setUser(postedByUser.toEntity());
        ticket.setEvent(event.toEntity());
        ticket.setStatus(status);
        ticket.setDescription(description);
        ticket.setPrice(price);
        ticket.setCategories(categories.stream().map(CategoryDto::toEntity).toList());
        return ticket;
    }

}
