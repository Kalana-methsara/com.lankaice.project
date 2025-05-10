package com.lankaice.project.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SalaryDto {
    private int salaryId;
    private int employeeId;
    private double basicSalary;
    private double bonus;
    private double deduction;
    private double netSalary;
    private int payMonth;
    private int payYear;
}
