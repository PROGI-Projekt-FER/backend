package com.ticketswap.service;

import com.ticketswap.dto.ticket.TicketDetailsDto;
import com.ticketswap.dto.ticket.TicketInsertDto;
import com.ticketswap.dto.ticket.TicketSearchDto;
import com.ticketswap.dto.user.UsernameDto;
import com.ticketswap.model.Category;
import com.ticketswap.model.Ticket;
import com.ticketswap.model.User;
import com.ticketswap.repository.CategoryRepository;
import com.ticketswap.repository.TicketRepository;
import com.ticketswap.repository.UserRepository;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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

    public TicketDetailsDto getTicketById(Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isPresent()) {
            return TicketDetailsDto.map(ticket.get());
        }
        throw new ResourceNotFoundException("Ticket with id " + ticketId + " not found.");
    }

    public TicketDetailsDto updateTicket(TicketInsertDto ticketInsertDto) {
        Ticket ticket = ticketInsertDto.toEntity();
        Optional<Ticket> existingTicket = ticketRepository.findById(ticket.getId());
        User loggedInUser = authService.getLoggedInUser().get();
        if (existingTicket.isEmpty() || !existingTicket.get().getUser().getId().equals(loggedInUser.getId()))
            throw new ResourceNotFoundException(String.format("Ticket with id = %s does not exist or is not yours", ticket.getId().toString()));
        ticket.setCategories(categoryRepository.findAllById(ticket.getCategories().stream().map(Category::getId).toList()));
        ticket.setInterestedInCategories(categoryRepository.findAllById(ticket.getInterestedInCategories().stream().map(Category::getId).toList()));
        ticket.setUser(loggedInUser);
        ticket = ticketRepository.save(ticket);
        return TicketDetailsDto.map(ticket);
    }

    public void deleteTicket(Long ticketId) {
        Optional<Ticket> existingTicket = ticketRepository.findById(ticketId);
        User loggedInUser = authService.getLoggedInUser().get();
        if (existingTicket.isEmpty() || !existingTicket.get().getUser().getId().equals(loggedInUser.getId()))
            throw new ResourceNotFoundException(String.format("Ticket with id = %s does not exist or is not yours", ticketId));
        ticketRepository.delete(existingTicket.get());
    }



    public TicketDetailsDto createTicket(TicketInsertDto ticketDetailsDto) {
        Ticket ticket = ticketDetailsDto.toEntity();
        ticket.setId(null);
        ticket.setCategories(categoryRepository.findAllById(ticket.getCategories().stream().map(Category::getId).toList()));
        ticket.setInterestedInCategories(categoryRepository.findAllById(ticket.getInterestedInCategories().stream().map(Category::getId).toList()));
        ticket.setUser(authService.getLoggedInUser().get());
        ticket = ticketRepository.save(ticket);
        return TicketDetailsDto.map(ticket);
    }

    public List<TicketSearchDto> searchTickets() {
        List<TicketSearchDto> tickets = ticketRepository.findAll().stream().map(TicketSearchDto::map).toList();
        return tickets;
    }
}
