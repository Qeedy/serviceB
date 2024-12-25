package com.microservice.serviceB.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microservice.serviceB.enums.BookingStatus;
import com.microservice.serviceB.enums.ServiceTime;
import com.microservice.serviceB.enums.ServiceType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingListModel {
    private String bookingId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BookingStatus bookingStatus;
    private LocalDate bookingDate;
    private String customerName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ServiceType serviceType;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ServiceTime serviceTime;
    private String technicianName;
}
