package com.dotnt.microservices.cinema.common;

public enum SubscriptionPack {
    MEMBER,   // Gói cơ bản, ít ưu đãi
    VIP,      // Gói nâng cao, có nhiều quyền lợi hơn
    PREMIUM,  // Gói cao cấp với nhiều ưu đãi hơn VIP
    PLATINUM, // Gói đặc biệt với quyền lợi tối đa
    TRIAL,    // Gói dùng thử miễn phí trong thời gian giới hạn
    FAMILY,   // Gói cho nhóm người dùng (gia đình, doanh nghiệp)
    STUDENT,  // Gói dành cho học sinh, sinh viên (có thể giảm giá)
    BUSINESS  // Gói dành cho doanh nghiệp với nhiều quyền lợi hơn
}
