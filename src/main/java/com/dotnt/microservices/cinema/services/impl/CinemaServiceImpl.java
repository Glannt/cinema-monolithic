package com.dotnt.microservices.cinema.services.impl;

import com.dotnt.microservices.cinema.common.CinemaStatus;
import com.dotnt.microservices.cinema.dto.request.CinemaDTO;
import com.dotnt.microservices.cinema.dto.response.CinemaResponse;
import com.dotnt.microservices.cinema.exception.AppException;
import com.dotnt.microservices.cinema.exception.ErrorCode;
import com.dotnt.microservices.cinema.model.Address;
import com.dotnt.microservices.cinema.model.Cinema;
import com.dotnt.microservices.cinema.repositories.AddressRepository;
import com.dotnt.microservices.cinema.repositories.CinemaRepository;
import com.dotnt.microservices.cinema.services.CinemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CINEMA-SERIVCE")
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository cinemaRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public CinemaResponse createCinema(CinemaDTO request) {
        UUID addressId =
                addressRepository.findIdByProvinceCodeAndDistrictCodeAndWardCode(
                        String.valueOf(request.getAddress().getProvinceCode()),
                        String.valueOf(request.getAddress().getDistrictCode()),
                        String.valueOf(request.getAddress().getWardCode()));
        if (cinemaRepository.existsCinemaByAddressId(addressId)) {
            throw new AppException(ErrorCode.ID_EXISTED);
        }
        Address address = addressRepository.findAddressById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND));


        Cinema newCinema = Cinema
                .builder()
                .name(request.getName())
                .addressId(addressId)
                .status(CinemaStatus.valueOf(request.getStatus()))
                .halls(request.getHalls())
                .build();

        cinemaRepository.save(newCinema);

        return CinemaResponse.builder()
                .id(newCinema.getId())
                .name(newCinema.getName())
                .address(address)
                .status(String.valueOf(CinemaStatus.ACTIVE))
                .build();
    }

    @Override
    @Transactional
    public CinemaResponse updateCinema(UUID cinemaId, CinemaDTO request) {
        Cinema cinema = cinemaRepository.findCinemaById(cinemaId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        if(request.getName() != null){
            cinema.setName(request.getName());
        }

        if(request.getStatus() != null) {
            cinema.setStatus(CinemaStatus.valueOf(request.getStatus()));
        }
        Address address = new Address();
        if(request.getAddress() != null){
            UUID addressId =
                    addressRepository.findIdByProvinceCodeAndDistrictCodeAndWardCode(
                            String.valueOf(request.getAddress().getProvinceCode()),
                            String.valueOf(request.getAddress().getDistrictCode()),
                            String.valueOf(request.getAddress().getWardCode()));
            if (cinemaRepository.existsCinemaByAddressId(addressId)) {
                throw new AppException(ErrorCode.ID_EXISTED);
            }
            cinema.setAddressId(addressId);
            address = addressRepository.findAddressById(addressId)
                    .orElseThrow(() -> new AppException(ErrorCode.ID_EXISTED));
        }

        if (request.getHalls() != null){
            cinema.setHalls(request.getHalls());
        }
//        cinema.setUpdatedBy();
        cinema.setUpdatedAt(LocalDateTime.now());
        Cinema updatedCinema = cinemaRepository.save(cinema);


        return CinemaResponse
                .builder()
                .id(updatedCinema.getId())
                .name(updatedCinema.getName())
                .address(address)
                .halls(updatedCinema.getHalls())
                .status(String.valueOf(updatedCinema.getStatus()))
                .build();
    }


    @Override
    @Transactional
    public CinemaResponse deleteCinema(UUID cinemaId) {
        Cinema cinema = cinemaRepository.findCinemaById(cinemaId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

            cinema.setStatus(CinemaStatus.INACTIVE);

        Cinema deletedCinema = cinemaRepository.save(cinema);
        return CinemaResponse
                .builder()
                .id(deletedCinema.getId())
                .name(deletedCinema.getName())
                .status(String.valueOf(deletedCinema.getStatus()))
                .build();
    }

    @Override
    public CinemaResponse getCinemaById(UUID cinemaId) {
        Cinema cinema = cinemaRepository.findCinemaById(cinemaId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        Address cinemaAddress = addressRepository.findAddressById(cinema.getAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        return CinemaResponse
                .builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .status(String.valueOf(cinema.getStatus()))
                .address(cinemaAddress)
                .halls(cinema.getHalls())
                .build();
    }

    @Override
    public List<CinemaResponse> getCinemas() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        Set<UUID> addressIds = cinemas.stream()
                .map(Cinema::getAddressId)
                .collect(Collectors.toSet());
        Map<UUID, Address> addressMap = addressRepository.findAllByIdIn(addressIds)
                .stream()
                .collect(Collectors.toMap(Address::getId, address -> address));
        return cinemas.stream()
                .map(cinema -> CinemaResponse.builder()
                        .id(cinema.getId())
                        .name(cinema.getName())
                        .status(cinema.getStatus().toString())
                        .address(addressMap.get(cinema.getAddressId()))
                        .build())
                .collect(Collectors.toList());
    }
}
