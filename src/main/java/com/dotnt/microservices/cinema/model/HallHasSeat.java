package com.dotnt.microservices.cinema.model;

import com.dotnt.microservices.cinema.common.SeatStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity(name = "HallHasSeat")
@Table(name = "hall_seat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HallHasSeat extends AbstractEntity<UUID> {

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    @JsonBackReference
    private Hall hall;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
//    @JsonBackReference
//    @JsonIgnore
    private Seat seat;

    private SeatStatus status;
}
