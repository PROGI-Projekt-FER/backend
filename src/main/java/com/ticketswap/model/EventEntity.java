package com.ticketswap.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity(name = "event_entity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type")
    private String type;

    @Column(name = "name", nullable = false)
    private String name;

}
