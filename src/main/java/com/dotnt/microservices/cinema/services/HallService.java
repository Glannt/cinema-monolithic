package com.dotnt.microservices.cinema.services;

import com.dotnt.microservices.cinema.dto.request.HallCreationRequest;
import com.dotnt.microservices.cinema.dto.response.HallCreationResponse;


public interface HallService {
    HallCreationResponse createHall(HallCreationRequest request);

    HallCreationResponse updateHall(HallCreationRequest request);

    HallCreationResponse deleteHall(HallCreationRequest request);
}
