package com.microservice.serviceB.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingOverview {
    private Integer totalBookings;
    private String monthName;
}
