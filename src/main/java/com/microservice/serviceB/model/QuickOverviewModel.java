package com.microservice.serviceB.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuickOverviewModel {
    private BigDecimal totalRevenue;
    private Integer totalBookings;
    private Integer totalTasks;
    private List<RevenueOverview> revenueOverviews;
    private List<BookingOverview> bookingOverviews;
}
