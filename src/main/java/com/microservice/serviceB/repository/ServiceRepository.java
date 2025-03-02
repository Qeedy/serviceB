package com.microservice.serviceB.repository;

import com.microservice.serviceB.entity.BookingProcess;
import com.microservice.serviceB.entity.Service;
import com.microservice.serviceB.enums.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<Service, UUID> {
    @Query("SELECT s FROM Service s WHERE " +
            "(:search IS NULL OR :search = '' OR UPPER(s.serviceName) LIKE UPPER(CONCAT('%', :search, '%')))")
    Page<Service> findAllByKeywordOptional(
            @Param("search") String search, Pageable pageable);

    @Query("FROM Service s WHERE s.serviceType=:serviceType")
    List<Service> findAllByType(@Param("serviceType") ServiceType serviceType);
}
