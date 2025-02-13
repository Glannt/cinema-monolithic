package com.dotnt.microservices.cinema.services.impl;

import com.dotnt.microservices.cinema.common.CinemaStatus;
import com.dotnt.microservices.cinema.common.ProjectionType;
import com.dotnt.microservices.cinema.common.SeatStatus;
import com.dotnt.microservices.cinema.common.SeatType;
import com.dotnt.microservices.cinema.dto.SeatDTO;
import com.dotnt.microservices.cinema.dto.request.HallCreationRequest;
import com.dotnt.microservices.cinema.dto.response.HallCreationResponse;
import com.dotnt.microservices.cinema.exception.AppException;
import com.dotnt.microservices.cinema.exception.ErrorCode;
import com.dotnt.microservices.cinema.mapper.SeatMapper;
import com.dotnt.microservices.cinema.model.Cinema;
import com.dotnt.microservices.cinema.model.Hall;
import com.dotnt.microservices.cinema.model.HallHasSeat;
import com.dotnt.microservices.cinema.model.Seat;
import com.dotnt.microservices.cinema.repositories.CinemaRepository;
import com.dotnt.microservices.cinema.repositories.HallHasSeatRepository;
import com.dotnt.microservices.cinema.repositories.HallRepository;
import com.dotnt.microservices.cinema.repositories.SeatRepository;
import com.dotnt.microservices.cinema.services.HallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "HALL-SERVICE")
public class HallServiceImpl implements HallService {
    private final SeatMapper seatMapper;
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;
    private final CinemaRepository cinemaRepository;
    private final HallHasSeatRepository hallHasSeatRepository;

    @Override
    @Transactional
    public HallCreationResponse createHall(HallCreationRequest request) {
        //find cinema with id
        Cinema cinema = cinemaRepository.findCinemaById(request.getCinemaId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        //build hall
        Hall hall = Hall
                .builder()
                .cinema(cinema)
                .projectionType(ProjectionType.valueOf(request.getProjectionType()))
                .seatCount(request.getSeatCount())
                .status(CinemaStatus.valueOf(request.getStatus()))
                .build();

        //Add hall into cinema
        cinema.getHalls().add(hall);

        //save hall to db
        hallRepository.save(hall);


//        List<Seat> seatList = seatRepository.findAll();
//        List<SeatDTO> seatDTOs = seatMapper.toDTOList(seatList);

        List<HallHasSeat> hallSeats = createHallSeats(hall, request.getSeatCount(), request.getSeatsPerRow());
        List<HallHasSeat> savedHallSeats = hallHasSeatRepository.saveAll(hallSeats);

        // 4. Map to response
        List<SeatDTO> seatDTOs = savedHallSeats.stream()
                .map(hallSeat -> seatMapper.toDTO(hallSeat.getSeat()))
                .collect(Collectors.toList());
        return HallCreationResponse.builder()
                .id(hall.getId())
                .name(request.getName())
                .status(request.getStatus())
                .seats(seatDTOs)
                .build();
    }

    @Override
    public HallCreationResponse updateHall(HallCreationRequest request) {
        return null;
    }

    @Override
    public HallCreationResponse deleteHall(HallCreationRequest request) {
        return null;
    }

    private List<HallHasSeat> createHallSeats(Hall hall, int seatCount, int seatsPerRow) {
        List<HallHasSeat> hallSeats = new ArrayList<>();
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        for (int rowIndex = 0; rowIndex < rows.length && hallSeats.size() < seatCount; rowIndex++) {
            String currentRow = rows[rowIndex];
            for (int seatNum = 1; seatNum <= seatsPerRow && hallSeats.size() < seatCount; seatNum++) {
                Seat seat = Seat.builder()
                        .row(currentRow)
                        .number(seatNum)
                        .type(SeatType.STANDARD)
                        .build();

                seat = seatRepository.save(seat);

                HallHasSeat hallSeat = HallHasSeat.builder()
                        .hall(hall)
                        .seat(seat)
                        .status(SeatStatus.AVAILABLE)
                        .build();

                hallSeats.add(hallSeat);
            }
        }

        return hallSeats;
    }
}
