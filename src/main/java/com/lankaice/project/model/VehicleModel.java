package com.lankaice.project.model;

import com.lankaice.project.dto.VehicleDto;
import com.lankaice.project.util.CrudUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public ArrayList<String> getActiveVehicle() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT vehicle_number FROM Vehicle WHERE status = 'Active'");
        ArrayList<String> activeVehicleIds = new ArrayList<>();
        while (resultSet.next()) {
            activeVehicleIds.add(resultSet.getString("vehicle_number"));
        }
        return activeVehicleIds;
    }


    public String getVehicleId(String vehicleNumber) throws SQLException, ClassNotFoundException {
        String sql = "SELECT vehicle_id FROM Vehicle WHERE vehicle_number = ?";

        ResultSet resultSet = CrudUtil.execute(sql, vehicleNumber);
        if (resultSet.next()) {
            return resultSet.getString("vehicle_id");
        }
        return null;
    }

    public List<String> getInactiveVehicles() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT vehicle_number FROM Vehicle WHERE status = 'Inactive' OR status = 'Under Repair'"
        );
        List<String> inactiveVehicleNumbers = new ArrayList<>();
        while (resultSet.next()) {
            inactiveVehicleNumbers.add(resultSet.getString("vehicle_number"));
        }
        return inactiveVehicleNumbers;
    }

    public boolean setActiveVehicle(String vehicleNumber) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Vehicle SET status = 'Active' WHERE vehicle_number = ?", vehicleNumber);
    }
    public boolean setUnderRepairVehicle(String vehicleNumber) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Vehicle SET status = 'Under Repair' WHERE vehicle_number = ?", vehicleNumber);
    }

}

