package com.ticketswap.controller;

import com.ticketswap.dto.ticket.TicketDetailsDto;
import com.ticketswap.dto.ticket.TicketInsertDto;
import com.ticketswap.dto.ticket.TicketSearchDto;
import com.ticketswap.model.CustomOAuth2User;
import com.ticketswap.model.User;
import com.ticketswap.service.AuthService;
import com.ticketswap.service.TicketService;
import com.ticketswap.util.NotLoggedInException;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<List<TicketSearchDto>> searchTickets(
    // TODO: add filters in query params
    ) {
        return ResponseEntity.ok(ticketService.searchTickets());
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDetailsDto> getTicketDetails(@PathVariable("ticketId") Long ticketId) {
        return ResponseEntity.ok(ticketService.getTicketById(ticketId));
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketDetailsDto> updateTicket(@RequestBody TicketInsertDto ticketInsertDto, @PathVariable("ticketId") Long ticketId) {
        Optional<User> loggedInUser = authService.getLoggedInUser();
        if (loggedInUser.isEmpty()) throw new NotLoggedInException();
        ticketInsertDto.setId(ticketId);
        return ResponseEntity.ok(ticketService.updateTicket(ticketInsertDto));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable("ticketId") Long ticketId) {
        Optional<User> loggedInUser = authService.getLoggedInUser();
        if (loggedInUser.isEmpty()) throw new NotLoggedInException();
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok("Successfully deleted ticket with id = " + ticketId);
    }

    @PostMapping
    public ResponseEntity<TicketDetailsDto> createTicket(@RequestBody TicketInsertDto ticketInsertDto) {
        Optional<User> loggedInUser = authService.getLoggedInUser();
        if (loggedInUser.isEmpty()) throw new NotLoggedInException();
        TicketDetailsDto createdTicket = ticketService.createTicket(ticketInsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }
}
