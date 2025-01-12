package com.ticketswap.repository;

import com.ticketswap.model.SwapConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SwapConfirmationRepository extends JpaRepository<SwapConfirmation, Long> {
    Optional<SwapConfirmation> findSwapConfirmationBySwapRequestId(Long swapRequestId);
}
