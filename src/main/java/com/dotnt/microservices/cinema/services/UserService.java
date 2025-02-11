package com.dotnt.microservices.cinema.services;

import com.dotnt.microservices.cinema.dto.request.UserCreationRequest;
import com.dotnt.microservices.cinema.dto.response.SignupResponse;

public interface UserService {
    SignupResponse createUser(UserCreationRequest request);
}
