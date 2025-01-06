package com.ticketswap.dto.transaction;

import com.ticketswap.model.Ticket;
import com.ticketswap.model.Transaction;
import com.ticketswap.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private Long ticketId;
    private Long buyerId;

    public static TransactionDto map(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getTicket() != null ? transaction.getTicket().getId() : null,
                transaction.getBuyer() != null ? transaction.getBuyer().getId() : null
        );
    }

    public Transaction toEntity(TransactionDto transactionDTO, User buyer, Ticket ticket) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setBuyer(buyer);
        transaction.setTicket(ticket);
        return transaction;
    }
}
