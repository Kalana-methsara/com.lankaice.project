package com.lankaice.project.model;

import com.lankaice.project.dto.OrderDetailsDto;
import com.lankaice.project.dto.StockDto;
import com.lankaice.project.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class StockModel {

    public boolean reduceQty(OrderDetailsDto orderDetailsDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Stock SET stock_quantity = stock_quantity - ? WHERE product_id = ?",
                orderDetailsDto.getQuantity(),
                orderDetailsDto.getProductId()
        );
    }
    public boolean reduceQty(String productId,int quantity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Stock SET stock_quantity = stock_quantity - ? WHERE product_id = ?",
              quantity,productId
        );
    }

    public int currentStock() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(stock_quantity), 0) AS total FROM Stock";
        ResultSet rs = CrudUtil.execute(sql);
        if (rs.next()) {
            return rs.getInt("total");
        }
        return 0;
    }

    public int todayAddedStock() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(stock_quantity), 0) AS total FROM Stock WHERE DATE(stock_date) = CURDATE()";
        ResultSet rs = CrudUtil.execute(sql);
        if (rs.next()) {
            return rs.getInt("total");
        }
        return 0;
    }

    public boolean addStock(String productId, String productName, int quantity, LocalDate date, LocalTime time) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Stock (product_id, product_name, stock_quantity, stock_date, stock_time) VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, productId, productName, quantity, date, time);
    }


    public List<StockDto> getAllStock() throws SQLException, ClassNotFoundException {
        List<StockDto> list = new ArrayList<>();
        String sql = "SELECT * FROM Stock ORDER BY stock_date DESC, stock_time DESC";
        ResultSet rs = CrudUtil.execute(sql);

        while (rs.next()) {
            list.add(new StockDto(
                    rs.getInt("stock_id"),
                    rs.getString("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("stock_quantity"),
                    rs.getDate("stock_date").toString(),
                    rs.getTime("stock_time").toString()
            ));
        }

        return list;
    }


    public List<StockDto> getStockByMonthAndYear(String year, String monthName) throws SQLException, ClassNotFoundException {
        List<StockDto> list = new ArrayList<>();
        int month = Month.valueOf(monthName.toUpperCase()).getValue();

        String sql = "SELECT * FROM Stock WHERE YEAR(stock_date) = ? AND MONTH(stock_date) = ? ORDER BY stock_date DESC";
        ResultSet rs = CrudUtil.execute(sql, Integer.parseInt(year), month);

        while (rs.next()) {
            list.add(new StockDto(
                    rs.getInt("stock_id"),
                    rs.getString("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("stock_quantity"),
                    rs.getDate("stock_date").toString(),
                    rs.getTime("stock_time").toString()
            ));
        }

        return list;
    }

    public boolean updateStock(StockDto dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Stock SET product_id = ?, product_name = ?, stock_quantity = ?, stock_date = ?,stock_time  = ? WHERE stock_id = ?";
        return CrudUtil.execute(sql, dto.getProductId(), dto.getProductName(), dto.getQty(), dto.getDate(), dto.getTime(), dto.getStockId());
    }


    public boolean deleteStock(String stockId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Stock WHERE stock_id = ?";
        return CrudUtil.execute(sql, stockId);
    }



    public List<StockDto> searchStock(String text) throws SQLException, ClassNotFoundException {
        List<StockDto> list = new ArrayList<>();
        String sql = "SELECT * FROM Stock WHERE product_name LIKE ? OR product_id LIKE ? OR stock_id LIKE ?";
        String pattern = "%" + text + "%";
        ResultSet rs = CrudUtil.execute(sql, pattern, pattern, pattern);

        while (rs.next()) {
            list.add(new StockDto(
                    rs.getInt("stock_id"),
                    rs.getString("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("stock_quantity"),
                    rs.getDate("stock_date").toString(),
                    rs.getTime("stock_time").toString()
            ));
        }

        return list;
    }
    public StockDto getStockById(int stockId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Stock WHERE stock_id = ?";
        ResultSet rs = CrudUtil.execute(sql, stockId);
        if (rs.next()) {
            return new StockDto(
                    rs.getInt("stock_id"),
                    rs.getString("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("stock_quantity"),
                    rs.getString("stock_date"),
                    rs.getString("stock_time")
            );
        }
        return null;
    }

    public int getCurrentStockQty(String productId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT stock_quantity FROM Stock WHERE product_id = ?", productId);
        if (rs.next()) {
            return rs.getInt("stock_quantity");
        } else {
            throw new SQLException("Product not found: " + productId);
        }
    }

}
