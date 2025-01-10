package com.ticketswap.service;

import com.ticketswap.model.*;
import com.ticketswap.repository.*;
import com.ticketswap.util.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwapService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SwapRequestRepository swapRequestRepository;

    @Autowired
    private SwapConfirmationRepository swapConfirmationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public SwapRequest requestTicketSwap(User loggedInUser, Long requestingTicketId, Long receivingTicketId) throws Exception {
        Ticket requestingTicket = ticketRepository.findById(requestingTicketId).orElseThrow(() -> new ResourceNotFoundException("Requesting ticket not found"));
        Ticket receivingTicket = ticketRepository.findById(receivingTicketId).orElseThrow(() -> new ResourceNotFoundException("Receiving ticket not found"));
        if (!loggedInUser.getId().equals(requestingTicket.getUser().getId())) throw new Exception("Cannot swap a ticket that is not yours");

        SwapCycle swapCycle = new SwapCycle();
        swapCycle.setInitiatedByUser(loggedInUser);

        SwapRequest swapRequest = new SwapRequest();
        swapRequest.setSwapCycle(swapCycle);
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
        swapRequest.getSwapCycle().setConfirmationStatus(responseStatus);

        SwapConfirmation swapConfirmation = new SwapConfirmation();
        swapConfirmation.setSwapRequest(swapRequest);
        swapConfirmation.setConfirmationStatus(responseStatus);

        Notification notification = new Notification();
        notification.setUser(swapRequest.getSendingTicket().getUser());
        if (responseStatus == ConfirmationStatus.APPROVED) notification.setMessage("One of your swap requests has been accepted!");
        else notification.setMessage("One of your swap requests has been declined!");

        notificationRepository.save(notification);

        return swapConfirmationRepository.save(swapConfirmation);
    }
}
