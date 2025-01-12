package com.ticketswap.service;

import com.ticketswap.dto.swap.RequestDetailsDto;
import com.ticketswap.dto.ticket.TicketDetailsDto;
import com.ticketswap.dto.ticket.TicketHistoryDto;
import com.ticketswap.dto.user.UserDto;
import com.ticketswap.dto.user.UserEditDto;
import com.ticketswap.model.*;
import com.ticketswap.repository.*;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketTradeHistoryRepository ticketTradeHistoryRepository;

    @Autowired
    private SwapRequestRepository swapRequestRepository;

    public UserDto getProfile(User user) {
        return UserDto.map(user);
    }

    public void editProfile(User user, UserEditDto userEditDto) {
        user.setFirstName(userEditDto.getFirstName());
        user.setLastName(userEditDto.getLastName());
        user.setUsername(userEditDto.getUsername());

        if (userEditDto.getPreferredCategoryId() != null) {
            Long categoryId = userEditDto.getPreferredCategoryId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + categoryId + " not found."));
            user.setPreferredCategory(category);
        }

        userRepository.save(user);
    }

    public List<TicketHistoryDto> getTradeHistory(User loggedInUser) {
        List<TicketTradeHistory> tradeHistory = ticketTradeHistoryRepository.findTicketTradeHistoriesByPreviousOwnerIdOrNewOwnerIdOrderByCreatedAtDesc(loggedInUser.getId(), loggedInUser.getId());

        return tradeHistory.stream().map(TicketHistoryDto::map).toList();
    }

    public List<TicketDetailsDto> getMyTickets(User loggedInUser) {
        List<Ticket> myTickets = ticketRepository.findAllByUserId(loggedInUser.getId());
        return myTickets.stream().map(TicketDetailsDto::map).toList();
    }

    public List<RequestDetailsDto> getPendingRequests(User loggedInUser) {
        List<SwapRequest> pendingRequests = swapRequestRepository.findAllByReceivingTicketUserId(loggedInUser.getId());
        return pendingRequests.stream()
                .filter(request -> request.getConfirmationStatus().equals(ConfirmationStatus.PENDING))
                .map(request -> RequestDetailsDto.map(request, null))
                .toList();
    }
}
