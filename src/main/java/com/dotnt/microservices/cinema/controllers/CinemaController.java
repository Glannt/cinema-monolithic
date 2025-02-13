package com.dotnt.microservices.cinema.controllers;

import com.dotnt.microservices.cinema.dto.request.CinemaDTO;
import com.dotnt.microservices.cinema.dto.response.ApiResponse;
import com.dotnt.microservices.cinema.dto.response.CinemaResponse;
import com.dotnt.microservices.cinema.services.CinemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "CINEMA-CONTROLLER")
@RequestMapping("api")
public class CinemaController {
    private final CinemaService cinemaService;

    @PostMapping("test")
    public CinemaResponse testCreateCinema(@RequestBody CinemaDTO request) {
//        log.info("Call Create cinema API");
        var response = cinemaService.createCinema(request);
        return response;
    }

    @PostMapping("cinema")
    public ApiResponse<CinemaResponse> createCinema(@RequestBody CinemaDTO request) {
        log.info("Call Create cinema API");
        var response = cinemaService.createCinema(request);
        return ApiResponse.<CinemaResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Cinema is created")
                .data(response)
                .build();
    }

    @PutMapping("cinema")
    public ApiResponse<CinemaResponse> updateCinema(@RequestParam(required = true) UUID cinemaId, @RequestBody CinemaDTO request) {
        log.info("Call Update cinema API");
        var response = cinemaService.updateCinema(cinemaId, request);
        return ApiResponse.<CinemaResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update cinema Successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("cinema")
    public ApiResponse<CinemaResponse> deleteCinema(@RequestBody UUID cinemaId) {
        log.info("Call Delete cinema API");
        var response = cinemaService.deleteCinema(cinemaId);
        return ApiResponse.<CinemaResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Delete cinema Successfully")
                .data(response)
                .build();
    }

    @GetMapping("/cinema/{id}")
    public ApiResponse<CinemaResponse> getCinema(@PathVariable("id") UUID cinemaId){

        var response = cinemaService.getCinemaById(cinemaId);

        return ApiResponse
                .<CinemaResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get cinema by Id successfully")
                .data(response)
                .build();
    }

    @GetMapping("cinema")
    public ApiResponse<List<CinemaResponse>> getCinemas(){

        var response = cinemaService.getCinemas();

        return ApiResponse
                .<List<CinemaResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get cinema by Id successfully")
                .data(response)
                .build();
    }
}
