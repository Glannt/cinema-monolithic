package com.dotnt.microservices.cinema.services.impl;

import com.dotnt.microservices.cinema.common.UserStatus;
import com.dotnt.microservices.cinema.common.UserType;
import com.dotnt.microservices.cinema.dto.request.UserCreationRequest;
import com.dotnt.microservices.cinema.dto.response.SignupResponse;
import com.dotnt.microservices.cinema.exception.AppException;
import com.dotnt.microservices.cinema.exception.ErrorCode;
import com.dotnt.microservices.cinema.model.Role;
import com.dotnt.microservices.cinema.model.User;
import com.dotnt.microservices.cinema.model.UserHasRole;
import com.dotnt.microservices.cinema.repositories.RoleRepository;
import com.dotnt.microservices.cinema.repositories.UserRepository;
import com.dotnt.microservices.cinema.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SignupResponse createUser(UserCreationRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            log.info("User already existed ", request.getEmail());
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Role role = roleRepository.findByName(String.valueOf(UserType.USER))
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        User user = User
                .builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .status(UserStatus.ACTIVE)
                .build();
        user.setCreatedBy(request.getEmail());

        UserHasRole userHasRole = UserHasRole
                .builder()
                .role(role)
                .user(user)
                .build();
        user.setUserHasRoles(Set.of(userHasRole));
        return SignupResponse
                .builder()
                .fullName(String.format("%s %s", user.getFirstName(), user.getLastName()))
                .email(user.getEmail())
                .dob(user.getDob())
                .roles(user.getUserHasRoles().stream().map(UserHasRole::getRole).map(Role::getName).collect(Collectors.toSet()))
                .build();
    }
}
