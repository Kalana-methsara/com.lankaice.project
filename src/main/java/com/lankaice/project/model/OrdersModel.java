package com.lankaice.project.model;

import com.lankaice.project.db.DBConnection;
import com.lankaice.project.dto.OrderDetailsDto;
import com.lankaice.project.dto.OrdersDto;
import com.lankaice.project.dto.PendingOrderDto;
import com.lankaice.project.util.CrudUtil;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class OrdersModel {

    private final OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
    private final ProductModel productModel= new ProductModel();
    private final StockModel stockModel= new StockModel();

    // View all orders
    public ArrayList<OrdersDto> viewAllOrders() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Orders");
        ArrayList<OrdersDto> orders = new ArrayList<>();

        while (resultSet.next()) {
            OrdersDto order = new OrdersDto(
                    resultSet.getInt("order_id"),
                    resultSet.getString("customer_id"),
                    resultSet.getDate("order_date").toString(),
                    resultSet.getTime("order_time").toString(),
                    resultSet.getString("description"),
                    resultSet.getString("vehicle_number"),
                    resultSet.getDouble("total_amount"),
                    new ArrayList<>() // No details fetched here
            );
            orders.add(order);
        }
        return orders;
    }

    // Get pending orders by date
    public ArrayList<PendingOrderDto> getPendingOrdersByDate(LocalDate date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT po.order_id, c.name AS customer_name, p.name AS product_name, po.quantity, po.status FROM PendingOrder po JOIN Customer c ON po.customer_id = c.customer_id JOIN Product p ON po.product_id = p.product_id WHERE po.status = 'PENDING' AND DATE(po.request_time) = ? ";


        ResultSet resultSet = CrudUtil.execute(sql, java.sql.Date.valueOf(date));
        ArrayList<PendingOrderDto> pendingList = new ArrayList<>();

        while (resultSet.next()) {
            PendingOrderDto dto = new PendingOrderDto(
                    resultSet.getInt("order_id"),
                    resultSet.getString("customer_name"),
                    resultSet.getString("product_name"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("status")
            );
            pendingList.add(dto);
        }

        return pendingList;
    }

    // Place a full order transaction
    public boolean placeOrder(OrdersDto dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            // Insert into Orders
            boolean isOrderSaved = CrudUtil.execute(
                    "INSERT INTO Orders(order_id, customer_id, order_date, order_time, description, vehicle_number, total_amount) VALUES(?, ?, ?, ?, ?, ?, ?)",
                    dto.getOrderId(), dto.getCustomerId(), dto.getOrderDate(), dto.getOrderTime(),
                    dto.getDescription(), dto.getVehicle_number(), dto.getTotalAmount());

            if (!isOrderSaved) {
                connection.rollback();
                return false;
            }

            // Insert into Order_Details and update stock
            for (OrderDetailsDto detail : dto.getCartList()) {
                boolean isDetailSaved = CrudUtil.execute(
                        "INSERT INTO Order_Details(order_id, product_id, quantity, unit_price, discount) VALUES(?, ?, ?, ?, ?)",
                        dto.getOrderId(), detail.getProductId(), detail.getQuantity(), detail.getUnitPrice(), detail.getDiscount());

                if (!isDetailSaved) {
                    connection.rollback();
                    return false;
                }

                boolean isStockUpdated = CrudUtil.execute(
                        "UPDATE Stock SET stock_quantity = stock_quantity - ? WHERE product_id = ?",
                        detail.getQuantity(), detail.getProductId());

                if (!isStockUpdated) {
                        connection.rollback();
                    return false;
                }
                boolean isPendingOrderSaved = CrudUtil.execute(
                        "INSERT INTO PendingOrder(order_id, customer_id, product_id, quantity, request_time, status) VALUES (?, ?, ?, ?, ?, ?)",
                        dto.getOrderId(), dto.getCustomerId(), detail.getProductId(), detail.getQuantity(),
                        Timestamp.valueOf(LocalDateTime.now()), "PENDING");

                if (!isPendingOrderSaved) {
                    connection.rollback();
                    return false;
                }

            }

            // Update Booking status if exists
            ResultSet rs = CrudUtil.execute(
                    "SELECT * FROM Booking WHERE customer_id = ? AND request_date = ?",
                    dto.getCustomerId(), dto.getOrderDate());

            if (rs.next()) {
                boolean isBookingUpdated = CrudUtil.execute(
                        "UPDATE Booking SET status = 'Confirmed' WHERE customer_id = ? AND request_date = ?",
                        dto.getCustomerId(), dto.getOrderDate());

                if (!isBookingUpdated) {
                    connection.rollback();
                    return false;
                }
            }

            // Add delivery and update vehicle if vehicle is assigned
            if (dto.getVehicle_number() != null && !dto.getVehicle_number().isEmpty()) {
                VehicleModel vModel = new VehicleModel();
                String vehicleId = vModel.getVehicleId(dto.getVehicle_number());

                boolean isDeliverySaved = CrudUtil.execute(
                        "INSERT INTO Delivery (order_id, delivery_date, delivery_time, delivery_address, delivery_status, vehicle_id) VALUES (?, ?, ?,?, ?, ?)",
                        dto.getOrderId(), dto.getOrderDate(), dto.getOrderTime(), "Galle", "Pending", vehicleId);

                if (!isDeliverySaved) {
                    connection.rollback();
                    return false;
                }

                boolean isVehicleStatusUpdated = CrudUtil.execute(
                        "UPDATE Vehicle SET status = 'Inactive' WHERE vehicle_id = ?",
                        vehicleId);

                if (!isVehicleStatusUpdated) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // Get last order ID
    public int getLastOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT MAX(order_id) AS order_id FROM Orders");
        if (rs.next()) {
            int lastId = rs.getInt("order_id");
            return rs.wasNull() ? 1001 : lastId;
        }
        return 1001;
    }

    public boolean updateOrderStatus(int orderId, String productName, String newStatus) throws SQLException, ClassNotFoundException {
        String productId = productModel.findIdByName(productName);
        String sql = "UPDATE PendingOrder SET status = ? WHERE order_id = ? AND product_id = ?";
        return CrudUtil.execute(sql, newStatus, orderId, productId); // ✅ Correct order
    }


}
