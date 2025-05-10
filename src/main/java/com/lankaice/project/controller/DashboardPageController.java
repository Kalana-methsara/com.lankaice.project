package com.lankaice.project.controller;

import com.lankaice.project.dto.OrdersDto;
import com.lankaice.project.dto.PendingOrderDto;
import com.lankaice.project.model.OrdersModel;
import com.lankaice.project.model.VehicleModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// DTO classes (You need to create these)
/*import com.lankaice.project.dto.DriverDto;*/
import com.lankaice.project.dto.OrdersDto;
import com.lankaice.project.dto.VehicleDto;

public class DashboardPageController implements Initializable {

    @FXML
    private TableView<VehicleDto> tableVehical;

    @FXML
    private TableColumn<VehicleDto, String> colVehicleNo;
    @FXML
    private TableColumn<VehicleDto, String> colModel;
    @FXML
    private TableColumn<VehicleDto, Integer> colCapacity;

    @FXML
    private TableColumn<PendingOrderDto, String> colOrderNo;

    @FXML
    private TableColumn<PendingOrderDto, String> colTime;

    @FXML
    private TableColumn<PendingOrderDto, Integer> colQty;

    @FXML
    private TableColumn<PendingOrderDto, String> colProcuctName;

    @FXML
    private TableColumn<PendingOrderDto, String> colCustomerName;

    @FXML
    private TableView<PendingOrderDto> tableOrder;

    @FXML
    private Label lblSale;

    @FXML
    private Label lblProduction;

    @FXML
    private Label lblTransport;

    @FXML
    private Label lblCurrentStock;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadVehicleTable();
        loadDashboardStats();
        pendingOrderToday();
    }

    private void loadVehicleTable() {
        colVehicleNo.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        try {
            VehicleModel vehicleModel = new VehicleModel();
            ArrayList<VehicleDto> vehicles = vehicleModel.activeVehicles();
            ObservableList<VehicleDto> observableList = FXCollections.observableArrayList(vehicles);
            tableVehical.setItems(observableList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void pendingOrderToday() {
        colOrderNo.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colProcuctName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("requestTime"));

        try {
            OrdersModel ordersModel = new OrdersModel();
            ArrayList<PendingOrderDto> pendingOrder = ordersModel.getPendingOrdersByDate(LocalDate.now());
            ObservableList<PendingOrderDto> observableList = FXCollections.observableArrayList(pendingOrder);
            tableOrder.setItems(observableList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private void loadDashboardStats() {
        lblSale.setText("25845");
        lblProduction.setText("45000");
        lblTransport.setText("38000");
        lblCurrentStock.setText("1500");
    }
}
