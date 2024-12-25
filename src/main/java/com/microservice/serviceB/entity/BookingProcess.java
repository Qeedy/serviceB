package com.microservice.serviceB.entity;

import com.microservice.serviceB.enums.BookingStatus;
import com.microservice.serviceB.enums.ServiceTime;
import com.microservice.serviceB.enums.ServiceType;
import com.microservice.serviceB.service.SequenceService;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class BookingProcess {

    @Autowired
    SequenceService sequenceService;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @Column
    private String bookingNumber;
    @Column
    private BookingStatus status;
    @Column
    private LocalDateTime bookingDate;
    @Column
    private ServiceTime serviceTime;
    @Column
    private ServiceType serviceType;
    @Column
    private LocalDateTime insertedDate;
    @OneToOne(mappedBy = "bookingProcess")
    private BookingDetail bookingDetail;
    @OneToOne(mappedBy = "bookingProcess")
    private Invoice invoice;

    @PrePersist
    public void generateBookingNumber() {
        this.bookingNumber = sequenceService.getSequenceNumber("BO");
    }
}
