package com.dotnt.microservices.cinema.model;

import com.dotnt.microservices.cinema.common.CinemaStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Cinema")
@Table(name = "cinema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema extends AbstractEntity<UUID> {
    private String name;

    @Enumerated(EnumType.STRING)
    private CinemaStatus status;
    private UUID addressId;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Hall> halls = new ArrayList<>();
}
