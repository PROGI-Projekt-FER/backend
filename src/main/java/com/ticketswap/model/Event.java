package com.ticketswap.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity(name = "event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name =  "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_entity_id")
    private EventEntity eventEntity;

}
