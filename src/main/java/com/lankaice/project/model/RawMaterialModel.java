package com.lankaice.project.model;

import com.lankaice.project.dto.RawMaterialsDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RawMaterialModel {
    public static List<RawMaterialsDto> getItemsBySupplier(String supplierId) throws SQLException, SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Raw_Materials WHERE supplier_id = ?", supplierId);
        List<RawMaterialsDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new RawMaterialsDto(
                    rs.getInt("material_id"),
                    rs.getString("name"),
                    rs.getString("unit_type"),
                    rs.getDouble("unit_cost"),
                    rs.getInt("quantity_available")
            ));
        }
        return list;
    }

    public static RawMaterialsDto getItemById(int materialId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Raw_Materials WHERE material_id = ?", materialId);
        if (rs.next()) {
            return new RawMaterialsDto(
                    rs.getInt("material_id"),
                    rs.getString("name"),
                    rs.getString("unit_type"),
                    rs.getDouble("unit_cost"),
                    rs.getInt("quantity_available")
            );
        }
        return null;
    }

    public static boolean updateMaterialQtyAfterPurchase(String materialId, int qtyToAdd) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Raw_Materials SET quantity_available = quantity_available + ?, lastUpdate = CURRENT_TIMESTAMP WHERE material_id = ?";
        return CrudUtil.execute(sql, qtyToAdd, materialId);
    }


    public static boolean updateMaterialPrice(int materialId, double newPrice) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Raw_Materials SET unit_cost = ?, lastUpdate = CURRENT_TIMESTAMP WHERE material_id = ?";
        return CrudUtil.execute(sql, newPrice, materialId);
    }
}
