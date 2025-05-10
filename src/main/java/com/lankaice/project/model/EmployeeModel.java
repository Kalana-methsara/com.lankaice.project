package com.lankaice.project.model;

import com.lankaice.project.dto.EmployeeDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {
    public boolean addEmployee(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO Employee (employee_id,name,nic,contact,email,job_role,address,join_date,date_of_birth,gender,bank_account_no,bank_branch,license_number) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return CrudUtil.execute(sql, employeeDto.getEmployeeId(), employeeDto.getName(), employeeDto.getNic(), employeeDto.getContact(), employeeDto.getEmail(), employeeDto.getJobRole(), employeeDto.getAddress(), employeeDto.getJoinDate(), employeeDto.getDateOfBirth(), employeeDto.getGender(), employeeDto.getBankAccountNo(), employeeDto.getBankBranch(), employeeDto.getLicenseNumber());
    }

    public boolean updateEmployee(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE Employee SET name = ?, nic = ? ,contact= ? ,email= ? ,job_role= ? ,address= ? ,join_date= ? ,date_of_birth = ?,gender = ?,bank_account_no= ? ,bank_branch= ? ,license_number= ? WHERE employee_id = ?";
        return CrudUtil.execute(sql, employeeDto.getName(), employeeDto.getNic(), employeeDto.getContact(), employeeDto.getEmail(), employeeDto.getJobRole(), employeeDto.getAddress(), employeeDto.getJoinDate(), employeeDto.getDateOfBirth(), employeeDto.getGender(), employeeDto.getBankAccountNo(), employeeDto.getBankBranch(), employeeDto.getLicenseNumber(), employeeDto.getEmployeeId());
    }

    public boolean deleteEmployee(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM Employee WHERE employee_id = ?";
        return CrudUtil.execute(sql, employeeDto.getEmployeeId());
    }

    public EmployeeDto searchbyId(String employeeId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Employee WHERE employee_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, employeeId);

        if (resultSet.next()) {
            return new EmployeeDto(
                    resultSet.getString("employee_id"),
                    resultSet.getString("name"),
                    resultSet.getString("nic"),
                    resultSet.getString("contact"),
                    resultSet.getString("email"),
                    resultSet.getString("job_role"),
                    resultSet.getString("address"),
                    resultSet.getString("join_date"),
                    resultSet.getString("date_of_birth"),
                    resultSet.getString("gender"),
                    resultSet.getString("bank_account_no"),
                    resultSet.getString("bank_branch"),
                    resultSet.getString("license_number")
            );
        }
        return null;
    }

    public ArrayList<EmployeeDto> searchEmployee(String employeeId, String name, String nic, String contact, String email, String jobRole, String address, String joinDate, String dateOfBirth, String gender, String bankAccountNo, String bankBranch, String licenseNumber) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Employee WHERE " +
                "employee_id LIKE ? OR name LIKE ? OR nic LIKE ? OR contact LIKE ? OR email LIKE ? OR " +
                "job_role LIKE ? OR address LIKE ? OR join_date LIKE ? OR date_of_birth LIKE ? OR " +
                "gender LIKE ? OR bank_account_no LIKE ? OR bank_branch LIKE ? OR license_number LIKE ?";

        ResultSet resultSet = CrudUtil.execute(sql,
                "%" + employeeId + "%", "%" + name + "%", "%" + nic + "%", "%" + contact + "%", "%" + email + "%",
                "%" + jobRole + "%", "%" + address + "%", "%" + joinDate + "%", "%" + dateOfBirth + "%",
                "%" + gender + "%", "%" + bankAccountNo + "%", "%" + bankBranch + "%", "%" + licenseNumber + "%");

        ArrayList<EmployeeDto> employeeDtos = new ArrayList<>();
        while (resultSet.next()) {
            employeeDtos.add(new EmployeeDto(
                    resultSet.getString("employee_id"),
                    resultSet.getString("name"),
                    resultSet.getString("nic"),
                    resultSet.getString("contact"),
                    resultSet.getString("email"),
                    resultSet.getString("job_role"),
                    resultSet.getString("address"),
                    resultSet.getString("join_date"),
                    resultSet.getString("date_of_birth"),
                    resultSet.getString("gender"),
                    resultSet.getString("bank_account_no"),
                    resultSet.getString("bank_branch"),
                    resultSet.getString("license_number")
            ));
        }
        return employeeDtos;
    }


    public ArrayList<EmployeeDto> viewAllEmployee() throws ClassNotFoundException, SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Employee");
        ArrayList<EmployeeDto> employeeDtos = new ArrayList<>();
        while (resultSet.next()) {
            employeeDtos.add(new EmployeeDto(
                    resultSet.getString("employee_id"),
                    resultSet.getString("name"),
                    resultSet.getString("nic"),
                    resultSet.getString("contact"),
                    resultSet.getString("email"),
                    resultSet.getString("job_role"),
                    resultSet.getString("address"),
                    resultSet.getString("join_date"),
                    resultSet.getString("date_of_birth"),
                    resultSet.getString("gender"),
                    resultSet.getString("bank_account_no"),
                    resultSet.getString("bank_branch"),
                    resultSet.getString("license_number")
            ));
        }
        return employeeDtos;
    }

    public int getEmployeeCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM Employee");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public ArrayList<EmployeeDto> searchByName(String name) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Employee WHERE name LIKE ?", "%" + name + "%");
        ArrayList<EmployeeDto> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new EmployeeDto(
                    resultSet.getString("employee_id"),
                    resultSet.getString("name"),
                    resultSet.getString("nic"),
                    resultSet.getString("contact"),
                    resultSet.getString("email"),
                    resultSet.getString("job_role"),
                    resultSet.getString("address"),
                    resultSet.getString("join_date"),
                    resultSet.getString("date_of_birth"),
                    resultSet.getString("gender"),
                    resultSet.getString("bank_account_no"),
                    resultSet.getString("bank_branch"),
                    resultSet.getString("license_number")
            ));
        }
        return list;
    }

    public String getLastEmployeeId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT MAX(employee_id) FROM Employee");
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public boolean isEmployeeExists(String employeeId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT employee_id FROM Employee WHERE employee_id = ?", employeeId);
        return rs.next();
    }

    public boolean isLicenseExists(String license) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT licenseNumber FROM Employee WHERE licenseNumber = ?", license);
        return rs.next();
    }

    public static int getEmployeeCountByRole(String role) throws Exception {
        String sql = "SELECT COUNT(*) FROM Employee WHERE job_role = ?";
        ResultSet result = CrudUtil.execute(sql, role);
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }


}
