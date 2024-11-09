package com.ticketswap.repository;

import com.ticketswap.model.SwapCycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwapCycleRepository extends JpaRepository<SwapCycle, Long> {
}
