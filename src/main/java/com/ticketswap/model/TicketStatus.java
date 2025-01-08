package com.ticketswap.model;

public enum TicketStatus {
    SELL, SWAP, DEACTIVATED, DELETED, EXCHANGED;

    public Boolean isActive() {
        return this == SELL || this == SWAP;
    }
}
