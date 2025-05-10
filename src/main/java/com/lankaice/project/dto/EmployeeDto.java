package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDto {
    private String employeeId;
    private String name;
    private String nic;
    private String contact;
    private String email;
    private String jobRole;
    private String address;
    private String joinDate;
    private String dateOfBirth;
    private String gender;
    private String bankAccountNo;
    private String bankBranch;
    private String licenseNumber;

    public EmployeeDto(String id) {
        this.employeeId = id;
    }
}
