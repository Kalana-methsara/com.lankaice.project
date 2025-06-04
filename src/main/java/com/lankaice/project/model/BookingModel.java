package com.lankaice.project.model;

import com.lankaice.project.dto.BookingDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingModel {

    public boolean addBooking(BookingDto dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Booking (customer_id, product_id, request_date, request_time, quantity, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                dto.getCustomerId(),
                dto.getProductId(),
                dto.getRequestDate(),
                dto.getRequestTime(),
                dto.getQuantity(),
                dto.getStatus()
        );
    }

    public boolean updateBooking(BookingDto dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Booking SET product_id = ?, request_time = ?, quantity = ?, status = ? " +
                "WHERE customer_id = ? AND request_date = ?";

        return CrudUtil.execute(sql,
                dto.getProductId(),      // 1
                dto.getRequestTime(),    // 2
                dto.getQuantity(),       // 3
                dto.getStatus(),         // 4
                dto.getCustomerId(),     // 5
                dto.getRequestDate()     // 6
        );
    }


    public boolean deleteBooking(String customerId, String date) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Booking WHERE customer_id = ? AND request_date = ?";
        return CrudUtil.execute(sql, customerId, date);
    }


    public ArrayList<BookingDto> getAllBookings() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM Booking");
        ArrayList<BookingDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(convertToDto(rs));
        }
        return list;
    }

    public ArrayList<BookingDto> searchByCustomerId(String customerId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Booking WHERE customer_id LIKE ?";
        ResultSet rs = CrudUtil.execute(sql, "%" + customerId + "%");
        ArrayList<BookingDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(convertToDto(rs));
        }
        return list;
    }

    public ArrayList<BookingDto> searchByDate(String date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Booking WHERE request_date = ?";
        ResultSet rs = CrudUtil.execute(sql, date);
        ArrayList<BookingDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(convertToDto(rs));
        }
        return list;
    }

    public BookingDto findProduct(String customerId, String requestDate, int qty) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Booking WHERE customer_id = ? AND request_date = ? AND quantity = ?";
        ResultSet rs = CrudUtil.execute(sql, customerId, requestDate, qty);
        if (rs.next()) {
            return convertToDto(rs);
        }
        return null;
    }

    public BookingDto getBookingByCustomerIdAndDate(String customerId, String date, String quantity) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Booking WHERE customer_id = ? AND request_date = ? AND quantity = ?";
        ResultSet rs = CrudUtil.execute(sql, customerId, date, quantity);
        if (rs.next()) {
            return convertToDto(rs);
        }
        return null;
    }

    public boolean isDuplicate(String customerId, String date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT 1 FROM Booking WHERE customer_id = ? AND request_date = ?";
        ResultSet rs = CrudUtil.execute(sql, customerId, date);
        return rs.next();
    }

    public static String getCustomerNameById(String customerId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT name FROM Customer WHERE customer_id = ?", customerId);
        if (rs.next()) {
            return rs.getString("name");
        }
        return "Unknown";
    }

    private BookingDto convertToDto(ResultSet rs) throws SQLException {
        return new BookingDto(
                rs.getInt("booking_id"),
                rs.getString("customer_id"),
                rs.getString("product_id"),
                rs.getDate("request_date"),
                rs.getString("request_time"),
                rs.getInt("quantity"),
                rs.getString("status")
        );
    }
}
