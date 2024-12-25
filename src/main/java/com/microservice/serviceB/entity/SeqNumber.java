package com.microservice.serviceB.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SeqNumber {
    @Id
    private String seqPrefix;
    @Column
    private int nextSeq;



}
