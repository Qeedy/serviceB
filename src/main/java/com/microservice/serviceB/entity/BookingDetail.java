package com.microservice.serviceB.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bookingDetailId", referencedColumnName = "uuid")
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
    private String instructions;
    @Column
    private UUID technicianId;
    @Column
    private String technitioanName;
    @Column
    private LocalDate repairCompletionDate;

}
