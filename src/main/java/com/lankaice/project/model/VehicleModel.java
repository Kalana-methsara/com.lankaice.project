package com.lankaice.project.model;

import com.lankaice.project.dto.VehicleDto;
import com.lankaice.project.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleModel {

    public ArrayList<VehicleDto> viewAllVehicles() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Vehicle");
        ArrayList<VehicleDto> vehicles = new ArrayList<>();
        while (resultSet.next()) {
            VehicleDto vehicle = new VehicleDto(
                    resultSet.getInt("vehicle_id"),
                    resultSet.getString("vehicle_number"),
                    resultSet.getString("status"),
                    resultSet.getInt("capacity"),
                    resultSet.getString("model")
            );
            vehicles.add(vehicle);
        }
        return vehicles;
    }
    public ArrayList<VehicleDto> activeVehicles() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Vehicle WHERE status = 'Active'");
        ArrayList<VehicleDto> activeList = new ArrayList<>();
        while (resultSet.next()) {
            VehicleDto vehicle = new VehicleDto(
                    resultSet.getInt("vehicle_id"),
                    resultSet.getString("vehicle_number"),
                    resultSet.getString("status"),
                    resultSet.getInt("capacity"),
                    resultSet.getString("model")
            );
            activeList.add(vehicle);
        }
        return activeList;
    }

}

