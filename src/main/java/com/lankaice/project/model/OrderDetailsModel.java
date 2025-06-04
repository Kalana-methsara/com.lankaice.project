package com.lankaice.project.model;

import com.lankaice.project.dto.OrderDetailsDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderDetailsModel {
private final StockModel stockModel=new StockModel();
    public boolean saveOrderDetailsList(ArrayList<OrderDetailsDto> cartList) throws SQLException, ClassNotFoundException {
        for (OrderDetailsDto orderDetailsDto : cartList) {
            boolean isDetailsSaved = saveOrderDetails(orderDetailsDto);
            if (!isDetailsSaved) {
                return false;
            }
            boolean isUpdated = stockModel.reduceQty(orderDetailsDto);
            if (!isUpdated) {
                return false;
            }

            //oooo
        }
        return true;
    }

    private boolean saveOrderDetails(OrderDetailsDto orderDetailsDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Order_Details (order_id, product_id, quantity, unit_price, discount) VALUES (?,?,?,?,?)",
                orderDetailsDto.getOrderId(),
                orderDetailsDto.getProductId(),
                orderDetailsDto.getQuantity(),
                orderDetailsDto.getUnitPrice(),
                orderDetailsDto.getDiscount());
    }

    public boolean saveOrderDetailsList(int orderId, String code, int qty, double unitPrice, double discount) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Order_Details (order_id, product_id, quantity, unit_price, discount) VALUES (?,?,?,?,?)",orderId,code,qty,unitPrice,discount);
    }

    public int todaySale() throws SQLException, ClassNotFoundException {
        String sql = """
        SELECT SUM(od.quantity) AS total_quantity
        FROM Order_Details od
        JOIN Orders o ON od.order_id = o.order_id
        WHERE o.order_date = ?
    """;
        ResultSet rs = CrudUtil.execute(sql, java.sql.Date.valueOf(LocalDate.now()));
        if (rs.next()) {
            return rs.getInt("total_quantity");
        }
        return 0;
    }
}



