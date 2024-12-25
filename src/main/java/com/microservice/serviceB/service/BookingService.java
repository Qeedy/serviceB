package com.microservice.serviceB.service;

import com.microservice.serviceB.entity.BookingProcess;
import com.microservice.serviceB.model.BookingDetailModel;
import com.microservice.serviceB.model.BookingListModel;
import com.microservice.serviceB.model.BookingProcessModel;
import com.microservice.serviceB.model.CreateBookingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    public BookingDetailModel getBookingDetailModel(String bookingNumber);
    public List<BookingListModel> getBookingListByBookingNumber(List<String> bookingNumbers);
    public Page<BookingListModel> getAllBookingProcessByUserId(UUID userId, Pageable pageable);
    public Page<BookingListModel> getAllBookingProcess(Pageable pageable);
    public BookingProcess createBookingProcess(CreateBookingModel model) throws Exception;
    public void updateBookingProcess(BookingProcess data) throws Exception;
    public BookingProcess getBookingProcessByBookingNumber(String bookingNumber);
}
