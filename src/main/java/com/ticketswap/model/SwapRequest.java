package com.ticketswap.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity(name = "swap_request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SwapRequest extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "swap_cycle_id")
    private SwapCycle swapCycle;

    @ManyToOne
    @JoinColumn(name = "receiving_ticket_id")
    private Ticket receivingTicket;

    @ManyToOne
    @JoinColumn(name = "sending_ticket_id")
    private Ticket sendingTicket;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ConfirmationStatus confirmationStatus = ConfirmationStatus.PENDING;

}
