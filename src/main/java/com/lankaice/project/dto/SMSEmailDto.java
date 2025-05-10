package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SMSEmailDto {
    private int notificationId;
    private int customerId;
    private int orderId;
    private String type;
    private String date;
}
