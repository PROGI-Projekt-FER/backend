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
    private final AppConfigService appConfigService;

    public TicketCleanupService(TicketRepository ticketRepository, AppConfigService appConfigService) {
        this.ticketRepository = ticketRepository;
        this.appConfigService = appConfigService;
    }

    @Scheduled(fixedRate = 3600000) // Run every 1 hour (3600000 ms), if ticket has the status DEACTIVATED, and it hasn't been updated in some time (default 24h) then change it to deleted
    public void deleteExpiredTickets() {
        int periodInSeconds = appConfigService.getTicketCleanupPeriodInSeconds();
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(periodInSeconds);
        TicketStatus deletedStatus = TicketStatus.DEACTIVATED;
        List<Ticket> ticketsToDelete = ticketRepository.getAllTicketsToBeAutomaticallyDeleted(
                deletedStatus, threshold
        );
        ticketsToDelete.forEach(ticket -> ticket.setStatus(TicketStatus.DELETED));
        ticketRepository.saveAll(ticketsToDelete);
    }
}
