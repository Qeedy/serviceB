package com.microservice.serviceB.repository;

import com.microservice.serviceB.entity.TransactionLog;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {
    @Query("FROM TransactionLog tl where tl.categoryNames LIKE %:categoryName% AND tl.transactionDate BETWEEN :startDate AND :endDate")
    List<TransactionLog> findAllTransactionLog(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("categoryName") String categoryName);
}
