package com.microservice.serviceB.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "seq_number")
public class SeqNumber {
    @Id
    private String seqPrefix;
    @Column
    private int nextSeq;
}
