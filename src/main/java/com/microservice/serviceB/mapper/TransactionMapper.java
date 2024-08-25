package com.microservice.serviceB.mapper;

import com.microservice.serviceB.entity.TransactionLog;
import com.microservice.serviceB.model.TransactionDataModel;
import com.microservice.serviceB.model.TransactionModel;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionMapper {

    public static TransactionLog constructTransactionLog(TransactionDataModel data) {
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setTransactionId(data.getTransactionId());
        transactionLog.setCustomerId(data.getCustomerId());
        transactionLog.setCustomerName(data.getCustomerName());
        transactionLog.setCustomerChange(data.getCustomerChange());
        transactionLog.setCustomerNewBalance(data.getCustomerNewBalance());
        transactionLog.setCustomerOldBalance(data.getCustomerOldBalance());
        transactionLog.setTotalCost(data.getTotalCost());
        transactionLog.setItemNames(String.join(",", data.getItemNames()));
        transactionLog.setCategoryNames(String.join(",", data.getCategoryNames()));
        transactionLog.setTransactionDate(LocalDate.now());
        return transactionLog;
    }


    public static TransactionDataModel constructTransactionDataModel(TransactionLog data) {
        Set<String> categories = Arrays.stream(data.getCategoryNames().split(","))
                .map(String::trim).collect(Collectors.toSet());
        return TransactionDataModel.builder().transactionId(data.getTransactionId())
                .customerId(data.getCustomerId())
                .customerName(data.getCustomerName())
                .customerChange(data.getCustomerChange())
                .customerNewBalance(data.getCustomerNewBalance())
                .customerOldBalance(data.getCustomerOldBalance())
                .totalCost(data.getTotalCost())
                .itemNames(List.of(data.getItemNames().split(",")))
                .categoryNames(categories).build();
    }

    public static TransactionModel constructTransactionModel(List<TransactionLog> datas) {
        return TransactionModel.builder()
                .datas(datas.stream()
                        .map(TransactionMapper::constructTransactionDataModel)
                        .toList())
                .build();
    }
}
