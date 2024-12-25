package com.microservice.serviceB.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueOverview {
    private BigDecimal monthlyRevenue;
    private String monthName;
}
