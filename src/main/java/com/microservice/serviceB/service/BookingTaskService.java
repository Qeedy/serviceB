package com.microservice.serviceB.service;

import com.microservice.serviceB.enums.RejectReasonEnum;
import com.microservice.serviceB.model.BookingDetailModel;
import com.microservice.serviceB.model.BookingListModel;
import com.microservice.serviceB.model.BookingProcessModel;
import com.microservice.serviceB.model.CreateBookingModel;
import jakarta.transaction.Transactional;
import org.flowable.task.api.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BookingTaskService {
    public Page<BookingListModel> getAllTasksByTechnicianId(UUID technicianId, Pageable pageable);
    public Page<BookingListModel> getAllTaskAdmin(Pageable pageable);
    public BookingDetailModel getTaskByBookingNumber(String bookingNumber);
    public void processTask(String bookingNumber, Map<String, Object> variables);
    @Transactional
    public String createTask(CreateBookingModel model) throws Exception;
}
