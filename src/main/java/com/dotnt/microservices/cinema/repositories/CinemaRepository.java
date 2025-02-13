package com.dotnt.microservices.cinema.repositories;

import com.dotnt.microservices.cinema.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, UUID> {

    boolean existsCinemaByAddressId(UUID addressId);

    Optional<Cinema> findCinemaById(UUID cinemaId);
}
