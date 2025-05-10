package com.lankaice.project.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceDto {
    private int attendanceId;
    private String employeeId;
    private String name;
    private LocalDate date;
    private String shift;
    private String status;
    private LocalTime inTime;
    private LocalTime outTime;

    public AttendanceDto(String employeeId, LocalDate date, String shift, String status, LocalTime inTime, LocalTime outTime) {
        this.employeeId = employeeId;
        this.date = date;
        this.shift = shift;
        this.status = status;
        this.inTime = inTime;
        this.outTime = outTime;
    }

}
