package com.microservice.serviceB.entity;

import com.microservice.serviceB.enums.BookingStatus;
import com.microservice.serviceB.enums.ServiceTime;
import com.microservice.serviceB.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;

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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @Column
    private String bookingNumber;
    @Column
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @Column
    private LocalDateTime bookingDate;
    @Column
    @Enumerated(EnumType.STRING)
    private ServiceTime serviceTime;
    @Column
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    @Column
    private LocalDateTime insertedDate;
    @OneToOne(mappedBy = "bookingProcess")
    private BookingDetail bookingDetail;
    @OneToOne(mappedBy = "bookingProcess")
    private Invoice invoice;
}
