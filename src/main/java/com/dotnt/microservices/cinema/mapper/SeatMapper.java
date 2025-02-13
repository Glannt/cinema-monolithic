package com.dotnt.microservices.cinema.mapper;

import com.dotnt.microservices.cinema.common.SeatType;
import com.dotnt.microservices.cinema.dto.SeatDTO;
import com.dotnt.microservices.cinema.model.Seat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatMapper {
    public SeatDTO toDTO(Seat seat) {
        return SeatDTO.builder()
                .id(seat.getId())
                .row(seat.getRow())
                .number(seat.getNumber())
                .type(seat.getType().toString())
                .build();
    }

    public List<SeatDTO> toDTOList(List<Seat> seats) {
        return seats.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Seat toEntity(SeatDTO dto) {
        return Seat.builder()
                .row(dto.getRow())
                .number(dto.getNumber())
                .type(SeatType.valueOf(dto.getType().toUpperCase()))
                .build();
    }
}
