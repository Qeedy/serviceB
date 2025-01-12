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
    private UUID invoice;
    @Column
    private LocalDate invoiceDate;
    @Column
    private String invoiceNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoiceId", referencedColumnName = "uuid")
    private BookingProcess bookingProcess;
    @Column
    private BigDecimal totalCost;
    @Column
    private String paymentMethod;
}
