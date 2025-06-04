package com.lankaice.project.model;

import com.lankaice.project.dto.tm.CartItemTM;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryCartModel {

    public static boolean saveCartItem(CartItemTM item) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Inventory_Cart (supplier_id, material_id,name, unit_type, unit_price, quantity) VALUES (?, ?, ?,?, ?, ?)";
        return CrudUtil.execute(sql,
                item.getSupplierId(),
                item.getMaterialId(),
                item.getName(),
                item.getUnitType(),
                item.getUnitPrice(),
                item.getQuantity()
        );
    }

    public static boolean updateCartItem(CartItemTM item, double newQty) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Inventory_Cart SET quantity = ?, unit_price = ?, unit_type = ? WHERE supplier_id = ? AND material_id = ?";
        return CrudUtil.execute(sql,
                newQty,
                item.getUnitPrice(),
                item.getUnitType(),
                item.getSupplierId(),
                item.getMaterialId()
        );
    }

    public static boolean removeCartItem(int cartId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Inventory_Cart WHERE cart_id = ?";
        return CrudUtil.execute(sql, cartId);
    }


    public ArrayList<CartItemTM> getAll() throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Inventory_Cart");
        ArrayList<CartItemTM> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new CartItemTM(
                    resultSet.getInt("cart_id"),
                    resultSet.getString("supplier_id"),
                    resultSet.getInt("material_id"),
                    resultSet.getString("name"),
                    resultSet.getString("unit_type"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("total")
            ));
        }
        return list;
    }

}
