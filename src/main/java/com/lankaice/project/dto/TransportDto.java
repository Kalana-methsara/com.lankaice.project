package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransportDto {
    private int transportId;
    private int productId;
    private int vehicleId;
    private int driverId;
    private String transportDate;
    private String deliveryBeginTime;
    private String deliveryEndTime;
    private int quantity;
    private String fromLocation;
    private String toLocation;
    private String status;
}
