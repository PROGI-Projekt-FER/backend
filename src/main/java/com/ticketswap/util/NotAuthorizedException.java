package com.ticketswap.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
    }
}
