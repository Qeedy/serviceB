package com.microservice.serviceB.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.serviceB.entity.TransactionLog;
import com.microservice.serviceB.mapper.TransactionMapper;
import com.microservice.serviceB.model.TransactionDataModel;
import com.microservice.serviceB.repository.TransactionLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TransactionConsumer {
    private static final String FILE_NAME = "transaction.xlsx";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @KafkaListener(topics = "transaction", groupId = "transaction")
    public void consume(String message) throws IOException {
        log.info("Received message: " + message);
        TransactionDataModel data = objectMapper.readValue(message, TransactionDataModel.class);
        saveToExcel(data);
        saveTransactionLog(data);
    }

    private void saveTransactionLog(TransactionDataModel data) {
        transactionLogRepository.save(
                TransactionMapper.constructTransactionLog(data));
    }

    private void saveToExcel(TransactionDataModel transaction) throws IOException {
        String filePath = "src/main/resources/data/transactions.xlsx";
        try (InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet("transaction");
            int lastRowNum = sheet.getLastRowNum();
            Row dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(transaction.getTransactionId().toString());
            dataRow.createCell(1).setCellValue(transaction.getCustomerId().toString());
            dataRow.createCell(2).setCellValue(transaction.getCustomerName());
            dataRow.createCell(3).setCellValue(String.join(", ", transaction.getItemNames())); // Asumsikan Anda ingin menampilkan item sebagai String
            dataRow.createCell(4).setCellValue(transaction.getCustomerChange().toPlainString());
            dataRow.createCell(5).setCellValue(transaction.getTotalCost().toPlainString());
            dataRow.createCell(6).setCellValue(transaction.getCustomerOldBalance().toPlainString()); // Old balance
            dataRow.createCell(7).setCellValue(transaction.getCustomerNewBalance().toPlainString()); // New balance
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
