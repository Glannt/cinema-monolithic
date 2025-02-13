package com.dotnt.microservices.cinema.dto.response;

import com.dotnt.microservices.cinema.dto.SeatDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class HallCreationResponse {
    private UUID id;
    private String name;
    private String status;
    private List<SeatDTO> seats;

    public List<SeatDTO> getSeats() {
        return seats != null ? Collections.unmodifiableList(seats) : Collections.emptyList();
    }
}
