package com.ticketswap.repository;

import com.ticketswap.model.SwapConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwapConfirmationRepository extends JpaRepository<SwapConfirmation, Long> {
}
