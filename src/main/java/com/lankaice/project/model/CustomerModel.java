package com.lankaice.project.model;

import com.lankaice.project.dto.CustomerDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public boolean addCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Customer (customer_id, name, nic, email, contact, address, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                dto.getCustomerId(),
                dto.getName(),
                dto.getNic(),
                dto.getEmail(),
                dto.getContact(),
                dto.getAddress(),
                dto.getDescription()
        );
    }

    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Customer SET name=?, nic=?, email=?, contact=?, address=?, description=? WHERE customer_id=?";
        return CrudUtil.execute(sql,
                dto.getName(),
                dto.getNic(),
                dto.getEmail(),
                dto.getContact(),
                dto.getAddress(),
                dto.getDescription(),
                dto.getCustomerId()
        );
    }

    public boolean deleteCustomer(String customerId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Customer WHERE customer_id=?";
        return CrudUtil.execute(sql, customerId);
    }

    public ArrayList<CustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Customer");
        ArrayList<CustomerDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(convertToDto(rs));
        }
        return list;
    }
    public static List<CustomerDto> getAllCustomer() throws Exception {
        ResultSet rs = CrudUtil.execute("SELECT customer_id, name FROM Customer");

        List<CustomerDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new CustomerDto(rs.getString("customer_id"), rs.getString("name")));
        }
        return list;
    }

    public ArrayList<CustomerDto> searchById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE customer_id LIKE ?";
        ResultSet rs = CrudUtil.execute(sql, "%" + id + "%");
        ArrayList<CustomerDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(convertToDto(rs));
        }
        return list;
    }

    public ArrayList<CustomerDto> searchByName(String name) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE name LIKE ?";
        ResultSet rs = CrudUtil.execute(sql, "%" + name + "%");
        ArrayList<CustomerDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(convertToDto(rs));
        }
        return list;
    }

    private CustomerDto convertToDto(ResultSet rs) throws SQLException {
        return new CustomerDto(
                rs.getString("customer_id"),
                rs.getString("name"),
                rs.getString("nic"),
                rs.getString("email"),
                rs.getString("contact"),
                rs.getString("address"),
                rs.getString("description")
        );
    }

    public String findNameById(String customerId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT name FROM Customer WHERE customer_id=?",
                customerId
        );
        if (rst.next()) {
            return rst.getString("name");
        }
        return "";

    }
    public String findIdByName(String customerName) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT customer_id FROM Customer WHERE name=?",customerName
        );
        if (rst.next()) {
            return rst.getString("customer_id");
        }
        return "";
    }
    public int getCustomerCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM Customer");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }
    public String getLastCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT MAX(customer_id) FROM Customer");
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public ArrayList<CustomerDto> searchCustomer(String searchText, String searchText1, String searchText2, String searchText3, String searchText4, String searchText5, String searchText6) {
        ArrayList<CustomerDto> customerList = new ArrayList<>();

        String sql = "SELECT * FROM Customer WHERE " +
                "customer_id LIKE ? OR " +
                "name LIKE ? OR " +
                "nic LIKE ? OR " +
                "email LIKE ? OR " +
                "contact LIKE ? OR " +
                "address LIKE ? OR " +
                "description LIKE ?";

        try {
            ResultSet rs = CrudUtil.execute(sql,
                    "%" + searchText + "%",
                    "%" + searchText1 + "%",
                    "%" + searchText2 + "%",
                    "%" + searchText3 + "%",
                    "%" + searchText4 + "%",
                    "%" + searchText5 + "%",
                    "%" + searchText6 + "%"
            );

            while (rs.next()) {
                customerList.add(new CustomerDto(
                        rs.getString("customer_id"),
                        rs.getString("name"),
                        rs.getString("nic"),
                        rs.getString("email"),
                        rs.getString("contact"),
                        rs.getString("address"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // or use logger
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return customerList;
    }


}
