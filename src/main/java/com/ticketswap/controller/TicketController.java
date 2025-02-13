package com.ticketswap.controller;

import com.ticketswap.dto.spotify.ArtistDetailsDto;
import com.ticketswap.dto.ticket.*;
import com.ticketswap.model.CustomOAuth2User;
import com.ticketswap.model.TicketStatus;
import com.ticketswap.model.User;
import com.ticketswap.service.AuthService;
import com.ticketswap.service.SpotifyService;
import com.ticketswap.service.TicketService;
import com.ticketswap.util.NotLoggedInException;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private AuthService authService;

    @Autowired
    private SpotifyService spotifyService;

    @GetMapping
    public ResponseEntity<List<TicketSearchDto>> searchTickets(
            @RequestParam(name = "categories", defaultValue = "") List<Long> categoryIds,
            @RequestParam(name = "offerTypes", defaultValue = "SELL,SWAP") List<TicketStatus> offerTypes,
            @RequestParam(name = "priceMin", defaultValue = "0") Integer priceMin,
            @RequestParam(name = "priceMax", defaultValue = "1000000") Integer priceMax,
            @RequestParam(name = "countries", defaultValue = "") List<String> countries,
            @RequestParam(name = "cities", defaultValue = "") List<String> cities,
            @RequestParam(name = "startDate", defaultValue = "1990-01-01T00:00:00") LocalDateTime startDate,
            @RequestParam(name = "endDate", defaultValue = "2100-01-01T00:00:00") LocalDateTime endDate
    ) {
        TicketSearchParams searchParams = new TicketSearchParams();
        searchParams.setCategoryIds(categoryIds);
        searchParams.setOfferTypes(offerTypes);
        searchParams.setPriceMin(priceMin);
        searchParams.setPriceMax(priceMax);
        searchParams.setCountries(countries);
        searchParams.setCities(cities);
        searchParams.setStartDate(startDate);
        searchParams.setEndDate(endDate);

        return ResponseEntity.ok(ticketService.searchTickets(searchParams));
    }

    @GetMapping("/autocomplete/artist")
    public ResponseEntity<List<Map.Entry<String, String>>> autocompleteArtists(@RequestParam String query) {
        return ResponseEntity.ok(spotifyService.searchArtists(query));
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<ArtistDetailsDto> getArtistDetails(@PathVariable("artistId") String artistId) {
        return ResponseEntity.ok(spotifyService.getArtistDetails(artistId));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDetailsDto> getTicketDetails(@PathVariable("ticketId") Long ticketId) {
        return ResponseEntity.ok(ticketService.getTicketById(ticketId));
    }

    @GetMapping("/{ticketId}/weather")
    public ResponseEntity<TicketWeatherResponseDto> getWeatherForTicket(@PathVariable("ticketId") Long ticketId) {
        return ResponseEntity.ok(ticketService.getWeatherForTicket(ticketId));
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketDetailsDto> updateTicket(@RequestBody TicketInsertDto ticketInsertDto, @PathVariable("ticketId") Long ticketId) {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        ticketInsertDto.setId(ticketId);
        return ResponseEntity.ok(ticketService.updateTicket(ticketInsertDto));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable("ticketId") Long ticketId) {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok("Successfully deleted ticket with id = " + ticketId);
    }

    @PostMapping
    public ResponseEntity<TicketDetailsDto> createTicket(@RequestBody TicketInsertDto ticketInsertDto) {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        TicketDetailsDto createdTicket = ticketService.createTicket(ticketInsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @PutMapping("/{ticketId}/change-status")
    public ResponseEntity<TicketDetailsDto> toggleTicketStatus(@RequestBody UpdateTicketStatusDto updateTicketStatusDto, @PathVariable("ticketId") Long ticketId) {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        updateTicketStatusDto.setTicketId(ticketId);
        TicketDetailsDto updatedTicket = ticketService.toggleTicketStatus(updateTicketStatusDto);
        return ResponseEntity.ok(updatedTicket);
    }

}
