package com.microservice.serviceB.controller;

import com.microservice.serviceB.enums.RejectReasonEnum;
import com.microservice.serviceB.model.BookingDetailModel;
import com.microservice.serviceB.model.BookingListModel;
import com.microservice.serviceB.model.BookingProcessModel;
import com.microservice.serviceB.model.CreateBookingModel;
import com.microservice.serviceB.service.BookingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    BookingTaskService bookingTaskService;

    @GetMapping("/{userId}")
    public ResponseEntity<Page<BookingListModel>> getTaskByUserId(
            @PathVariable("userId") UUID userId,
            @RequestParam(name = "isAdmin", defaultValue = "false") Boolean isAdmin,
            Pageable pageable) {
        Page<BookingListModel> datas = isAdmin ?
                bookingTaskService.getAllTaskAdmin(pageable) :
                bookingTaskService.getAllTasksByTechnicianId(userId, pageable);
        return ResponseEntity.ok(datas);
    }

    @GetMapping("/detail/{bookingNumber}")
    public ResponseEntity<BookingDetailModel> getTaskDetail(
            @PathVariable("bookingNumber") String bookingNumber) {
        return ResponseEntity.ok(bookingTaskService
                .getTaskByBookingNumber(bookingNumber));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody CreateBookingModel model) throws Exception {
        return ResponseEntity.ok(
                bookingTaskService.createTask(model));
    }

    @PostMapping("/process-task/{bookingNumber}")
    public ResponseEntity<Void> approveTask(
            @PathVariable("bookingNumber") String bookingNumber,
            @RequestBody Map<String, Object> variables) {
        bookingTaskService.processTask(bookingNumber, variables);
        return ResponseEntity.ok().build();
    }

}
