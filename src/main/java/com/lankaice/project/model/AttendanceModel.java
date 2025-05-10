package com.lankaice.project.model;

import com.lankaice.project.dto.AttendanceDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceModel {

    public boolean markAttendance(AttendanceDto dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Attendance (employee_id, date, shift, status, in_time, out_time) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE status = VALUES(status), in_time = VALUES(in_time), out_time = VALUES(out_time)";

        return CrudUtil.<Boolean>execute(
                sql,
                dto.getEmployeeId(),
                Date.valueOf(dto.getDate()),
                dto.getShift(),
                dto.getStatus(),
                dto.getInTime() != null ? Time.valueOf(dto.getInTime()) : null,
                dto.getOutTime() != null ? Time.valueOf(dto.getOutTime()) : null
        );
    }
    public boolean isDuplicateAttendance(String empId, String date, String shift) throws SQLException, ClassNotFoundException {
        String checkSql = "SELECT COUNT(*) FROM Attendance WHERE employee_id = ? AND date = ? AND shift = ?";
        ResultSet resultSet = CrudUtil.execute(
                checkSql,
                empId,
                Date.valueOf(date),
                shift
        );

        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }


    public ArrayList<AttendanceDto> getAttendanceByDate(String dateString) throws SQLException, ClassNotFoundException {
        // Convert the String to a Date object
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


}
