package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VehicleDto {
    private int vehicleId;
    private String vehicleNumber;
    private String status;
    private int capacity;
    private String model;


}
