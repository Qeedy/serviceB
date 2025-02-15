package com.microservice.serviceB.service;

import com.microservice.serviceB.model.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface BookingTaskService {
    public PageImpl<BookingTaskModel> getAllTasksByTechnicianId(String keyword, UUID technicianId, Pageable pageable);
    public PageImpl<BookingTaskModel> getAllTaskAdmin(String keyword, Pageable pageable);
    public void processTask(String bookingNumber, Map<String, Object> variables);
    @Transactional
    public String createTask(CreateBookingModel model) throws Exception;
    public Integer getTotalTask();
}
