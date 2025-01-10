package com.ticketswap.controller;

import com.ticketswap.dto.transaction.TransactionDto;
import com.ticketswap.model.User;
import com.ticketswap.service.AuthService;
import com.ticketswap.service.TransactionService;
import com.ticketswap.util.NotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AuthService authService;

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping("/tickets/{ticketId}/buy")
    public ResponseEntity<TransactionDto> buyTicket(@PathVariable("ticketId") Long ticketId) throws Exception {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        return ResponseEntity.ok(transactionService.buyTicket(ticketId, loggedInUser));
    }
}
