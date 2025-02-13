package com.dotnt.microservices.cinema.model;

import com.dotnt.microservices.cinema.common.SeatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity(name = "Seat")
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat extends AbstractEntity<UUID> {
    @Column(nullable = false)
    private String row;  // e.g., "A", "B", "C"

    @Column(nullable = false)
    private Integer number;  // e.g., 1, 2, 3

    @Enumerated(EnumType.STRING)
    private SeatType type;

    @OneToMany(mappedBy = "seat")
//    @JsonManagedReference
    @JsonIgnore
    private Set<HallHasSeat> hallHasSeats;
}
