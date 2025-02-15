package com.microservice.serviceB.entity;

import com.microservice.serviceB.enums.InvoiceStatus;
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
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @OneToOne
    @MapsId
    @JoinColumn(name = "uuid")
    private BookingProcess bookingProcess;
    @Column
    private LocalDate invoiceDate;
    @Column
    private String invoiceNumber;
    @Column
    private BigDecimal totalCost;
    @Column
    private String paymentMethod;
}
