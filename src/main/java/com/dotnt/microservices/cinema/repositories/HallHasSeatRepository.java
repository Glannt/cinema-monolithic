package com.dotnt.microservices.cinema.repositories;

import com.dotnt.microservices.cinema.model.HallHasSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HallHasSeatRepository extends JpaRepository<HallHasSeat, UUID> {
}
