package com.dotnt.microservices.cinema.services;

import com.dotnt.microservices.cinema.model.Hall;
import com.dotnt.microservices.cinema.model.Seat;

import java.util.List;

public interface SeatService {
    List<Seat> createSeatsForHall(Hall hall, int seatCount);
}
