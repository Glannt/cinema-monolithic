package com.dotnt.microservices.cinema.controllers;

import com.dotnt.microservices.cinema.dto.request.SignInRequest;
import com.dotnt.microservices.cinema.dto.request.UserCreationRequest;
import com.dotnt.microservices.cinema.dto.response.ApiResponse;
import com.dotnt.microservices.cinema.dto.response.AuthenticationResponse;
import com.dotnt.microservices.cinema.services.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/check")
    public String checkRunning() {
        return "Application is running";
    }

    @PostMapping("register")
    public ApiResponse<AuthenticationResponse> register(@RequestBody @Valid UserCreationRequest request) {
        var result = authenticationService.signup(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(result.getToken());
        return ApiResponse.<AuthenticationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("User created")
                .data(result)
                .build();

    }

    @PostMapping("login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody @Valid SignInRequest request, HttpServletResponse repsonse) {
        var result = authenticationService.login(request, repsonse);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(result.getToken());
        return ApiResponse.<AuthenticationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Login Successfully")
                .data(result)
                .build();

    }
}
