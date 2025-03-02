package com.microservice.serviceB.entity;

import com.microservice.serviceB.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @Column
    private ServiceType serviceType;
    @Column
    private String serviceName;
    @Column
    private BigDecimal cost;

}
