package com.lankaice.project.model;

import com.lankaice.project.dto.TransportDto;
import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TransportModel {

    public int getTodayTransportTotal() throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(quantity) AS total_quantity FROM Transport WHERE transport_date = CURDATE()";
        ResultSet rs = CrudUtil.execute(sql);
        if (rs.next()) {
            return rs.getInt("total_quantity");
        }
        return 0;
    }

    public boolean saveTransport(TransportDto dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Transport (transport_id, product_id, vehicle_number, transport_date, start_time, end_time, quantity, location, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return CrudUtil.execute(sql,
                dto.getTransportId(),
                dto.getProductId(),
                dto.getVehicleNumber(),
                dto.getTransportDate(),
                dto.getDeliveryBeginTime() != null ? Time.valueOf(dto.getDeliveryBeginTime()) : null,
                dto.getDeliveryEndTime() != null ? Time.valueOf(dto.getDeliveryEndTime()) : null,
                dto.getQuantity(),
                dto.getLocation(),
                dto.getStatus()
        );
    }

    public boolean updateTransport(TransportDto dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Transport SET product_id = ?, vehicle_number = ?, transport_date = ?, start_time = ?, end_time = ?, quantity = ?, location = ?, status = ? WHERE transport_id = ?";

        return CrudUtil.execute(sql,
                dto.getProductId(),
                dto.getVehicleNumber(),
                dto.getTransportDate(),
                dto.getDeliveryBeginTime() != null ? Time.valueOf(dto.getDeliveryBeginTime()) : null,
                dto.getDeliveryEndTime() != null ? Time.valueOf(dto.getDeliveryEndTime()) : null,
                dto.getQuantity(),
                dto.getLocation(),
                dto.getStatus(),
                dto.getTransportId()
        );
    }

    public boolean deleteTransport(String transportId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Transport WHERE transport_id = ?";
        return CrudUtil.execute(sql, transportId);
    }

    public List<TransportDto> getAllTransport() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Transport ORDER BY transport_date DESC";
        ResultSet rs = CrudUtil.execute(sql);
        List<TransportDto> transportList = new ArrayList<>();

        while (rs.next()) {
            transportList.add(mapResultSetToDto(rs));
        }
        return transportList;
    }

    public List<TransportDto> getTransportByDate(String date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Transport WHERE transport_date = ?";
        ResultSet rs = CrudUtil.execute(sql, date);

        List<TransportDto> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapResultSetToDto(rs));
        }
        return list;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        String prefix = "TP";
        ResultSet rs = CrudUtil.execute("SELECT transport_id FROM Transport ORDER BY transport_id DESC LIMIT 1");

        if (rs.next()) {
            String lastId = rs.getString("transport_id");
            int nextNum = Integer.parseInt(lastId.substring(2)) + 1;
            return String.format("%s%03d", prefix, nextNum);
        }
        return prefix + "001";
    }

    // --- Utility Methods ---

    private Time parseTime(String timeStr) {
        if (timeStr == null || timeStr.isBlank()) return null;
        if (!timeStr.matches("\\d{2}:\\d{2}")) return null;
        return Time.valueOf(timeStr + ":00");
    }

    private TransportDto mapResultSetToDto(ResultSet rs) throws SQLException {
        return new TransportDto(
                rs.getString("transport_id"),
                rs.getString("product_id"),
                rs.getString("vehicle_number"),
                rs.getString("transport_date"),
                rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime() : null,
                rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime() : null,
                rs.getInt("quantity"),
                rs.getString("location"),
                rs.getString("status")
        );
    }

    private String formatTime(Time time) {
        return (time != null) ? time.toString().substring(0, 5) : null;
    }
}
