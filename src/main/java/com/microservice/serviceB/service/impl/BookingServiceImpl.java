package com.microservice.serviceB.service.impl;

import com.microservice.serviceB.entity.BookingDetail;
import com.microservice.serviceB.entity.BookingProcess;
import com.microservice.serviceB.entity.Invoice;
import com.microservice.serviceB.entity.User;
import com.microservice.serviceB.enums.BookingStatus;
import com.microservice.serviceB.enums.InvoiceStatus;
import com.microservice.serviceB.enums.ServiceTime;
import com.microservice.serviceB.mapper.BookingProcessMapper;
import com.microservice.serviceB.model.*;
import com.microservice.serviceB.repository.BookingProcessRepository;
import com.microservice.serviceB.repository.UserRepository;
import com.microservice.serviceB.service.BookingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingProcessRepository bookingProcessRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public BookingDetailModel getBookingDetailModel(String bookingNumber) {
        return constructBookingDetailModel(
                getBookingProcessByBookingNumber(bookingNumber));
    }

    @Override
    public List<BookingListModel> getBookingListByBookingNumber(List<String> bookingNumbers) {
        return bookingProcessRepository.findByBookingNumberIn(bookingNumbers)
                .stream().map(BookingProcessMapper::constructBookingListModel)
                .collect(Collectors.toList());
    }

    private BookingDetailModel constructBookingDetailModel(BookingProcess data) {
        return BookingDetailModel.builder()
                .uuid(data.getUuid())
                .bookingId(data.getBookingNumber())
                .bookingStatus(data.getStatus())
                .bookingDateTime(constructBookingDateTime(
                        data.getBookingDate().toLocalDate(),
                        data.getServiceTime()))
                .serviceType(data.getServiceType())
                .location(data.getBookingDetail().getLocation())
                .technitionName(data.getBookingDetail().getTechnitioanName())
                .instructions(data.getBookingDetail().getInstructions())
                .customerName(data.getBookingDetail().getCustomerName())
                .customerEmail(data.getBookingDetail().getCustomerEmail())
                .customerPhoneNumber(data.getBookingDetail().getCustomerPhoneNumber())
                .customerAddress(data.getBookingDetail().getCustomerAddress())
                .paymentMethod(data.getInvoice().getPaymentMethod())
                .totalCost(data.getInvoice().getTotalCost())
                .build();
    }

    private BookingListModel constructBookingListModel(BookingProcess data) {
        return BookingListModel.builder()
                .bookingDate(data.getBookingDate().toLocalDate())
                .technicianName(data.getBookingDetail().getTechnitioanName())
                .bookingId(data.getBookingNumber())
                .serviceTime(data.getServiceTime())
                .bookingStatus(data.getStatus())
                .customerName(data.getBookingDetail().getCustomerName())
                .serviceType(data.getServiceType())
                .build();
    }

    private String constructBookingDateTime(LocalDate bookingDate, ServiceTime serviceTime) {
        return String.format("%s, %s",
                bookingDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                serviceTime.getTimeEstimation());
    }

    @Override
    public Page<BookingListModel> getAllBookingProcessByUserId(UUID userId, Pageable pageable) {
        return bookingProcessRepository
                .findAllByUserIdOrTechnicianId(userId, pageable)
                .map(this::constructBookingListModel);
    }

    @Override
    public Page<BookingListModel> getAllBookingProcess(Pageable pageable) {
        return bookingProcessRepository.findAllByAdmin(pageable)
                .map(this::constructBookingListModel);
    }

    @Override
    @Transactional
    public BookingProcess createBookingProcess(CreateBookingModel model) throws Exception {
        User user = userRepository.findById(model.getCustomerId())
                .orElseThrow(()-> new Exception("Get User Failed"));
        BookingProcess bookingProcess = BookingProcess.builder()
                .status(BookingStatus.DRAFT)
                .bookingDate(model.getBookingDate()
                        .atTime(model.getServiceTime().getMaxTime(), 0))
                .serviceTime(model.getServiceTime())
                .serviceType(model.getServiceType())
                .insertedDate(LocalDateTime.now())
                .bookingDetail(BookingDetail.builder()
                        .customerName(user.getFullName())
                        .customerAddress(user.getAddress())
                        .location(model.getAddress())
                        .customerEmail(user.getEmail())
                        .customerPhoneNumber(user.getPhoneNumber())
                        .instructions(model.getInstruction())
                        .build())
                .build();
        BookingProcess data = bookingProcessRepository.save(bookingProcess);
        return data;
    }

    @Override
    @Transactional
    public void updateBookingProcess(BookingProcess data) throws Exception {
        bookingProcessRepository.saveAndFlush(data);
    }

    @Override
    public BookingProcess getBookingProcessByBookingNumber(String bookingNumber) {
        return bookingProcessRepository.findFirstByBookingNumber(bookingNumber);
    }
}
