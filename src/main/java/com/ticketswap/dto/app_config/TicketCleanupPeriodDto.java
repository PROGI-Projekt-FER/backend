package com.ticketswap.dto.app_config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketCleanupPeriodDto {
    int cleanupPeriodInSeconds;
}
