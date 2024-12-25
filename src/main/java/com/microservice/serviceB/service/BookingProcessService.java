package com.microservice.serviceB.service;

import java.math.BigDecimal;

public interface BookingProcessService {
    public void processCustomerBooking(String bookingNumber) throws Exception;
    public void cancelCustomerBooking(String bookingNumber) throws Exception;
    public void processBookingApproval(String bookingNumber, String technicianId) throws Exception;
    public void rejectBookingApproval(String bookingNumber) throws Exception;
    public void processBookingCompletion(String bookingNumber) throws Exception;
    public void processCreateInvoice(String bookingNumber, BigDecimal totalCost, String paymentMethod) throws Exception;
}
