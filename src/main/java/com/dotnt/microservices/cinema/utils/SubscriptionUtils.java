package com.dotnt.microservices.cinema.utils;

import java.time.LocalDateTime;

public class SubscriptionUtils {
    public static boolean isSubscriptionValid(LocalDateTime endTime) {
        if (endTime == null) {
            return false;
        }
        LocalDateTime today = LocalDateTime.now();
        return !today.isAfter(endTime);
    }
}
