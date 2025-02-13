package com.dotnt.microservices.cinema.repositories;

import com.dotnt.microservices.cinema.model.CustomerHasSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerHasSubscriptionRepository extends JpaRepository<CustomerHasSubscription, UUID> {
    List<CustomerHasSubscription> findByEndDateBeforeAndActivationStatus(LocalDateTime endDate, String activationStatus);
}
