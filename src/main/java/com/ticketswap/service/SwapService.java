package com.ticketswap.service;

import com.ticketswap.dto.swap.RequestDetailsDto;
import com.ticketswap.dto.ticket.TicketDetailsDto;
import com.ticketswap.model.*;
import com.ticketswap.repository.*;
import com.ticketswap.util.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SwapService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SwapRequestRepository swapRequestRepository;

    @Autowired
    private SwapConfirmationRepository swapConfirmationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TicketTradeHistoryRepository ticketTradeHistoryRepository;

    @Transactional
    public SwapRequest requestTicketSwap(User loggedInUser, Long requestingTicketId, Long receivingTicketId) throws Exception {
        Ticket requestingTicket = ticketRepository.findById(requestingTicketId).orElseThrow(() -> new ResourceNotFoundException("Requesting ticket not found"));
        Ticket receivingTicket = ticketRepository.findById(receivingTicketId).orElseThrow(() -> new ResourceNotFoundException("Receiving ticket not found"));
        if (!loggedInUser.getId().equals(requestingTicket.getUser().getId())) throw new Exception("Cannot swap a ticket that is not yours");
        if (receivingTicket.getUser().getId().equals(loggedInUser.getId())) throw new Exception("Cannot swap for your own ticket");
        if (!receivingTicket.getStatus().equals(TicketStatus.SWAP) || !requestingTicket.getStatus().equals(TicketStatus.SWAP)) throw new Exception("Both tickets need to be of status SWAP");
        if (doesActiveSwapExist(requestingTicketId, receivingTicketId)) throw new Exception("There already exists an active swap for these two tickets");

        SwapRequest swapRequest = new SwapRequest();
        swapRequest.setSendingTicket(requestingTicket);
        swapRequest.setReceivingTicket(receivingTicket);

        Notification notification = new Notification();
        notification.setUser(receivingTicket.getUser());
        notification.setMessage("You have an incoming swap request!");
        notificationRepository.save(notification);

        return swapRequestRepository.save(swapRequest);
    }

    @Transactional
    public SwapConfirmation respondToTicketSwap(User loggedInUser, Long swapRequestId, boolean isAccepting) throws Exception {
        SwapRequest swapRequest = swapRequestRepository.findById(swapRequestId).orElseThrow(() -> new ResourceNotFoundException("Swap request not found"));
        if (!swapRequest.getReceivingTicket().getUser().getId().equals(loggedInUser.getId())) throw new Exception("Cannot respond to a swap request not directed to one of your tickets");

        ConfirmationStatus responseStatus;
        if (isAccepting) responseStatus = ConfirmationStatus.APPROVED;
        else responseStatus = ConfirmationStatus.CANCELED;

        swapRequest.setConfirmationStatus(responseStatus);

        SwapCycle swapCycle = swapRequest.getSwapCycle();
        if (swapCycle != null) {
            swapCycle.setConfirmationStatus(responseStatus);
        }
        swapRequest.setSwapCycle(swapCycle);

        SwapConfirmation swapConfirmation = new SwapConfirmation();
        swapConfirmation.setSwapRequest(swapRequest);
        swapConfirmation.setConfirmationStatus(responseStatus);

        Notification notification = new Notification();
        notification.setUser(swapRequest.getSendingTicket().getUser());
        if (responseStatus == ConfirmationStatus.APPROVED) notification.setMessage("One of your swap requests has been accepted!");
        else notification.setMessage("One of your swap requests has been declined!");
        notificationRepository.save(notification);

        if (isAccepting) {
            Ticket receivingTicket = swapRequest.getReceivingTicket();
            receivingTicket.setStatus(TicketStatus.EXCHANGED);
            Ticket requestingTicket = swapRequest.getSendingTicket();
            requestingTicket.setStatus(TicketStatus.EXCHANGED);
            User previousOwner = swapRequest.getReceivingTicket().getUser();
            User newOwner = swapRequest.getSendingTicket().getUser();
            receivingTicket.setUser(newOwner);
            requestingTicket.setUser(previousOwner);
            ticketRepository.saveAll(List.of(receivingTicket, requestingTicket));

            TicketTradeHistory ticketTradeHistory = new TicketTradeHistory();
            ticketTradeHistory.setTicket(receivingTicket);
            ticketTradeHistory.setSwappedForTicket(requestingTicket);
            ticketTradeHistory.setTradeType(TradeType.SWAP);
            ticketTradeHistory.setPreviousOwner(previousOwner);
            ticketTradeHistory.setNewOwner(newOwner);
            ticketTradeHistoryRepository.save(ticketTradeHistory);
        }

        return swapConfirmationRepository.save(swapConfirmation);
    }

    public RequestDetailsDto getRequestDetails(User loggedInUser, Long swapRequestId) throws Exception {
        SwapRequest swapRequest = swapRequestRepository.findById(swapRequestId).orElseThrow(() -> new ResourceNotFoundException("Swap request not found"));
        if (!swapRequest.getSendingTicket().getUser().getId().equals(loggedInUser.getId()) && !swapRequest.getReceivingTicket().getUser().getId().equals(loggedInUser.getId()))
            throw new Exception("You are not a part of this swap request");
        RequestDetailsDto requestDetailsDto = new RequestDetailsDto();
        requestDetailsDto.setReceivingTicket(TicketDetailsDto.map(swapRequest.getReceivingTicket()));
        requestDetailsDto.setSendingTicket(TicketDetailsDto.map(swapRequest.getSendingTicket()));
        requestDetailsDto.setSentAt(swapRequest.getCreatedAt());
        requestDetailsDto.setConfirmationStatus(swapRequest.getConfirmationStatus());
        if (swapRequest.getConfirmationStatus() != ConfirmationStatus.PENDING) {
            Optional<SwapConfirmation> swapConfirmation = swapConfirmationRepository.findSwapConfirmationBySwapRequestId(swapRequestId);
            if (swapConfirmation.isPresent() && swapConfirmation.get().getConfirmationStatus() != ConfirmationStatus.PENDING) {
                requestDetailsDto.setRespondedAt(swapConfirmation.get().getCreatedAt());
            }
        }
        return requestDetailsDto;
    }

    private boolean doesActiveSwapExist(Long requestingTicketId, Long receivingTicketId) {
        Optional<SwapRequest> swapRequest = swapRequestRepository.findSwapRequestByReceivingTicketIdAndSendingTicketId(receivingTicketId, requestingTicketId);
        return swapRequest.isPresent() && swapRequest.get().getConfirmationStatus().equals(ConfirmationStatus.PENDING);
    }
}
