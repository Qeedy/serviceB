package com.microservice.serviceB.controller;

import com.microservice.serviceB.enums.RejectReasonEnum;
import com.microservice.serviceB.model.*;
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

    @GetMapping("/get-task-list")
    public ResponseEntity<Page<BookingTaskModel>> getTaskByUserId(
            @RequestParam("userId") UUID userId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "isAdmin", defaultValue = "false") Boolean isAdmin,
            Pageable pageable) {
        Page<BookingTaskModel> datas = isAdmin ?
                bookingTaskService.getAllTaskAdmin(keyword, pageable) :
                bookingTaskService.getAllTasksByTechnicianId(keyword, userId, pageable);
        return ResponseEntity.ok(datas);
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

    @GetMapping("/get-total-task")
    public ResponseEntity<Integer> getTotalTask() {
        return ResponseEntity.ok(bookingTaskService.getTotalTask());
    }

}
