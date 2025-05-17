package com.lankaice.project.model;

import com.lankaice.project.dto.ProductDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel {

    public static List<ProductDto> getAllProducts() throws SQLException, ClassNotFoundException {
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
    public static boolean updateProductPrice(String code, double newPrice) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET price_per_unit = ? WHERE product_id = ?";
        return CrudUtil.execute(sql, newPrice, code);
    }

}
