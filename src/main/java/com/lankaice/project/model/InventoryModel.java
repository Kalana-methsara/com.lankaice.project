package com.lankaice.project.model;

import com.lankaice.project.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryModel {

    public String getNextOrderId() throws SQLException, ClassNotFoundException {
        char tableChar = 'O';
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }
}
