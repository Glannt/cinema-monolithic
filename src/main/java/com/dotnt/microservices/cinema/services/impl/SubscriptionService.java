package com.dotnt.microservices.cinema.services.impl;

import com.dotnt.microservices.cinema.model.CustomerHasSubscription;
import com.dotnt.microservices.cinema.repositories.CustomerHasSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SUBSCRIPTION-SERVICE")
public class SubscriptionService {

    private final CustomerHasSubscriptionRepository customerHasSubscriptionRepository;

    @Scheduled(cron = "0 0 0 * * *") // Chạy mỗi ngày lúc 00:00
    public void checkExpiredSubscriptions() {
        //Get today date
        LocalDateTime today = LocalDateTime.now();
        // Query để lấy các customer_subscription có end_date trước ngày hôm nay và status ACTIVE
        List<CustomerHasSubscription> expiredSubscriptions = customerHasSubscriptionRepository.findByEndDateBeforeAndActivationStatus(today, "ACTIVE");

        // Vô hiệu hóa các subscription đã hết hạn
        for (CustomerHasSubscription subscription : expiredSubscriptions) {
            subscription.setActivationStatus("INACTIVE");
            customerHasSubscriptionRepository.save(subscription);
        }
    }
}
