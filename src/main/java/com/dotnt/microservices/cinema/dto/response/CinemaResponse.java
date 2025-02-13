package com.dotnt.microservices.cinema.dto.response;

import com.dotnt.microservices.cinema.model.Address;
import com.dotnt.microservices.cinema.model.Hall;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CinemaResponse {
    private UUID id;
    private String name;
    private Address address;
    private String status;
    private List<Hall> halls;
}
