package com.dotnt.microservices.cinema.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "Subscription")
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription extends AbstractEntity<UUID>{


    private String name;


    private double discount;



    @ManyToOne(fetch = FetchType.LAZY)

    private Customer customer;
}
