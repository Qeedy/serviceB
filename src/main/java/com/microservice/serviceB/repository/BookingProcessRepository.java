package com.microservice.serviceB.repository;

import com.microservice.serviceB.entity.BookingProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingProcessRepository extends JpaRepository<BookingProcess, UUID> {
    BookingProcess findFirstByBookingNumber(String bookingNumber);

    @Query("SELECT b FROM BookingProcess b WHERE b.bookingDetail.customerId = :userId OR b.bookingDetail.technicianId = :userId")
    Page<BookingProcess> findAllByUserIdOrTechnicianId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT b FROM BookingProcess b")
    Page<BookingProcess> findAllByAdmin(Pageable pageable);

    List<BookingProcess> findByBookingNumberIn(List<String> bookingNumbers);
}
