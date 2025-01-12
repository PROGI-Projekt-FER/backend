package com.ticketswap.dto.swap;

import com.ticketswap.dto.ticket.TicketDetailsDto;
import com.ticketswap.model.ConfirmationStatus;
import com.ticketswap.model.SwapConfirmation;
import com.ticketswap.model.SwapRequest;
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

    public static RequestDetailsDto map(SwapRequest swapRequest, SwapConfirmation swapConfirmation) {
        RequestDetailsDto requestDetailsDto = new RequestDetailsDto();
        requestDetailsDto.setReceivingTicket(TicketDetailsDto.map(swapRequest.getReceivingTicket()));
        requestDetailsDto.setSendingTicket(TicketDetailsDto.map(swapRequest.getSendingTicket()));
        requestDetailsDto.setConfirmationStatus(swapRequest.getConfirmationStatus());
        requestDetailsDto.setSentAt(swapRequest.getCreatedAt());
        if (swapConfirmation != null) {
            requestDetailsDto.setRespondedAt(swapConfirmation.getCreatedAt());
        }
        return requestDetailsDto;
    }
}
