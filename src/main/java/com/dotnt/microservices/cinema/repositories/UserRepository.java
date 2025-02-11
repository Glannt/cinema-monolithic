package com.dotnt.microservices.cinema.repositories;

import com.dotnt.microservices.cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findAllByEmail(String email);
    boolean existsByEmail(String email);
}
