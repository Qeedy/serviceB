package com.microservice.serviceB.entity;

import com.microservice.serviceB.enums.ServiceTime;
import com.microservice.serviceB.enums.ServiceType;
import jakarta.persistence.*;
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
@Entity
@Table
public class BookingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @OneToOne
    @MapsId
    @JoinColumn(name = "uuid")
    private BookingProcess bookingProcess;
    @Column
    private String customerName;
    @Column
    private String customerAddress;
    @Column
    private String customerPhoneNumber;
    @Column
    private String customerEmail;
    @Column
    private UUID customerId;
    @Column
    private String location;
    @Column
    private UUID technicianId;
    @Column
    private String technitioanName;
    @Column
    private String serviceName;
    @Column
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    @Column
    @Enumerated(EnumType.STRING)
    private ServiceTime serviceTime;
    @Column
    private LocalDateTime bookingDate;
    @Column
    private String instructions;
    @Column
    private LocalDate repairCompletionDate;
}
