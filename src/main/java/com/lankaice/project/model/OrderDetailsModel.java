package com.lankaice.project.model;

import com.lankaice.project.db.DBConnection;
import com.lankaice.project.dto.OrderDetailsDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public Map<LocalDate, Integer> getSalesForLast7Days() throws SQLException {
        Map<LocalDate, Integer> salesMap = new LinkedHashMap<>();

        // Step 1: Initialize the map with the last 7 days and default value 0
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            salesMap.put(date, 0); // Default zero sales
        }

        // Step 2: Query sales data from DB
        String sql = """
        SELECT o.order_date, SUM(od.quantity * od.unit_price) AS total
        FROM Orders o
        JOIN Order_Details od ON o.order_id = od.order_id
        WHERE o.order_date >= ?
        GROUP BY o.order_date
        ORDER BY o.order_date
    """;

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(today.minusDays(6)));
            ResultSet rs = ps.executeQuery();

            // Step 3: Replace zero values with actual sales totals if present
            while (rs.next()) {
                LocalDate date = rs.getDate("order_date").toLocalDate();
                int total = rs.getInt("total");
                salesMap.put(date, total);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found", e);
        }

        return salesMap;
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



