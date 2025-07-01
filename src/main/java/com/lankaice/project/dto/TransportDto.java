package com.lankaice.project.dto;

import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransportDto {
    private String transportId;
    private String productId;
    private String vehicleNumber;
    private String transportDate;
    private LocalTime deliveryBeginTime;
    private LocalTime deliveryEndTime;
    private int quantity;
    private String location;
    private String status;

}
