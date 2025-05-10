package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BillingDto {
    private int billingId;
    private int orderId;
    private String billingDate;
    private String paymentStatus;
    private double amount;
}
