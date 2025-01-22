package com.ticketswap.service;

import com.ticketswap.dto.spotify.ArtistDetailsDto;
import com.ticketswap.dto.ticket.*;
import com.ticketswap.dto.user.UsernameDto;
import com.ticketswap.dto.weather.CityDailyForecastDto;
import com.ticketswap.dto.weather.DailyWeatherInCityDto;
import com.ticketswap.model.*;
import com.ticketswap.repository.CategoryRepository;
import com.ticketswap.repository.TicketRepository;
import com.ticketswap.repository.TicketTradeHistoryRepository;
import com.ticketswap.repository.UserRepository;
import com.ticketswap.util.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private EventService eventService;

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private TicketTradeHistoryRepository ticketTradeHistoryRepository;

    public ArtistDetailsDto getArtistForTicket(Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            throw new ResourceNotFoundException("Ticket with id " + ticketId + " not found.");
        }

        return spotifyService.getArtistDetails(ticket.get().getEvent().getEventEntity().getName());
    }

    public TicketDetailsDto getTicketById(Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isPresent()) {
            return TicketDetailsDto.map(ticket.get());
        }
        throw new ResourceNotFoundException("Ticket with id " + ticketId + " not found.");
    }

    public TicketWeatherResponseDto getWeatherForTicket(Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            throw new ResourceNotFoundException("Ticket with id " + ticketId + " not found.");
        }
        Location location = ticket.get().getEvent().getVenue().getLocation();
        String city = location.getCity();
        String country = location.getCountry();
        DailyWeatherInCityDto forecast = weatherService.getWeatherData(city, country, ticket.get().getEvent().getEventDate());
        if (forecast != null) {
            return TicketWeatherResponseDto.map(forecast);
        }
        throw new ResourceNotFoundException("Could not fetch weather forecast for ticket with id = " + ticketId);
    }

    public TicketDetailsDto updateTicket(TicketInsertDto ticketInsertDto) {
        Ticket ticket = ticketInsertDto.toEntity();
        Optional<Ticket> existingTicket = ticketRepository.findById(ticket.getId());
        User loggedInUser = authService.getLoggedInUser().get();
        if (existingTicket.isEmpty() || !existingTicket.get().getUser().getId().equals(loggedInUser.getId()))
            throw new ResourceNotFoundException(String.format("Ticket with id = %s does not exist or is not yours", ticket.getId().toString()));
        ticket.setEvent(eventService.getUpdatedEvent(ticketInsertDto.getEvent()));
        ticket.setCategories(categoryRepository.findAllById(ticket.getCategories().stream().map(Category::getId).toList()));
        ticket.setInterestedInCategories(categoryRepository.findAllById(ticket.getInterestedInCategories().stream().map(Category::getId).toList()));
        ticket.setUser(loggedInUser);
        ticket = ticketRepository.save(ticket);
        return TicketDetailsDto.map(ticket);
    }

    @Transactional
    public void deleteTicket(Long ticketId) {
        Optional<Ticket> existingTicket = ticketRepository.findById(ticketId);
        User loggedInUser = authService.getLoggedInUser().get();
        if (existingTicket.isEmpty() || !existingTicket.get().getUser().getId().equals(loggedInUser.getId()))
            throw new ResourceNotFoundException(String.format("Ticket with id = %s does not exist or is not yours", ticketId));

        ticketRepository.delete(existingTicket.get());
    }



    public TicketDetailsDto createTicket(TicketInsertDto ticketDetailsDto) {
        Ticket ticket = ticketDetailsDto.toEntity();
        if (ticket.getEvent().getEventDate().isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Event date can't be in the past");
        if (ticket.getDescription().length() > 255) throw new IllegalArgumentException("Ticket description must be shorter than 256 characters");
        ticket.setId(null);
        ticket.setCategories(categoryRepository.findAllById(ticket.getCategories().stream().map(Category::getId).toList()));
        ticket.setInterestedInCategories(categoryRepository.findAllById(ticket.getInterestedInCategories().stream().map(Category::getId).toList()));
        ticket.setUser(authService.getLoggedInUser().get());
        ticket = ticketRepository.save(ticket);
        return TicketDetailsDto.map(ticket);
    }

    public List<TicketSearchDto> searchTickets(TicketSearchParams searchParams) {
        List<TicketSearchDto> tickets = ticketRepository.findAll().stream().filter(ticket -> {

            if (!ticket.getStatus().isActive()) return false;

            List<Long> ticketCategoryIds = new ArrayList<>(ticket.getCategories().stream().map(Category::getId).toList());
            if (!searchParams.getCategoryIds().isEmpty()) {
                ticketCategoryIds.retainAll(searchParams.getCategoryIds());
                if (ticketCategoryIds.isEmpty()) return false;
            }

            if (searchParams.getOfferTypes().size() == 1) {
                if (ticket.getStatus() != searchParams.getOfferTypes().get(0)) return false;
            }

            if (ticket.getStatus().equals(TicketStatus.SELL)) {
                if (ticket.getPrice() < searchParams.getPriceMin()) return false;
                if (ticket.getPrice() > searchParams.getPriceMax()) return false;
            }
            String ticketCountry = ticket.getEvent().getVenue().getLocation().getCountry();
            if (!searchParams.getCountries().isEmpty() && !searchParams.getCountries().contains(ticketCountry)) return false;
            String ticketCity = ticket.getEvent().getVenue().getLocation().getCity();
            if (!searchParams.getCities().isEmpty() && !searchParams.getCities().contains(ticketCity)) return false;

            LocalDateTime eventDate = ticket.getEvent().getEventDate();
            if (eventDate.isBefore(searchParams.getStartDate())) return false;
            if (eventDate.isAfter(searchParams.getEndDate())) return false;

            return true;
        }).map(TicketSearchDto::map).toList();
        return tickets;
    }

    public TicketDetailsDto toggleTicketStatus(UpdateTicketStatusDto updateTicketStatusDto) {
        Optional<Ticket> existingTicket = ticketRepository.findById(updateTicketStatusDto.getTicketId());
        User loggedInUser = authService.getLoggedInUser().get();
        if (existingTicket.isEmpty() || !existingTicket.get().getUser().getId().equals(loggedInUser.getId()))
            throw new ResourceNotFoundException(String.format("Ticket with id = %s does not exist or is not yours", updateTicketStatusDto.getTicketId()));
        Ticket updatedTicket = existingTicket.get();
        updatedTicket.setStatus(updateTicketStatusDto.getTicketStatus());
        return TicketDetailsDto.map(ticketRepository.save(updatedTicket));
    }
}
