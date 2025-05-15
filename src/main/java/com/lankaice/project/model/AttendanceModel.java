package com.lankaice.project.model;

import com.lankaice.project.dto.AttendanceDto;
import com.lankaice.project.util.CrudUtil;

<<<<<<< HEAD
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
=======
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805

public class AttendanceModel {

    public boolean markAttendance(AttendanceDto dto) throws SQLException, ClassNotFoundException {
<<<<<<< HEAD
        String sql = "INSERT INTO Attendance (employee_id, date, shift, status, in_time, out_time) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE status = VALUES(status), in_time = VALUES(in_time), out_time = VALUES(out_time)";
=======
        String sql = "INSERT INTO Attendance (employee_id, date, shift, status) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE status = VALUES(status)";
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805

        return CrudUtil.<Boolean>execute(
                sql,
                dto.getEmployeeId(),
                Date.valueOf(dto.getDate()),
                dto.getShift(),
<<<<<<< HEAD
                dto.getStatus(),
                dto.getInTime() != null ? Time.valueOf(dto.getInTime()) : null,
                dto.getOutTime() != null ? Time.valueOf(dto.getOutTime()) : null
=======
                dto.getStatus()
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        );
    }

    public boolean isDuplicateAttendance(String empId, String date, String shift) throws SQLException, ClassNotFoundException {
<<<<<<< HEAD
        String sql = "SELECT COUNT(*) FROM Attendance WHERE employee_id = ? AND date = ? AND shift = ?";
        ResultSet rs = CrudUtil.execute(
                sql,
=======
        String checkSql = "SELECT COUNT(*) FROM Attendance WHERE employee_id = ? AND date = ? AND shift = ?";
        ResultSet resultSet = CrudUtil.execute(
                checkSql,
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
                empId,
                Date.valueOf(date),
                shift
        );

<<<<<<< HEAD
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public AttendanceDto getAttendance(String empId, String date, String shift) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Attendance WHERE employee_id = ? AND date = ? AND shift = ?";
        ResultSet rs = CrudUtil.execute(
                sql,
                empId,
                Date.valueOf(date),
                shift
        );

        if (rs.next()) {
            return new AttendanceDto(
                    rs.getString("employee_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getString("shift"),
                    rs.getString("status"),
                    rs.getTime("in_time") != null ? rs.getTime("in_time").toLocalTime() : null,
                    rs.getTime("out_time") != null ? rs.getTime("out_time").toLocalTime() : null
            );
        }
        return null;
    }

    public boolean updateOutTime(String empId, String date, String shift, LocalTime outTime) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Attendance SET out_time = ? WHERE employee_id = ? AND date = ? AND shift = ?";
        return CrudUtil.<Boolean>execute(
                sql,
                Time.valueOf(outTime),
                empId,
                Date.valueOf(date),
                shift
        );
    }

    public ArrayList<AttendanceDto> getAttendanceByDate(String dateString) throws SQLException, ClassNotFoundException {
        Date date = Date.valueOf(dateString);
        String sql = "SELECT a.*, e.name FROM Attendance a " +
                "JOIN Employee e ON a.employee_id = e.employee_id " +
                "WHERE a.date = ?";

        ResultSet rs = CrudUtil.execute(sql, date);
        ArrayList<AttendanceDto> list = new ArrayList<>();

        while (rs.next()) {
            AttendanceDto dto = new AttendanceDto();
            dto.setEmployeeId(rs.getString("employee_id"));
            dto.setName(rs.getString("name"));
            dto.setShift(rs.getString("shift"));
            dto.setStatus(rs.getString("status"));
            dto.setInTime(rs.getTime("in_time") != null ? rs.getTime("in_time").toLocalTime() : null);
            dto.setOutTime(rs.getTime("out_time") != null ? rs.getTime("out_time").toLocalTime() : null);
            list.add(dto);
        }

        return list;
    }
=======
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
}
