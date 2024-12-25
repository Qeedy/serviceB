package com.microservice.serviceB.repository;

import com.microservice.serviceB.entity.SeqNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeqRepository extends JpaRepository<SeqNumber, String> {
}
