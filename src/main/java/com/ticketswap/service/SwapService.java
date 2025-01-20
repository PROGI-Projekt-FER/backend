package com.ticketswap.service;

import com.ticketswap.dto.swap.RequestDetailsDto;
import com.ticketswap.dto.ticket.TicketDetailsDto;
import com.ticketswap.model.*;
import com.ticketswap.repository.*;
import com.ticketswap.util.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private SwapCycleRepository swapCycleRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TicketTradeHistoryRepository ticketTradeHistoryRepository;

    @Transactional
    public SwapCycle createSwapCycle(Ticket initialRequestingTicket, Ticket initialReceivingTicket) throws Exception {
        if (areTicketsSwappable(initialRequestingTicket, initialReceivingTicket)) return null; // traditional 1 for 1 swap can be made and cycle isn't needed
        Optional<Ticket> potentialThirdTicket = ticketRepository.findAll().stream().filter(ticket ->
            areTicketsSwappable(ticket, initialReceivingTicket)
                    && areTicketsSwappable(initialRequestingTicket, ticket)
                    && !ticket.getUser().getId().equals(initialReceivingTicket.getUser().getId())
                    && !ticket.getUser().getId().equals(initialRequestingTicket.getUser().getId())
                    && ticket.getStatus().equals(TicketStatus.SWAP)
        ).findAny();
        if (potentialThirdTicket.isEmpty()) {
            // if we can't find a match by interested categories just complete the cycle with a random ticket
            potentialThirdTicket = ticketRepository.findAll().stream().filter(ticket ->
                    !ticket.getUser().getId().equals(initialReceivingTicket.getUser().getId())
                            && !ticket.getUser().getId().equals(initialRequestingTicket.getUser().getId())
                            && ticket.getStatus().equals(TicketStatus.SWAP)
            ).findAny();
        }
        if (potentialThirdTicket.isEmpty()) throw new Exception("Cycle can't be created because there is not eligible tickets");

        Ticket thirdTicket = potentialThirdTicket.get();

        SwapCycle swapCycle = new SwapCycle();
        swapCycle.setInitiatedByUser(initialRequestingTicket.getUser());

        swapCycle = swapCycleRepository.save(swapCycle);

        SwapRequest swapRequest1 = new SwapRequest();
        swapRequest1.setSendingTicket(initialReceivingTicket);
        swapRequest1.setReceivingTicket(initialRequestingTicket);
        swapRequest1.setSwapCycle(swapCycle);

        Notification notification1 = new Notification();
        notification1.setUser(initialReceivingTicket.getUser());
        notification1.setMessage("You have been added to a swap cycle!");

        SwapRequest swapRequest2 = new SwapRequest();
        swapRequest2.setSendingTicket(thirdTicket);
        swapRequest2.setReceivingTicket(initialReceivingTicket);
        swapRequest2.setSwapCycle(swapCycle);

        Notification notification2 = new Notification();
        notification2.setUser(thirdTicket.getUser());
        notification2.setMessage("You have been added to a swap cycle!");

        SwapRequest swapRequest3 = new SwapRequest();
        swapRequest3.setSendingTicket(initialRequestingTicket);
        swapRequest3.setReceivingTicket(thirdTicket);
        swapRequest3.setSwapCycle(swapCycle);

        notificationRepository.saveAll(List.of(notification1, notification2));
        swapRequestRepository.saveAll(List.of(swapRequest1, swapRequest2, swapRequest3));

        return swapCycle;
    }

    // tickets are swappable if one of the requesting ticket categories is contained in the receiving ticket interestedInCategories list
    private boolean areTicketsSwappable(Ticket requesting, Ticket receiving) {
        for (Category category: receiving.getInterestedInCategories()) {
            if (requesting.getCategories().stream().map(Category::getId).anyMatch(id -> id.equals(category.getId()))) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void requestTicketSwap(User loggedInUser, Long requestingTicketId, Long receivingTicketId, boolean attemptCycle) throws Exception {
        Ticket requestingTicket = ticketRepository.findById(requestingTicketId).orElseThrow(() -> new ResourceNotFoundException("Requesting ticket not found"));
        Ticket receivingTicket = ticketRepository.findById(receivingTicketId).orElseThrow(() -> new ResourceNotFoundException("Receiving ticket not found"));
        if (!loggedInUser.getId().equals(requestingTicket.getUser().getId())) throw new Exception("Cannot swap a ticket that is not yours");
        if (receivingTicket.getUser().getId().equals(loggedInUser.getId())) throw new Exception("Cannot swap for your own ticket");
        if (!receivingTicket.getStatus().equals(TicketStatus.SWAP) || !requestingTicket.getStatus().equals(TicketStatus.SWAP)) throw new Exception("Both tickets need to be of status SWAP");
        if (doesActiveSwapExist(requestingTicketId, receivingTicketId)) throw new Exception("There already exists an active swap for these two tickets");

        if (!areTicketsSwappable(requestingTicket, receivingTicket) && attemptCycle) {
            createSwapCycle(requestingTicket, receivingTicket);
            return;
        } else if (!areTicketsSwappable(requestingTicket, receivingTicket)){
            throw new Exception("Tickets aren't compatible for swap and cycle wasn't requested");
        }

        SwapRequest swapRequest = new SwapRequest();
        swapRequest.setSendingTicket(requestingTicket);
        swapRequest.setReceivingTicket(receivingTicket);

        Notification notification = new Notification();
        notification.setUser(receivingTicket.getUser());
        notification.setMessage("You have an incoming swap request!");
        notificationRepository.save(notification);
        swapRequestRepository.save(swapRequest);
    }

    @Transactional
    public void buyTicket(User loggedInUser, Long ticketId) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.getUser().getId().equals(loggedInUser.getId())) throw new Exception("Cannot buy your own ticket");
        if (!ticket.getStatus().equals(TicketStatus.SELL)) throw new Exception("Ticket is not of type SELL");

        Notification notification = new Notification();
        notification.setUser(ticket.getUser());
        notification.setMessage("Your ticket has been sold!");
        notification.setSeenByUser(false);
        notificationRepository.save(notification);

        TicketTradeHistory ticketTradeHistory = new TicketTradeHistory();
        ticketTradeHistory.setTicket(ticket);
        ticketTradeHistory.setSoldForPrice(ticket.getPrice());
        ticketTradeHistory.setTradeType(TradeType.SELL);
        ticketTradeHistory.setPreviousOwner(ticket.getUser());
        ticketTradeHistory.setNewOwner(loggedInUser);
        ticketTradeHistoryRepository.save(ticketTradeHistory);

        ticket.setUser(loggedInUser);
        ticket.setStatus(TicketStatus.EXCHANGED);
        ticketRepository.save(ticket);
    }

    @Transactional
    public SwapConfirmation respondToTicketSwap(User loggedInUser, Long swapRequestId, boolean isAccepting) throws Exception {
        SwapRequest swapRequest = swapRequestRepository.findById(swapRequestId).orElseThrow(() -> new ResourceNotFoundException("Swap request not found"));
        if (!swapRequest.getReceivingTicket().getUser().getId().equals(loggedInUser.getId())) throw new Exception("Cannot respond to a swap request not directed to one of your tickets");
        if (!swapRequest.getConfirmationStatus().equals(ConfirmationStatus.PENDING)) throw new Exception("This swap was already approved or canceled");

        ConfirmationStatus responseStatus;
        if (isAccepting) responseStatus = ConfirmationStatus.APPROVED;
        else responseStatus = ConfirmationStatus.CANCELED;

        swapRequest.setConfirmationStatus(responseStatus);

        SwapConfirmation swapConfirmation = new SwapConfirmation();
        swapConfirmation.setSwapRequest(swapRequest);
        swapConfirmation.setConfirmationStatus(responseStatus);

        SwapCycle swapCycle = swapRequest.getSwapCycle();

        if (swapCycle != null) {

            if (!isAccepting) {
                cancelSwapCycle(swapCycle);
                return swapConfirmationRepository.save(swapConfirmation);
            }

            swapConfirmation = swapConfirmationRepository.save(swapConfirmation);
            handleSwapCycleUpdate(swapCycle);
            return swapConfirmation;
        } else {

            Notification notification = new Notification();
            notification.setUser(swapRequest.getSendingTicket().getUser());
            notification.setSeenByUser(false);
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

    }

    @Transactional
    public void cancelSwapCycle(SwapCycle swapCycle) {
        List<SwapRequest> swapRequests = swapRequestRepository.findSwapRequestsBySwapCycleId(swapCycle.getId());
        swapRequests.forEach(swapRequest -> swapRequest.setConfirmationStatus(ConfirmationStatus.CANCELED));
        swapRequestRepository.saveAll(swapRequests);
        swapCycle.setConfirmationStatus(ConfirmationStatus.CANCELED);
        swapCycleRepository.save(swapCycle);
    }

    @Transactional
    public void handleSwapCycleUpdate(SwapCycle swapCycle) {
        List<SwapRequest> swapRequests = swapRequestRepository.findSwapRequestsBySwapCycleId(swapCycle.getId());
        boolean allApproved = true;
        for (SwapRequest request: swapRequests) {
            if (!request.getConfirmationStatus().equals(ConfirmationStatus.APPROVED)) {
                allApproved = false;
                break;
            }
        }
        if (allApproved) {
            swapCycle.setConfirmationStatus(ConfirmationStatus.APPROVED);
            swapCycle = swapCycleRepository.save(swapCycle);

            for (SwapRequest request: swapRequests) {
                Ticket ticket = request.getReceivingTicket();
                ticket.setStatus(TicketStatus.EXCHANGED);
                request.setSwapCycle(swapCycle);

                TicketTradeHistory ticketTradeHistory = new TicketTradeHistory();
                ticketTradeHistory.setTicket(request.getSendingTicket());
                ticketTradeHistory.setSwappedForTicket(ticket);
                ticketTradeHistory.setTradeType(TradeType.SWAP);
                ticketTradeHistory.setPreviousOwner(request.getSendingTicket().getUser());
                ticketTradeHistory.setNewOwner(ticket.getUser());
                ticketTradeHistoryRepository.save(ticketTradeHistory);
            }

            User firstReceivingUser = swapRequests.get(0).getReceivingTicket().getUser();
            User secondReceivingUser = swapRequests.get(1).getReceivingTicket().getUser();
            User thirdReceivingUser = swapRequests.get(2).getReceivingTicket().getUser();

            swapRequests.get(0).getSendingTicket().setUser(firstReceivingUser);
            swapRequests.get(1).getSendingTicket().setUser(secondReceivingUser);
            swapRequests.get(2).getSendingTicket().setUser(thirdReceivingUser);

            swapRequestRepository.saveAll(swapRequests);
        }
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
