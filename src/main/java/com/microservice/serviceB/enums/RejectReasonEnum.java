package com.microservice.serviceB.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum RejectReasonEnum {
    UNPAID("User not paid"),
    BOOKING_DATE_NOT_AVAILABLE("Booking date is not available"),
    CANCEL_BY_USER("Cancelled by user");

    public final String messageReason;

    public static RejectReasonEnum getByValue(String value) {
        for (RejectReasonEnum status : RejectReasonEnum.values()) {
            if (status.getMessageReason().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}



