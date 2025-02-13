package com.dotnt.microservices.cinema.dto.request;

import com.dotnt.microservices.cinema.model.Address;
import com.dotnt.microservices.cinema.model.Hall;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaDTO {
    private String name;
    private Address address;
    private String status;
    private List<Hall> halls;
}
