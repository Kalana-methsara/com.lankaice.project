package com.lankaice.project.model;

import com.lankaice.project.db.DBConnection;
import com.lankaice.project.dto.SalaryDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.*;
import java.util.ArrayList;

public class SalaryModel {

    private String getEmployeeRole(String employeeId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT job_role FROM Employee WHERE employee_id = ?";
        ResultSet rs = CrudUtil.execute(sql, employeeId);
        return rs.next() ? rs.getString("job_role") : null;
    }

    private String getEmployeeName(String employeeId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT name FROM Employee WHERE employee_id = ?";
        ResultSet rs = CrudUtil.execute(sql, employeeId);
        return rs.next() ? rs.getString("name") : null;
    }

    private double calculateDeduction(String employeeId, int month, int year) throws SQLException, ClassNotFoundException {
        String sql = """
            SELECT COUNT(*) AS absent_days 
            FROM Attendance 
            WHERE employee_id = ? AND MONTH(date) = ? AND YEAR(date) = ? AND status = 'Absent'
        """;

        try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, employeeId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int absentDays = rs.getInt("absent_days");
                return absentDays * 100.0;
            }
        }
        return 0;
    }

    private double getWorkingHours(String employeeId, int month, int year) throws SQLException, ClassNotFoundException {
        String sql = """
            SELECT in_time, out_time 
            FROM Attendance 
            WHERE employee_id = ? AND MONTH(date) = ? AND YEAR(date) = ? AND status = 'Present'
        """;

        double totalHours = 0;

        try (PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, employeeId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Time in = rs.getTime("in_time");
                Time out = rs.getTime("out_time");
                if (in != null && out != null) {
                    long millis = out.getTime() - in.getTime();
                    totalHours += millis / (1000.0 * 60 * 60); // ms → hrs
                }
            }
        }

        return totalHours;
    }

    public ArrayList<SalaryDto> getAllSalaries() throws SQLException, ClassNotFoundException {
        String sql = """
            SELECT s.salary_id, s.employee_id, e.name, s.basic_amount, s.bonus, 
                   s.deduction, s.net_amount, s.pay_month, s.pay_year, s.status 
            FROM Salary s 
            INNER JOIN Employee e ON s.employee_id = e.employee_id
        """;

        ResultSet rs = CrudUtil.execute(sql);
        ArrayList<SalaryDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(convertToDto(rs));
        }
        return list;
    }

    private SalaryDto convertToDto(ResultSet rs) throws SQLException {
        return new SalaryDto(
                rs.getInt("salary_id"),
                rs.getString("employee_id"),
                rs.getString("name"),
                rs.getDouble("basic_amount"),
                rs.getDouble("bonus"),
                rs.getDouble("deduction"),
                rs.getDouble("net_amount"),
                rs.getInt("pay_month"),
                rs.getInt("pay_year"),
                rs.getString("status")
        );
    }

    public boolean updateSalaryStatus(int salaryId, String newStatus) {
        String sql = "UPDATE Salary SET status = ? WHERE salary_id = ?";
        try {
            return CrudUtil.execute(sql, newStatus, salaryId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSalary(String employeeId, int payMonth, int payYear) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Salary WHERE employee_id = ? AND pay_month = ? AND pay_year = ?";
        return CrudUtil.execute(sql, employeeId, payMonth, payYear);
    }

    public boolean updateSalary(SalaryDto dto) throws SQLException, ClassNotFoundException {
        String sql = """
            UPDATE Salary 
            SET basic_amount = ?, bonus = ?, deduction = ?, net_amount = ?, status = ? 
            WHERE employee_id = ? AND pay_month = ? AND pay_year = ?
        """;
        return CrudUtil.execute(sql,
                dto.getBasicSalary(),
                dto.getBonus(),
                dto.getDeduction(),
                dto.getNetSalary(),
                dto.getStatus(),
                dto.getEmployeeId(),
                dto.getPayMonth(),
                dto.getPayYear()
        );
    }

    public boolean generateMonthlySalaries(int month, int year) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT employee_id FROM Employee ORDER BY employee_id ASC");

        boolean atLeastOneGenerated = false;

        while (rs.next()) {
            String empId = rs.getString("employee_id");
            String role = getEmployeeRole(empId);
            if (role == null) continue;

            double hourlyRate;
            double bonus;

            switch (role) {
                case "Manager" -> { hourlyRate = 250; bonus = 5000; }
                case "Supervisor" -> { hourlyRate = 200; bonus = 3000; }
                case "Cashier" -> { hourlyRate = 180; bonus = 2000; }
                case "Driver" -> { hourlyRate = 175; bonus = 1500; }
                case "Worker" -> { hourlyRate = 150; bonus = 1000; }
                default -> {
                    continue;
                }
            }

            double totalHours = getWorkingHours(empId, month, year);
            double basicSalary = totalHours * hourlyRate;
            double deduction = calculateDeduction(empId, month, year);
            double netSalary = basicSalary + bonus - deduction;

            ResultSet checkRs = CrudUtil.execute(
                    "SELECT 1 FROM Salary WHERE employee_id = ? AND pay_month = ? AND pay_year = ?",
                    empId, month, year
            );

            if (!checkRs.next()) {
                String sql = "INSERT INTO Salary (employee_id, basic_amount, bonus, deduction, net_amount, pay_month, pay_year)VALUES (?, ?, ?,?,?, ?, ?)";
                CrudUtil.execute(sql, empId, basicSalary, bonus,deduction, netSalary,month,year);


                atLeastOneGenerated = true;
            }
        }

        return atLeastOneGenerated;
    }

    public boolean deleteAllSalary() throws SQLException, ClassNotFoundException {
        String sql ="DELETE FROM Salary";
       return CrudUtil.execute(sql);
    }
}
