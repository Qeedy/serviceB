package com.microservice.serviceB.controller;

import com.microservice.serviceB.model.BookingDetailModel;
import com.microservice.serviceB.model.BookingListModel;
import com.microservice.serviceB.model.BookingProcessModel;
import com.microservice.serviceB.model.CreateBookingModel;
import com.microservice.serviceB.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/detail/{bookingNumber}")
    public ResponseEntity<BookingDetailModel> getBookingProcess(
            @PathVariable String bookingNumber) {
        return ResponseEntity.ok(
                bookingService.getBookingDetailModel(bookingNumber));
    }

    @GetMapping("/get-booking-list")
    public ResponseEntity<Page<BookingListModel>> getAllBookingProcessByUserId(
            @RequestParam(required = false) UUID userId,
            @RequestParam(name = "isAdmin", defaultValue = "false") Boolean isAdmin,
            @RequestParam(defaultValue = "") String search,
            Pageable pageable) {
        return ResponseEntity.ok( !isAdmin ?
                bookingService.getAllBookingProcessByUserId(userId, search, pageable)
                : bookingService.getAllBookingProcess(search, pageable));
    }

    @GetMapping("/bookings-report-preview")
    public ResponseEntity<Page<BookingListModel>> getReportBookingsPreview(
            @RequestParam(required = false) String status,
            @RequestParam String dateRange,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            Pageable pageable) {
        return ResponseEntity.ok(bookingService.getReportBookingsPreview(
                status, dateRange, dateFrom, dateTo, pageable));
    }
    @GetMapping("/bookings-report")
    public ResponseEntity<List<BookingListModel>> getReportBookings(
            @RequestParam(required = false) String status,
            @RequestParam String dateRange,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo) {
        return ResponseEntity.ok(bookingService.getReportBookings(
                status, dateRange, dateFrom, dateTo));
    }

    @GetMapping("/get-revenue")
    public ResponseEntity<BigDecimal> getRevenueByMonth() {
        return ResponseEntity.ok(bookingService.getRevenue());
    }

    @GetMapping("/get-total-bookings")
    public ResponseEntity<Integer> getTotalBookings() {
        return ResponseEntity.ok(bookingService.getTotalBooking());
    }

    @GetMapping("/get-transaction-history")
    public ResponseEntity<Map<String, Object>> getTransactionHistory() {
        Map<String, Object> response = bookingService.getTransactionHistory();
        return ResponseEntity.ok(response);
    }

}
