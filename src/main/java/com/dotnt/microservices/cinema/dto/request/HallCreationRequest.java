package com.dotnt.microservices.cinema.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HallCreationRequest {
    private UUID cinemaId;
    private String name;
    private String projectionType;
    private String status;
    private int seatCount;
    private int seatsPerRow;
}
