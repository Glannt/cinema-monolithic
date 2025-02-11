package com.dotnt.microservices.cinema.services.impl;

import com.dotnt.microservices.cinema.common.UserStatus;
import com.dotnt.microservices.cinema.common.UserType;
import com.dotnt.microservices.cinema.configuration.JwtTokenProvider;
import com.dotnt.microservices.cinema.dto.request.SignInRequest;
import com.dotnt.microservices.cinema.dto.request.UserCreationRequest;
import com.dotnt.microservices.cinema.dto.response.ApiResponse;
import com.dotnt.microservices.cinema.dto.response.AuthenticationResponse;
import com.dotnt.microservices.cinema.dto.response.LoginResponse;
import com.dotnt.microservices.cinema.dto.response.SignupResponse;
import com.dotnt.microservices.cinema.exception.AppException;
import com.dotnt.microservices.cinema.exception.ErrorCode;
import com.dotnt.microservices.cinema.model.Address;
import com.dotnt.microservices.cinema.model.Role;
import com.dotnt.microservices.cinema.model.User;
import com.dotnt.microservices.cinema.model.UserHasRole;
import com.dotnt.microservices.cinema.repositories.AddressRepository;
import com.dotnt.microservices.cinema.repositories.RoleRepository;
import com.dotnt.microservices.cinema.repositories.UserRepository;
import com.dotnt.microservices.cinema.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Authentication-service")
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AddressRepository addressRepository;
    private final AuthenticationManager authenticationManager;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthenticationResponse<SignupResponse> signup(UserCreationRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            log.info("User already existed ", request.getEmail());
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Role role = roleRepository.findByName(String.valueOf(UserType.USER))
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
//        String codeAddress = request.getAddress().getProvinceCode() ? request.getAddress().getDistrictCode() ? request.getAddress().getWardCode() ;
        User user = User
                .builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .status(UserStatus.ACTIVE)
                .addressId(addressRepository
                        .findIdByProvinceCodeAndDistrictCodeAndWardCode(
                                 String.valueOf(request.getAddress().getProvinceCode())
                                ,String.valueOf(request.getAddress().getDistrictCode())
                                ,String.valueOf(request.getAddress().getWardCode())))
                .phoneNumber(request.getPhoneNumber())
                .build();
        user.setCreatedBy(request.getEmail());

        UserHasRole userHasRole = UserHasRole
                .builder()
                .role(role)
                .user(user)
                .build();
        user.setUserHasRoles(Set.of(userHasRole));

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        return AuthenticationResponse.<SignupResponse>builder()
                .token(accessToken)
                .object(SignupResponse
                        .builder()
                        .fullName(String.format("%s %s", user.getFirstName(), user.getLastName()))
                        .email(user.getEmail())
                        .dob(user.getDob())
                        .roles(user.getUserHasRoles().stream().map(UserHasRole::getRole).map(Role::getName).collect(Collectors.toSet()))
                        .build())
                .build();
    }

    @Override
    public AuthenticationResponse<LoginResponse> login(SignInRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = (User) authentication.getPrincipal();
        log.info("Authority: {}", user.getAuthorities());
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true nếu chỉ cho gửi qua HTTPS
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(14 * 24 * 60 * 60); // 2 tuần

        response.addCookie(cookie);

        return AuthenticationResponse.<LoginResponse>builder()
                .token(accessToken)
                .object(LoginResponse
                        .builder()
                                .refreshToken(refreshToken)
                                .accessToken(accessToken)
                                .userId(user.getId())
//                        .roles(user.getUserHasRoles().stream().map(UserHasRole::getRole).map(Role::getName).collect(Collectors.toSet()))
                        .build())
                .build();
    }


}
