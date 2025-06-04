package com.lankaice.project.model;

import com.lankaice.project.dto.SalaryDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SalaryModel {

    public ArrayList<SalaryDto> getAllSalarys() throws SQLException, ClassNotFoundException {
        String sql = "SELECT s.salary_id, s.employee_id, e.name, s.basic_amount, s.bonus, s.deduction, " +
                "s.net_amount, s.pay_month, s.pay_year, s.status " +
                "FROM Salary s INNER JOIN Employee e ON s.employee_id = e.employee_id";

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
        String sql = "UPDATE Salary SET basic_amount = ?, bonus = ?, deduction = ?, net_amount = ?, status = ? WHERE employee_id = ? AND pay_month = ? AND pay_year = ?";
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

}
