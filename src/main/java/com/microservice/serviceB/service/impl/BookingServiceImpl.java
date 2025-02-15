package com.microservice.serviceB.service.impl;

import com.microservice.serviceB.entity.BookingDetail;
import com.microservice.serviceB.entity.BookingProcess;
import com.microservice.serviceB.entity.User;
import com.microservice.serviceB.enums.BookingStatus;
import com.microservice.serviceB.enums.ServiceTime;
import com.microservice.serviceB.mapper.BookingProcessMapper;
import com.microservice.serviceB.model.BookingDetailModel;
import com.microservice.serviceB.model.BookingListModel;
import com.microservice.serviceB.model.BookingTaskModel;
import com.microservice.serviceB.model.CreateBookingModel;
import com.microservice.serviceB.repository.BookingProcessRepository;
import com.microservice.serviceB.repository.UserRepository;
import com.microservice.serviceB.service.BookingService;
import com.microservice.serviceB.service.SequencesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingProcessRepository bookingProcessRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SequencesService sequenceService;

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

    @Override
    public BookingTaskModel getBookingTaskModel(String bookingNumber) {
        return constructBookingTaskModel(
                getBookingProcessByBookingNumber(bookingNumber));
    }

    private BookingDetailModel constructBookingDetailModel(BookingProcess data) {
        BookingDetailModel model = BookingDetailModel.builder()
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
                .build();
        if(data.getInvoice() != null){
            model.setPaymentMethod(data.getInvoice().getPaymentMethod());
            model.setTotalCost(data.getInvoice().getTotalCost());
        }
        return model;
    }

    private BookingListModel constructBookingListModel(BookingProcess data) {
        return BookingListModel.builder()
                .bookingDate(data.getBookingDate().toLocalDate())
                .technicianName(data.getBookingDetail().getTechnitioanName())
                .bookingId(data.getBookingNumber())
                .createdDate(data.getInsertedDate())
                .serviceTime(data.getServiceTime())
                .bookingStatus(data.getStatus())
                .customerName(data.getBookingDetail().getCustomerName())
                .serviceType(data.getServiceType())
                .build();
    }

    private BookingTaskModel constructBookingTaskModel(BookingProcess data) {
        return BookingTaskModel.builder()
                .bookingDate(data.getBookingDate().toLocalDate())
                .technicianName(data.getBookingDetail().getTechnitioanName())
                .bookingId(data.getBookingNumber())
                .createdDate(data.getInsertedDate())
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
    public Page<BookingListModel> getAllBookingProcessByUserId(UUID userId, String search, Pageable pageable) {
        return bookingProcessRepository
                .findAllByUserIdOrTechnicianId(search, userId, pageable)
                .map(this::constructBookingListModel);
    }

    @Override
    public Page<BookingListModel> getAllBookingProcess(String search, Pageable pageable) {
        return bookingProcessRepository.findAllByAdmin(search, pageable)
                .map(this::constructBookingListModel);
    }

    @Override
    @Transactional
    public BookingProcess createBookingProcess(CreateBookingModel model) throws Exception {
        User user = userRepository.findById(model.getCustomerId())
                .orElseThrow(()-> new Exception("Get User Failed"));
        BookingProcess bookingProcess = BookingProcess.builder()
                .status(BookingStatus.DRAFT)
                .bookingNumber(sequenceService.getSequenceNumber("BO"))
                .bookingDate(model.getBookingDate()
                        .atTime(model.getServiceTime().getMaxTime(), 0))
                .serviceTime(model.getServiceTime())
                .serviceType(model.getServiceType())
                .insertedDate(LocalDateTime.now())
                .build();
        BookingDetail bookingDetail = BookingDetail.builder()
                .bookingProcess(bookingProcess)
                .customerName(user.getFullName())
                .customerId(user.getUuid())
                .customerAddress(user.getAddress())
                .location(model.getAddress())
                .customerEmail(user.getEmail())
                .customerPhoneNumber(user.getPhoneNumber())
                .instructions(model.getInstruction())
                .build();
        bookingProcess.setBookingDetail(bookingDetail);
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

    @Override
    public BigDecimal getRevenue() {
        return bookingProcessRepository.sumRevenue();
    }

    @Override
    public Integer getTotalBooking() {
        return bookingProcessRepository.getTotalBooking();
    }

    @Override
    public Map<String, Object> getTransactionHistory() {
        LocalDate now = LocalDate.now();
        LocalDate sixMonthsAgo = now.minusMonths(5);
        List<Object[]> results = bookingProcessRepository
                .getLastSixMonthsRevenueAndBookings(sixMonthsAgo.atStartOfDay());
        Map<Integer, Double> revenueMap = new HashMap<>();
        Map<Integer, Integer> bookingMap = new HashMap<>();
        List<String> months = new ArrayList<>();
        List<Double> revenue = new ArrayList<>();
        List<Integer> bookings = new ArrayList<>();

        // Simpan data yang ada dari database ke Map
        for (Object[] row : results) {
            int monthIndex = (Integer) row[0]; // Bulan dalam format angka (1 = Januari, dst.)
            revenueMap.put(monthIndex, ((Number) row[1]).doubleValue());
            bookingMap.put(monthIndex, ((Number) row[2]).intValue());
        }

        // Loop untuk memastikan semua 6 bulan terakhir ada
        for (int i = 5; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            int monthValue = monthDate.getMonthValue();
            String monthName = monthDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

            months.add(monthName);
            revenue.add(revenueMap.getOrDefault(monthValue, 0.0)); // Jika tidak ada, set 0
            bookings.add(bookingMap.getOrDefault(monthValue, 0)); // Jika tidak ada, set 0
        }

        Map<String, Object> response = new HashMap<>();
        response.put("labels", months);
        response.put("revenue", revenue);
        response.put("bookings", bookings);

        return response;
    }
}
