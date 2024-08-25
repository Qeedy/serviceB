package com.microservice.serviceB.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class TransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private UUID transactionId;
    @Column
    private UUID customerId;
    @Column
    private String customerName;
    @Column
    private String itemNames;
    @Column
    private BigDecimal customerChange;
    @Column
    private BigDecimal totalCost;
    @Column
    private BigDecimal customerOldBalance;
    @Column
    private BigDecimal customerNewBalance;
    @Column
    private LocalDate transactionDate;
    @Column
    private String categoryNames;
}
