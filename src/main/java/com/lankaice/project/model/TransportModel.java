package com.lankaice.project.model;

import com.lankaice.project.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransportModel{
    public int getTodayTransportTotal() throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(quantity) AS total_quantity FROM Transport WHERE transport_date = CURDATE()";
        ResultSet rs = CrudUtil.execute(sql);
        if (rs.next()) {
            return rs.getInt("total_quantity");
        }
        return 0;
    }

}