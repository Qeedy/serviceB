package com.microservice.serviceB.repository;

import com.microservice.serviceB.entity.BookingProcess;
import com.microservice.serviceB.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingProcessRepository extends JpaRepository<BookingProcess, UUID> {
    BookingProcess findFirstByBookingNumber(String bookingNumber);

    @Query("SELECT b FROM BookingProcess b WHERE b.bookingDetail.customerId = :userId OR b.bookingDetail.technicianId = :userId and " +
            "(:search IS NULL OR :search = '' OR UPPER(b.bookingNumber) LIKE UPPER(CONCAT('%', :search, '%')))")
    Page<BookingProcess> findAllByUserIdOrTechnicianId(
            @Param("search") String search,
            @Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT b FROM BookingProcess b where " +
            "(:search IS NULL OR :search = '' OR UPPER(b.bookingNumber) LIKE UPPER(CONCAT('%', :search, '%')))")
    Page<BookingProcess> findAllByAdmin(
            @Param("search") String search, Pageable pageable);

    List<BookingProcess> findByBookingNumberIn(List<String> bookingNumbers);

    @Query("SELECT SUM(b.invoice.totalCost) FROM BookingProcess b WHERE b.status='COMPLETED'")
    BigDecimal sumRevenue();

    @Query("SELECT COUNT(b) FROM BookingProcess b")
    Integer getTotalBooking();

    @Query("SELECT MONTH(b.insertedDate) AS month, SUM(CASE WHEN b.status = 'COMPLETED' THEN b.invoice.totalCost ELSE 0 END) AS totalRevenue, COUNT(b) AS totalBookings " +
            "FROM BookingProcess b " +
            "WHERE b.insertedDate >= :sixMonthsAgo " +
            "GROUP BY MONTH(b.insertedDate) " +
            "ORDER BY MONTH(b.insertedDate)")
    List<Object[]> getLastSixMonthsRevenueAndBookings(@Param("sixMonthsAgo") LocalDateTime sixMonthsAgo);

    @Query("SELECT b FROM BookingProcess b " +
            "WHERE (:status IS NULL OR b.status = :status) " +
            "AND (cast(:dateFrom as date) IS NULL OR cast(:dateTo as date) IS NULL OR b.insertedDate BETWEEN :dateFrom AND :dateTo)")
    List<BookingProcess> getReportBookings(
            @Param("status") BookingStatus status,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo);

    @Query("SELECT b FROM BookingProcess b " +
            "WHERE (:status IS NULL OR b.status = :status) " +
            "AND (cast(:dateFrom as date) IS NULL OR cast(:dateTo as date) IS NULL OR b.insertedDate BETWEEN :dateFrom AND :dateTo)")
    Page<BookingProcess> getReportBookingsPreview(
            @Param("status") BookingStatus status,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            Pageable pageable);

}
