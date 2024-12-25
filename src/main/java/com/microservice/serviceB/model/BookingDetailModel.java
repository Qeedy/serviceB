package com.microservice.serviceB.model;

import com.microservice.serviceB.enums.BookingStatus;
import com.microservice.serviceB.enums.ServiceType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDetailModel {
    private UUID uuid;
    private String bookingId;
    private ServiceType serviceType;
    private String bookingDateTime;
    private BookingStatus bookingStatus;
    private String technitionName;
    private String location;
    private String instructions;
    private String customerName;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerAddress;
    private String paymentMethod;
    private BigDecimal totalCost;
}
