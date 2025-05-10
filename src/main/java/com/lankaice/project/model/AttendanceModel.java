package com.lankaice.project.model;

import com.lankaice.project.dto.AttendanceDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendanceModel {

    public boolean markAttendance(AttendanceDto dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Attendance (employee_id, date, shift, status) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE status = VALUES(status)";

        return CrudUtil.<Boolean>execute(
                sql,
                dto.getEmployeeId(),
                Date.valueOf(dto.getDate()),
                dto.getShift(),
                dto.getStatus()
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
}
