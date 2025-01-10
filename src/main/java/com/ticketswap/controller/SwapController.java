package com.ticketswap.controller;

import com.ticketswap.dto.swap.RequestTicketSwapDto;
import com.ticketswap.dto.swap.RespondTicketSwapDto;
import com.ticketswap.model.User;
import com.ticketswap.service.AuthService;
import com.ticketswap.service.SwapService;
import com.ticketswap.util.NotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/swap")
public class SwapController {

    @Autowired
    private AuthService authService;

    @Autowired
    private SwapService swapService;

    @PostMapping("/request")
    public ResponseEntity<String> requestTicketSwap(@RequestBody RequestTicketSwapDto requestTicketSwapDto) throws Exception {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);

        swapService.requestTicketSwap(loggedInUser, requestTicketSwapDto.getRequestingTicketId(), requestTicketSwapDto.getRequestingTicketId());

        return ResponseEntity.ok("Request sent successfully");
    }

    @PostMapping("/respond")
    public ResponseEntity<String> respondToTicketSwap(@RequestBody RespondTicketSwapDto respondTicketSwapDto) throws Exception {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);

        swapService.respondToTicketSwap(loggedInUser, respondTicketSwapDto.getSwapRequestId(), respondTicketSwapDto.isAccepting());

        return ResponseEntity.ok("Response sent successfully");
    }
}
