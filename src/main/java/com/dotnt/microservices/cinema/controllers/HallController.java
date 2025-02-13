package com.dotnt.microservices.cinema.controllers;

import com.dotnt.microservices.cinema.dto.request.HallCreationRequest;
import com.dotnt.microservices.cinema.dto.response.ApiResponse;
import com.dotnt.microservices.cinema.dto.response.HallCreationResponse;
import com.dotnt.microservices.cinema.services.HallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/hall")
@RequiredArgsConstructor
@Slf4j(topic = "HALL-SERVICE")
public class HallController {
    private final HallService hallService;

    @PostMapping
    public ApiResponse<HallCreationResponse> createHall(@RequestBody HallCreationRequest request) {
        var response = hallService.createHall(request);
        return ApiResponse.<HallCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Hall is created")
                .data(response)
                .build();
    }
}
