package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PendingOrderDto {
    private int orderId;
    private String customerName;
    private String productName;
    private int quantity;
    private String requestTime;
}
