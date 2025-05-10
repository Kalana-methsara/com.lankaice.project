package com.lankaice.project.dto;

import lombok.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SalaryPaymentDto {
    private int salaryPaymentId;
    private int salaryId;
    private Date paymentDate;
    private double salaryAmount;
    private String status;
}
