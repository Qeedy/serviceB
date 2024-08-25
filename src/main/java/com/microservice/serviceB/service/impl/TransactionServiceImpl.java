package com.microservice.serviceB.service.impl;

import com.microservice.serviceB.entity.TransactionLog;
import com.microservice.serviceB.mapper.TransactionMapper;
import com.microservice.serviceB.model.TransactionModel;
import com.microservice.serviceB.repository.TransactionLogRepository;
import com.microservice.serviceB.service.TransactionService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Override
    public TransactionModel getTransaction(LocalDate startDate, LocalDate endDate, String itemCategory) {
        List<TransactionLog> transactionLogs = transactionLogRepository.findAllTransactionLog(startDate, endDate, itemCategory);
        return TransactionMapper.constructTransactionModel(transactionLogs);
    }

    @Override
    public void sendEmailTransaction(String emailReceiver) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(emailReceiver);
            helper.setSubject("Transaction");
            helper.setText("Transaction");
            Path path = Paths.get("src/main/resources/data/transactions.xlsx");
            helper.addAttachment(path.getFileName().toString(), new File(path.toString()));
            mailSender.send(mimeMessage);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
