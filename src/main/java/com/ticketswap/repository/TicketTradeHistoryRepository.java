package com.ticketswap.repository;

import com.ticketswap.model.SwapRequest;
import com.ticketswap.model.TicketTradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketTradeHistoryRepository extends JpaRepository<TicketTradeHistory, Long> {
    List<TicketTradeHistory> findTicketTradeHistoriesByPreviousOwnerIdOrNewOwnerIdOrderByCreatedAtDesc(Long previousOwnerId, Long newOwnerId);
}
