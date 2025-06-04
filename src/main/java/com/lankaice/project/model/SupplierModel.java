package com.lankaice.project.model;

import com.lankaice.project.dto.CustomerDto;
import com.lankaice.project.dto.SupplierDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {
    public boolean addSupplier(SupplierDto supplierDto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Supplier (supplier_id, name, nic,contact,email,address) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                supplierDto.getSupplierId(),
                supplierDto.getName(),
                supplierDto.getNic(),
                supplierDto.getContact(),
                supplierDto.getEmail(),
                supplierDto.getAddress()
        );
    }

    public boolean deleteSupplier(String supplierId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Supplier WHERE supplier_id = ?";
        return CrudUtil.execute(sql,supplierId);
    }

    public boolean updateSupplier(SupplierDto dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Supplier SET name=?, nic=?, email=?, contact=?, address=? WHERE supplier_id=?";
        return CrudUtil.execute(sql,
                dto.getName(),
                dto.getNic(),
                dto.getEmail(),
                dto.getContact(),
                dto.getAddress(),
                dto.getSupplierId()
        );
    }

    public ArrayList<SupplierDto> searchSupplier(String searchText, String searchText1, String searchText2, String searchText3, String searchText4, String searchText5) {
        ArrayList<SupplierDto> supplierList = new ArrayList<>();

        String sql = "SELECT * FROM Supplier WHERE " +
                "supplier_id LIKE ? OR " +
                "name LIKE ? OR " +
                "nic LIKE ? OR " +
                "email LIKE ? OR " +
                "contact LIKE ? OR " +
                "address LIKE ? ";

        try {
            ResultSet rs = CrudUtil.execute(sql,
                    "%" + searchText + "%",
                    "%" + searchText1 + "%",
                    "%" + searchText2 + "%",
                    "%" + searchText3 + "%",
                    "%" + searchText4 + "%",
                    "%" + searchText5 + "%"
            );

            while (rs.next()) {
                supplierList.add(new SupplierDto(
                        rs.getString("supplier_id"),
                        rs.getString("name"),
                        rs.getString("nic"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("address")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // or use logger
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return supplierList;
    }

    public int getSupplierCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM Supplier");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public String getLastSupplierId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT MAX(supplier_id) FROM Supplier");
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public ArrayList<SupplierDto> getAllSuppliers() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Supplier");
        ArrayList<SupplierDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(convertToDto(rs));
        }
        return list;
    }

    private SupplierDto convertToDto(ResultSet rs) throws SQLException {
        return new SupplierDto(
                rs.getString("supplier_id"),
                rs.getString("name"),
                rs.getString("nic"),
                rs.getString("contact"),
                rs.getString("email"),
                rs.getString("address")
        );
    }
    public static List<SupplierDto> getAllSupplier() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Supplier");
        List<SupplierDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new SupplierDto(rs.getString("supplier_id"), rs.getString("name"),rs.getString("email")));
        }
        return list;
    }

    public static String getSupplierName(String supplierId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT name FROM Supplier WHERE supplier_id = ?", supplierId);
        if (rs.next()) {
            return rs.getString("name");
        }
        return null;
    }
}
