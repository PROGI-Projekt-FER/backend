package com.ticketswap.dto.swap;

import com.ticketswap.dto.ticket.TicketDetailsDto;
import com.ticketswap.model.ConfirmationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetailsDto {
    private TicketDetailsDto sendingTicket;

    private TicketDetailsDto receivingTicket;

    private ConfirmationStatus confirmationStatus;

    private LocalDateTime sentAt;

    private LocalDateTime respondedAt;
}
