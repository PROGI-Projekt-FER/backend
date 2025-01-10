package com.ticketswap.service;

import com.ticketswap.dto.transaction.TransactionDto;
import com.ticketswap.model.Ticket;
import com.ticketswap.model.TicketStatus;
import com.ticketswap.model.Transaction;
import com.ticketswap.model.User;
import com.ticketswap.repository.TicketRepository;
import com.ticketswap.repository.TransactionRepository;
import com.ticketswap.repository.UserRepository;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private TicketRepository ticketRepository;

    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(TransactionDto::map)
                .collect(Collectors.toList());
    }

    public TransactionDto buyTicket(Long ticketId, User buyer) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (!ticket.getStatus().equals(TicketStatus.SELL)) throw new Exception("This ticket cannot be sold.");
        if (buyer.getId().equals(ticket.getUser().getId())) throw new Exception("You cannot buy your own ticket.");
        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setTicket(ticket);
        Transaction savedTransaction = transactionRepository.save(transaction);
        ticket.setStatus(TicketStatus.EXCHANGED);
        ticketRepository.save(ticket);
        return TransactionDto.map(savedTransaction);
    }

    public void deleteTransaction(Long id) {
        if (id == null) throw new ResourceNotFoundException("Transaction Id can not be null");
        Transaction transaction = new Transaction();
        transaction.setId(id);
        if(!transactionRepository.existsById(id)) throw new ResourceNotFoundException("Transaction with id " + id + " not found");
        transactionRepository.deleteById(id);
    }
}
