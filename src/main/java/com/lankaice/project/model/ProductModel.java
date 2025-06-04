package com.lankaice.project.model;

import com.lankaice.project.dto.ProductDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel {

    // ✅ Get all products
    public List<ProductDto> getAllProducts() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Product";
        ResultSet rs = CrudUtil.execute(sql);

        List<ProductDto> productList = new ArrayList<>();
        while (rs.next()) {
            productList.add(new ProductDto(
                    rs.getString("product_id"),
                    rs.getString("name"),
                    rs.getDouble("weight"),
                    rs.getDouble("price_per_unit")
            ));
        }
        return productList;
    }

    public boolean updateProductPrice(String productId, double newPrice) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET price_per_unit = ? WHERE product_id = ?";
        return CrudUtil.execute(sql, newPrice, productId);
    }

    // ✅ Get product name by ID
    public String findNameById(String productId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT name FROM Product WHERE product_id = ?", productId);
        if (rs.next()) {
            return rs.getString("name");
        }
        return "";
    }

    // ✅ Get product ID by name
    public String findIdByName(String productName) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT product_id FROM Product WHERE name = ?", productName);
        if (rs.next()) {
            return rs.getString("product_id");
        }
        return "";
    }

    // ✅ Get all product IDs
    public List<String> getAllProductIds() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT product_id FROM Product");
        List<String> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rs.getString("product_id"));
        }
        return list;
    }

    // ✅ Get product name by ID (fixed incomplete method)
    public String getProductNameById(String productId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT name FROM Product WHERE product_id = ?", productId);
        if (rs.next()) {
            return rs.getString("name");
        }
        return "";
    }
}
