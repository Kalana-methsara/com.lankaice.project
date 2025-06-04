package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeliveryDto {
    private int deliveryId;
    private int orderId;
    private String deliveryDate;
    private String deliveryTime;
    private String deliveryAddress;
    private String deliveryState;
    private int vehicleId;

}
