package com.microservice.serviceB.mapper;

import com.microservice.serviceB.entity.BookingProcess;
import com.microservice.serviceB.model.BookingListModel;

public class BookingProcessMapper {
    public static BookingListModel constructBookingListModel(BookingProcess data) {
        return BookingListModel.builder().build();
    }
}
