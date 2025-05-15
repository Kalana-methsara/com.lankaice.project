package com.lankaice.project.dto;

import lombok.*;

<<<<<<< HEAD
import java.time.LocalDate;
import java.time.LocalTime;

=======
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttendanceDto {
    private int attendanceId;
    private String employeeId;
<<<<<<< HEAD
    private String name;
    private LocalDate date;
    private String shift;
    private String status;
    private LocalTime inTime;
    private LocalTime outTime;

    public AttendanceDto(String employeeId, LocalDate date, String shift, String status, LocalTime inTime, LocalTime outTime) {
=======
    private String date;
    private String shift;
    private String status;

    public AttendanceDto(String employeeId, String date, String shift, String status) {
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        this.employeeId = employeeId;
        this.date = date;
        this.shift = shift;
        this.status = status;
<<<<<<< HEAD
        this.inTime = inTime;
        this.outTime = outTime;
=======
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
    }

}
