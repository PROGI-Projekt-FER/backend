package com.ticketswap.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity(name = "swap_confirmation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SwapConfirmation extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "swap_request_id")
    private SwapRequest swapRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ConfirmationStatus confirmationStatus = ConfirmationStatus.PENDING;
}
