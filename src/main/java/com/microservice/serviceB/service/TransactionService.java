package com.microservice.serviceB.service;

import com.microservice.serviceB.model.TransactionModel;

import java.time.LocalDate;

public interface TransactionService {
    TransactionModel getTransaction(LocalDate startDate, LocalDate endDate, String itemCategory);
    void sendEmailTransaction(String emailReceiver);
}
