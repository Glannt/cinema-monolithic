package com.dotnt.microservices.cinema.services.impl;

import com.dotnt.microservices.cinema.common.SeatType;
import com.dotnt.microservices.cinema.model.Hall;
import com.dotnt.microservices.cinema.model.HallHasSeat;
import com.dotnt.microservices.cinema.model.Seat;
import com.dotnt.microservices.cinema.services.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SEAT-SERVICE")
public class SeatServiceImpl implements SeatService {
    @Override
    public List<Seat> createSeatsForHall(Hall hall, int seatCount) {
        List<Seat> seats = new ArrayList<>();
        int seatsPerRow = 10;
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        for (int rowIndex = 0; rowIndex < rows.length && seats.size() < seatCount; rowIndex++) {
            String currentRow = rows[rowIndex];
            for (int seatNum = 1; seatNum <= seatsPerRow && seats.size() < seatCount; seatNum++) {
                Seat seat = Seat
                        .builder()
                        .row(currentRow)
                        .number(seatNum)
                        .type(SeatType.STANDARD)
                        .hallHasSeats(new HashSet<>())
                        .build();

                HallHasSeat hallHasSeat = HallHasSeat
                        .builder()
                        .seat(seat)
                        .hall(hall)
                        .build();
                seat.getHallHasSeats().add(hallHasSeat);
                seats.add(seat);
            }
        }

        return seats;
    }
}
