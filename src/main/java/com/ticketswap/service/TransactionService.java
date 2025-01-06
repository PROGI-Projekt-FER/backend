package com.ticketswap.service;

import com.ticketswap.dto.transaction.TransactionDto;
import com.ticketswap.model.Ticket;
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
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(TransactionDto::map)
                .collect(Collectors.toList());
    }

    public TransactionDto saveTransaction(TransactionDto transactionDto) {
        User buyer = userRepository.findById(transactionDto.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));
        Ticket ticket = ticketRepository.findById(transactionDto.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        Transaction transaction = transactionDto.toEntity(transactionDto, buyer, ticket);
        Transaction savedTransaction = transactionRepository.save(transaction);
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
