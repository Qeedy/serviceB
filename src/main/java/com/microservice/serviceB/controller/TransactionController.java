package com.microservice.serviceB.controller;

import com.microservice.serviceB.model.TransactionModel;
import com.microservice.serviceB.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/get-transaction")
    public ResponseEntity<TransactionModel> getTransaction(
            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam String category) {
        return ResponseEntity.ok(transactionService.getTransaction(startDate, endDate, category));
    }

    @GetMapping("/send-email-transaction")
    public ResponseEntity<Void> sendEmail(@RequestParam String receiver) {
        transactionService.sendEmailTransaction(receiver);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
