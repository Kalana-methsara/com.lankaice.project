package com.lankaice.project.model;

import com.lankaice.project.util.CrudUtil;
import com.lankaice.project.dto.OrderPaymentDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderPaymentModel {


    public void savePayment(OrderPaymentDto dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Order_Payment (payment_id, order_id, payment_method, items_count, subtotal, discount, net_total, payment_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        CrudUtil.execute(
                sql,
                dto.getPaymentId(),
                dto.getOrderId(),
                dto.getPaymentMethod(),
                dto.getItemCount(),
                dto.getSubtotal(),
                dto.getDiscount(),
                dto.getNetTotal(),
                dto.getPaymentDate(),
                dto.getStatus()
        );
    }

    public boolean updatePayment(OrderPaymentDto dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Order_Payment SET order_id = ?, payment_method = ?, item_count = ?, subtotal = ?, discount = ?, net_total = ?, payment_date = ?, status = ? " +
                "WHERE payment_id = ?";
        return CrudUtil.execute(
                sql,
                dto.getOrderId(),
                dto.getPaymentMethod(),
                dto.getItemCount(),
                dto.getSubtotal(),
                dto.getDiscount(),
                dto.getNetTotal(),
                dto.getPaymentDate(),
                dto.getStatus(),
                dto.getPaymentId()
        );
    }

    public boolean deletePayment(String paymentId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Order_Payment WHERE payment_id = ?";
        return CrudUtil.execute(sql, paymentId);
    }

    public List<OrderPaymentDto> getAllPayment() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Order_Payment";
        ResultSet resultSet = CrudUtil.execute(sql);

        List<OrderPaymentDto> list = new ArrayList<>();

        while (resultSet.next()) {
            OrderPaymentDto dto = new OrderPaymentDto(
                    resultSet.getString("payment_id"),
                    resultSet.getInt("order_id"),
                    resultSet.getString("payment_method"),
                    resultSet.getInt("item_count"),
                    resultSet.getDouble("subtotal"),
                    resultSet.getDouble("discount"),
                    resultSet.getDouble("net_total"),
                    resultSet.getString("payment_date"),
                    resultSet.getString("status")
            );
            list.add(dto);
        }
        return list;
    }

    public boolean isPaymentCompleted(int orderId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT 1 FROM Order_Payment WHERE order_id = ? AND status = 'Success'";
        ResultSet resultSet = CrudUtil.execute(sql, orderId);
        return resultSet.next();
    }


    public String getNextPaymentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT payment_id FROM Order_Payment ORDER BY payment_id DESC LIMIT 1");
        String prefix = "PAY";

        if (resultSet.next()) {
            String lastId = resultSet.getString("payment_id");
            String numberPart = lastId.substring(3);

            try {
                int lastNumber = Integer.parseInt(numberPart);
                int nextNumber = lastNumber + 1;
                return String.format("%s%03d", prefix, nextNumber);
            } catch (NumberFormatException e) {
                return prefix + "001";
            }
        }

        return prefix + "001";
    }

}
