package com.ticketswap.controller;

import com.ticketswap.dto.swap.RequestDetailsDto;
import com.ticketswap.dto.swap.RequestTicketSwapDto;
import com.ticketswap.dto.swap.RespondTicketSwapDto;
import com.ticketswap.model.User;
import com.ticketswap.service.AuthService;
import com.ticketswap.service.SwapService;
import com.ticketswap.util.NotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SwapController {

    @Autowired
    private AuthService authService;

    @Autowired
    private SwapService swapService;

    @PostMapping("/request")
    public ResponseEntity<String> requestTicketSwap(@RequestBody RequestTicketSwapDto requestTicketSwapDto) throws Exception {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);

        swapService.requestTicketSwap(loggedInUser, requestTicketSwapDto.getRequestingTicketId(), requestTicketSwapDto.getReceivingTicketId());

        return ResponseEntity.ok("Request sent successfully");
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<RequestDetailsDto> getRequestDetails(@PathVariable Long requestId) throws Exception {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        RequestDetailsDto requestDetailsDto = swapService.getRequestDetails(loggedInUser, requestId);
        return ResponseEntity.ok(requestDetailsDto);
    }

    @PostMapping("/request/{requestId}/respond")
    public ResponseEntity<String> respondToTicketSwap(@RequestBody RespondTicketSwapDto respondTicketSwapDto, @PathVariable Long requestId) throws Exception {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);

        swapService.respondToTicketSwap(loggedInUser, requestId, respondTicketSwapDto.isAccepting());

        return ResponseEntity.ok("Response sent successfully");
    }
}
