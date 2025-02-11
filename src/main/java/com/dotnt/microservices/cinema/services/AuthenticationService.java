package com.dotnt.microservices.cinema.services;

import com.dotnt.microservices.cinema.dto.request.SignInRequest;
import com.dotnt.microservices.cinema.dto.request.UserCreationRequest;
import com.dotnt.microservices.cinema.dto.response.AuthenticationResponse;
import com.dotnt.microservices.cinema.dto.response.LoginResponse;
import com.dotnt.microservices.cinema.dto.response.SignupResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    AuthenticationResponse<SignupResponse> signup(UserCreationRequest request);

    AuthenticationResponse<LoginResponse> login(SignInRequest request, HttpServletResponse response);
}
