package com.ticketswap.repository;

import com.ticketswap.model.SwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {
    Optional<SwapRequest> findSwapRequestByReceivingTicketIdAndSendingTicketId(Long receivingTicketId, Long sendingTicketId);
}
