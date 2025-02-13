package com.dotnt.microservices.cinema.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "Customer")
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends AbstractEntity<UUID> {


    private int loyalty_points;


    private boolean marketing_opt_in;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", orphanRemoval = true)
    @JoinColumn(name = "subscription_id")
    private List<Subscription> subscriptions;
}
