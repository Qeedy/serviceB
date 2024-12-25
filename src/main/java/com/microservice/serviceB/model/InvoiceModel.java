package com.microservice.serviceB.model;

import com.microservice.serviceB.enums.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceModel {
    private LocalDate invoiceDate;
    private InvoiceStatus status;
    private BigDecimal totalCost;
    private String invoiceAttachment;
}
