package com.lankaice.project.dto;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookingDto {
    private int bookingId;
    private String customerId;
    private String productId;
    private Date requestDate;
    private String requestTime;
    private int quantity;
    private String status;

    public BookingDto(String customerId, String productId, String selectedDate, String selectedTime, int qty, String status) {
        this.customerId = customerId;
        this.productId = productId;
        this.requestDate = Date.valueOf(selectedDate);
        this.requestTime = selectedTime;
        this.quantity = qty;
        this.status = status;
    }

}
