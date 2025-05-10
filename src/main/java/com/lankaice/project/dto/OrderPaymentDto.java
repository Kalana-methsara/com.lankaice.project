package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderPaymentDto {
    private int paymentId;
    private int orderId;
    private int billingId;
    private String paymentMethod;
    private double amount;
    private String date;
    private String status;
}
