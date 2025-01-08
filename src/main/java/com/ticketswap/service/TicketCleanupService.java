package com.ticketswap.service;

import com.ticketswap.model.Ticket;
import com.ticketswap.model.TicketStatus;
import com.ticketswap.repository.TicketRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketCleanupService {

    private final TicketRepository ticketRepository;

    public TicketCleanupService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(fixedRate = 21600000) // Run every 6 hours (21600000 ms), if ticket has the status DELETED, and it hasn't been updated in 24h then delete it from the database
    public void deleteExpiredTickets() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        TicketStatus deletedStatus = TicketStatus.DELETED;
        List<Ticket> ticketsToDelete = ticketRepository.getAllTicketsToBeAutomaticallyDeleted(
                deletedStatus, threshold
        );
        ticketRepository.deleteAll(ticketsToDelete);
    }
}
