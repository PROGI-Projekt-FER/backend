package com.ticketswap.repository;

import com.ticketswap.model.Ticket;
import com.ticketswap.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM ticket t WHERE t.status = :ticketStatus AND t.updatedAt <= :threshold")
    List<Ticket> getAllTicketsToBeAutomaticallyDeleted(TicketStatus ticketStatus, LocalDateTime threshold);

    List<Ticket> findAllByUserId(Long userId);
}
