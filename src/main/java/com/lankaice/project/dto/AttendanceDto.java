package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceDto {
    private int attendanceId;
    private String employeeId;
    private String date;
    private String shift;
    private String status;

    public AttendanceDto(String employeeId, String date, String shift, String status) {
        this.employeeId = employeeId;
        this.date = date;
        this.shift = shift;
        this.status = status;
    }

}
