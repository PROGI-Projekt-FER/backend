package com.ticketswap.dto.ticket;

import com.ticketswap.dto.user.UserDto;
import com.ticketswap.model.Ticket;
import com.ticketswap.model.TicketTradeHistory;
import com.ticketswap.model.TradeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryDto {
    private TicketBasicInfoDto ticket;

    private TicketBasicInfoDto swappedForTicket;

    private double soldForPrice;

    private LocalDateTime tradedAt;

    private TradeType tradeType;

    private UserDto newOwner;

    private UserDto previousOwner;

    public static TicketHistoryDto map(TicketTradeHistory ticketTradeHistory) {
        TicketHistoryDto ticketHistoryDto = new TicketHistoryDto();
        ticketHistoryDto.setTicket(TicketBasicInfoDto.map(ticketTradeHistory.getTicket()));
        ticketHistoryDto.setSwappedForTicket(TicketBasicInfoDto.map(ticketTradeHistory.getSwappedForTicket()));
        ticketHistoryDto.setSoldForPrice(ticketTradeHistory.getSoldForPrice());
        ticketHistoryDto.setTradedAt(ticketTradeHistory.getCreatedAt());
        ticketHistoryDto.setNewOwner(UserDto.map(ticketTradeHistory.getNewOwner()));
        ticketHistoryDto.setPreviousOwner(UserDto.map(ticketTradeHistory.getPreviousOwner()));
        return ticketHistoryDto;
    }
}
