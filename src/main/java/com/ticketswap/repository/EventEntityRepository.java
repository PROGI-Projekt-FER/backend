package com.ticketswap.repository;

import com.ticketswap.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventEntityRepository extends JpaRepository<EventEntity, Long> {
}
