package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderPaymentDto {
    private String paymentId;
    private int orderId;
    private String paymentMethod;
    private int itemCount;
    private double subtotal;
    private double discount;
    private double netTotal;
    private String paymentDate;
    private String status;


}
