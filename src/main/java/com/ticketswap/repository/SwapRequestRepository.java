package com.ticketswap.repository;

import com.ticketswap.model.SwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {
}
