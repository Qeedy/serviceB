package com.microservice.serviceB.service;

import com.microservice.serviceB.entity.BookingProcess;
import com.microservice.serviceB.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BookingService {
    public BookingDetailModel getBookingDetailModel(String bookingNumber);
    public List<BookingListModel> getBookingListByBookingNumber(List<String> bookingNumbers);
    public BookingTaskModel getBookingTaskModel(String bookingNumber);
    public Page<BookingListModel> getAllBookingProcessByUserId(UUID userId, String search, Pageable pageable);
    public Page<BookingListModel> getAllBookingProcess(String search, Pageable pageable);
    public BookingProcess createBookingProcess(CreateBookingModel model) throws Exception;
    public void updateBookingProcess(BookingProcess data) throws Exception;
    public BookingProcess getBookingProcessByBookingNumber(String bookingNumber);
    public BigDecimal getRevenue();
    public Integer getTotalBooking();
    public Map<String, Object> getTransactionHistory();
}
