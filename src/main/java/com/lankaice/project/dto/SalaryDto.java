package com.lankaice.project.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SalaryDto {
    private int salaryId;
    private String employeeId;
    private String employeeName;
    private double basicSalary;
    private double bonus;
    private double deduction;
    private double netSalary;
    private int payMonth;
    private int payYear;
    private String status;


    public SalaryDto(String employeeId, Object employeeName, double basicSalary, double bonus, double deduction, double netSalary, int month, int year, String pending) {
        this.employeeId = employeeId;
        this.employeeName = employeeName.toString();
        this.basicSalary = basicSalary;
        this.bonus = bonus;
        this.deduction = deduction;
        this.netSalary = netSalary;
        this.payMonth = month;
        this.payYear = year;
        this.status = pending;
    }
}
