package com.microservice.serviceB.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microservice.serviceB.enums.ServiceTime;
import com.microservice.serviceB.enums.ServiceType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookingModel {
    private UUID customerId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ServiceType serviceType;
    private String address;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate bookingDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ServiceTime serviceTime;
    private String instruction;
}
