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

import java.util.List;
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
            @RequestParam UUID userId,
            @RequestParam(name = "isAdmin", defaultValue = "false") Boolean isAdmin,
            Pageable pageable) {
        return ResponseEntity.ok( isAdmin ?
                bookingService.getAllBookingProcessByUserId(userId, pageable)
                : bookingService.getAllBookingProcess(pageable));
    }
}
