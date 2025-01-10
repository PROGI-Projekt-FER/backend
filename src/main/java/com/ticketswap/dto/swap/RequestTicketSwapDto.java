package com.ticketswap.dto.swap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestTicketSwapDto {
    private Long requestingTicketId;
    private Long receivingTicketId;
}
