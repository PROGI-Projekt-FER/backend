package com.ticketswap.dto.ticket;

import com.ticketswap.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketStatusDto {
    private Long ticketId;
    private TicketStatus ticketStatus;
}
