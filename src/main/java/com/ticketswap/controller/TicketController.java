package com.ticketswap.controller;

import com.ticketswap.dto.ticket.TicketDetailsDto;
import com.ticketswap.dto.ticket.TicketInsertDto;
import com.ticketswap.dto.ticket.TicketSearchDto;
import com.ticketswap.model.CustomOAuth2User;
import com.ticketswap.service.TicketService;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

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

    @PostMapping
    public ResponseEntity<TicketDetailsDto> createTicket(@RequestBody TicketInsertDto ticketInsertDto) {
    // TODO: uncomment this and set user to principal.getUser()
//     if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        TicketDetailsDto createdTicket = ticketService.createTicket(ticketInsertDto, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }
}
