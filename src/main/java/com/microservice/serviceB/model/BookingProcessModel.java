package com.microservice.serviceB.model;

import com.microservice.serviceB.enums.BookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingProcessModel {
    private UUID bookingId;
    private String bookingNumber;
    private UUID userId;
    private UUID technicianId;
    private BookingStatus status;
    private LocalDate bookingDate;
    private LocalDateTime insertedBookingDate;
    private String damageReport;
    private String damageImage;
    private InvoiceModel invoiceModel;
}
