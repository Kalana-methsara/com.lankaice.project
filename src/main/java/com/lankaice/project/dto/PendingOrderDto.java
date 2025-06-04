package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PendingOrderDto {
    private int pendingOrderId;
    private int orderId;
    private String customerName;
    private String productName;
    private int quantity;
    private String requestTime;
    private String status;

    public PendingOrderDto(int orderId, String customerName, String productName, int quantity,String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.status = status;
    }


}
