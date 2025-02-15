package com.microservice.serviceB.service.impl;

import com.microservice.serviceB.entity.BookingDetail;
import com.microservice.serviceB.entity.BookingProcess;
import com.microservice.serviceB.entity.Invoice;
import com.microservice.serviceB.entity.User;
import com.microservice.serviceB.enums.BookingStatus;
import com.microservice.serviceB.repository.UserRepository;
import com.microservice.serviceB.service.BookingProcessService;
import com.microservice.serviceB.service.BookingService;
import com.microservice.serviceB.service.SequencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service("bookingProcessService")
public class BookingProcessServiceImpl implements BookingProcessService {

    @Autowired
    BookingService bookingService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    SequencesService sequenceService;


    @Override
    public void processCustomerBooking(String bookingNumber) throws Exception {
        BookingProcess data = bookingService.getBookingProcessByBookingNumber(bookingNumber);
        data.setStatus(BookingStatus.PENDING);
        bookingService.updateBookingProcess(data);
    }

    @Override
    public void cancelCustomerBooking(String bookingNumber) throws Exception {
        BookingProcess data = bookingService.getBookingProcessByBookingNumber(bookingNumber);
        data.setStatus(BookingStatus.CANCELLED);
        bookingService.updateBookingProcess(data);
    }

    @Override
    public void processBookingApproval(String bookingNumber, String technicianId) throws Exception {
        BookingProcess data = bookingService.getBookingProcessByBookingNumber(bookingNumber);
        User technician = Optional.of(userRepository
                .findById(UUID.fromString(technicianId)))
                .orElseThrow(()-> new Exception("Technician Not Found"))
                .get();
        BookingDetail bookingDetail = data.getBookingDetail();
        bookingDetail.setTechnicianId(technician.getUuid());
        bookingDetail.setTechnitioanName(technician.getFullName());
        data.setBookingDetail(bookingDetail);
        data.setStatus(BookingStatus.CONFIRMED);
        bookingService.updateBookingProcess(data);
    }

    @Override
    public void rejectBookingApproval(String bookingNumber) throws Exception {
        BookingProcess data = bookingService.getBookingProcessByBookingNumber(bookingNumber);
        data.setStatus(BookingStatus.CANCELLED);
        bookingService.updateBookingProcess(data);
    }

    @Override
    public void processBookingCompletion(String bookingNumber) throws Exception {
        BookingProcess data = bookingService.getBookingProcessByBookingNumber(bookingNumber);
        data.setStatus(BookingStatus.PROCESSED);
        bookingService.updateBookingProcess(data);
    }

    @Override
    public void processCreateInvoice(String bookingNumber, BigDecimal totalCost, String paymentMethod) throws Exception {
        BookingProcess data = bookingService.getBookingProcessByBookingNumber(bookingNumber);
        data.setStatus(BookingStatus.COMPLETED);
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(sequenceService.getSequenceNumber("INV"));
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setPaymentMethod(paymentMethod);
        invoice.setTotalCost(totalCost);
        invoice.setBookingProcess(data);
        data.setInvoice(invoice);
        bookingService.updateBookingProcess(data);
    }
}
