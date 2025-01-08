package com.ticketswap.dto.ticket;

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
public class TicketSearchParams {
    private List<Long> categoryIds;
    private List<TicketStatus> offerTypes;
    private Integer priceMin;
    private Integer priceMax;
    private List<String> countries;
    private List<String> cities;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
