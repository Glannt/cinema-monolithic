package com.dotnt.microservices.cinema.services;

import com.dotnt.microservices.cinema.dto.request.CinemaDTO;
import com.dotnt.microservices.cinema.dto.response.CinemaResponse;

import java.util.List;
import java.util.UUID;

public interface CinemaService {
    CinemaResponse createCinema(CinemaDTO request);

    CinemaResponse updateCinema(UUID cinemaId, CinemaDTO request);

    CinemaResponse deleteCinema(UUID cinemaId);

    CinemaResponse getCinemaById(UUID cinemaId);

    List<CinemaResponse> getCinemas();


}
