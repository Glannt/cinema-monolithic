package com.dotnt.microservices.cinema.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
public class LoginResponse implements Serializable {
    private String refreshToken;
    private String accessToken;
    private UUID userId;
}
