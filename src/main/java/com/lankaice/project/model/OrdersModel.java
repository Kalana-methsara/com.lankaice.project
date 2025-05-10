package com.lankaice.project.model;

import com.lankaice.project.dto.OrdersDto;
import com.lankaice.project.dto.PendingOrderDto;
import com.lankaice.project.dto.UserDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrdersModel {
    public ArrayList<OrdersDto> viewAllOrders() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Orders");
        ArrayList<OrdersDto> orders = new ArrayList<>();
        while (resultSet.next()) {

            LocalDate orderDate = resultSet.getDate("orderDate").toLocalDate();
            LocalTime orderTime = resultSet.getTime("orderTime").toLocalTime();

            OrdersDto order = new OrdersDto(
                    resultSet.getInt("orderId"),
                    resultSet.getInt("customerId"),
                    orderDate.toString(),  // Correct order date
                    orderTime.toString(),  // Correct order time
                    resultSet.getString("status"),
                    resultSet.getDouble("totalAmount")
            );
            orders.add(order);
        }
        return orders;
    }

public ArrayList<PendingOrderDto> getPendingOrdersByDate(LocalDate date) throws SQLException, ClassNotFoundException {
    String sql = "SELECT o.order_id, c.name AS customer_name, p.name AS product_name, " +
            "od.quantity, od.request_time " +
            "FROM Orders o " +
            "JOIN Customer c ON o.customer_id = c.customer_id " +
            "JOIN Order_Details od ON o.order_id = od.order_id " +
            "JOIN Product p ON od.product_id = p.product_id " +
            "WHERE o.status = 'Pending' " +
            "AND DATE(o.order_date) = ?";

    ResultSet resultSet = CrudUtil.execute(sql, java.sql.Date.valueOf(date));
    ArrayList<PendingOrderDto> pendingList = new ArrayList<>();

    while (resultSet.next()) {
        PendingOrderDto dto = new PendingOrderDto();
        dto.setOrderId(resultSet.getInt("order_id"));
        dto.setCustomerName(resultSet.getString("customer_name"));
        dto.setProductName(resultSet.getString("product_name"));
        dto.setQuantity(resultSet.getInt("quantity"));
        dto.setRequestTime(resultSet.getString("request_time"));

        pendingList.add(dto);
    }

    return pendingList;
}


}
