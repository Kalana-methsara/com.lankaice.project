package com.lankaice.project.controller;

import com.lankaice.project.dto.VehicleDto;
import com.lankaice.project.model.VehicleModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TransportPageController implements Initializable {

    @FXML
    private Label txtVehicle;
    @FXML
    private AnchorPane ancTransport;

    @FXML
    private TableColumn<VehicleDto, String> colVehicleNo;

    @FXML
    private TableColumn<VehicleDto, String> colStatus;

    @FXML
    private TableColumn<VehicleDto, Integer> colCapacity;

    @FXML
    private TableColumn<VehicleDto, String> colModel;

    @FXML
    private TableView<VehicleDto> tableVehical;

    // Label arrays for easier management
    @FXML
    private List<Label> lblVehicleNumbers, lblCapacities, lblLats, lblLons;

    @FXML
    private Label lblVehicalNumber, lblVehicalNumber1, lblVehicalNumber2, lblVehicalNumber3, lblVehicalNumber4, lblVehicalNumber5, lblVehicalNumber6;
    @FXML
    private Label lblCapacity, lblCapacity1, lblCapacity2, lblCapacity3, lblCapacity4, lblCapacity5, lblCapacity6;
    @FXML
    private Label lblLat, lblLat1, lblLat2, lblLat3, lblLat4, lblLat5, lblLat6;
    @FXML
    private Label lblLon, lblLon1, lblLon2, lblLon3, lblLon4, lblLon5, lblLon6;

    private final double[] latitudes = {8.0269, 10.0083, 6.0184, 5.9485, 3.1399, 1.9779, 4.9271};
    private final double[] longitudes = {90.2170, 80.2492, 100.2423, 50.5350, 10.1026, 70.4303, 39.8612};

    private final VehicleModel vehicleModel = new VehicleModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLabelLists();
        loadVehicleTable();
        setVehicalLabels();
        setLocations();
       setVehicalNumber();


    }

    private void setVehicalNumber() {
        tableVehical.setOnMouseClicked(event -> {
            VehicleDto selectedVehicle = tableVehical.getSelectionModel().getSelectedItem();
            if (selectedVehicle != null) {
                txtVehicle.setText(selectedVehicle.getVehicleNumber());
            }
        });
    }


    private void setupLabelLists() {
        lblVehicleNumbers = List.of(lblVehicalNumber, lblVehicalNumber1, lblVehicalNumber2, lblVehicalNumber3,
                lblVehicalNumber4, lblVehicalNumber5, lblVehicalNumber6);
        lblCapacities = List.of(lblCapacity, lblCapacity1, lblCapacity2, lblCapacity3,
                lblCapacity4, lblCapacity5, lblCapacity6);
        lblLats = List.of(lblLat, lblLat1, lblLat2, lblLat3, lblLat4, lblLat5, lblLat6);
        lblLons = List.of(lblLon, lblLon1, lblLon2, lblLon3, lblLon4, lblLon5, lblLon6);
    }

    private void setLocations() {
        for (int i = 0; i < latitudes.length; i++) {
            lblLats.get(i).setText(String.valueOf(latitudes[i]));
            lblLons.get(i).setText(String.valueOf(longitudes[i]));
        }
    }

    private void setVehicalLabels() {
        try {
            VehicleModel vehicleModel = new VehicleModel();
            ArrayList<VehicleDto> vehicles = vehicleModel.viewAllVehicles();

            for (int i = 0; i < Math.min(vehicles.size(), 7); i++) {
                lblVehicleNumbers.get(i).setText(vehicles.get(i).getVehicleNumber());
                lblCapacities.get(i).setText(String.valueOf(vehicles.get(i).getCapacity()));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadVehicleTable() {
        colVehicleNo.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));

        try {
            VehicleModel vehicleModel = new VehicleModel();
            ArrayList<VehicleDto> vehicles = vehicleModel.viewAllVehicles();
            ObservableList<VehicleDto> observableList = FXCollections.observableArrayList(vehicles);
            tableVehical.setItems(observableList);

            tableVehical.setRowFactory(tv -> new TableRow<>() {
                @Override
                protected void updateItem(VehicleDto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else {
                        switch (item.getStatus()) {
                            case "Active" -> setStyle("-fx-background-color: #90ee90;");
                            case "Inactive" -> setStyle("-fx-background-color: #d9cd66;");
                            case "Under Repair" -> setStyle("-fx-background-color: #f08080;");
                            default -> setStyle("");
                        }
                    }
                }
            });

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleLocationClick(MouseEvent event, int index) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/VehicleTracker.fxml"));
            AnchorPane pane = loader.load();

            VehicleTrackerController controller = loader.getController();
            controller.setLocation(latitudes[index], longitudes[index]);

            ancTransport.getChildren().setAll(pane);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Vehicle Tracker Page Not Found!");
            e.printStackTrace();
        }
    }

    // Bind this in FXML or call from individual onLocationX methods
    @FXML
    void onLocation(MouseEvent event) {
        handleLocationClick(event, 0);
    }

    @FXML
    void onLocation1(MouseEvent event) {
        handleLocationClick(event, 1);
    }

    @FXML
    void onLocation2(MouseEvent event) {
        handleLocationClick(event, 2);
    }

    @FXML
    void onLocation3(MouseEvent event) {
        handleLocationClick(event, 3);
    }

    @FXML
    void onLocation4(MouseEvent event) {
        handleLocationClick(event, 4);
    }

    @FXML
    void onLocation5(MouseEvent event) {
        handleLocationClick(event, 5);
    }

    @FXML
    void onLocation6(MouseEvent event) {
        handleLocationClick(event, 6);
    }

    public void btnActiveOnAction(ActionEvent actionEvent) {
        String vehicleNumber = txtVehicle.getText();

        if (vehicleNumber == null || vehicleNumber.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please select a vehicle to activate.");
            return;
        }

        try {
            vehicleModel.setActiveVehicle(vehicleNumber);
            showAlert(Alert.AlertType.INFORMATION, "Vehicle activated successfully!");
            loadVehicleTable();
            txtVehicle.setText("");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to activate the vehicle.");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);
        DialogPane pane = alert.getDialogPane();
        pane.setStyle(type == Alert.AlertType.INFORMATION
                ? "-fx-border-color: blue; -fx-border-width: 2px;"
                : "-fx-border-color: red; -fx-border-width: 2px;");
        alert.show();
    }


    public void btnUnderRepairOnAction(ActionEvent actionEvent) {
        String vehicleNumber = txtVehicle.getText();

        if (vehicleNumber == null || vehicleNumber.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please select a vehicle to mark as under repair.");
            return;
        }

        try {
            vehicleModel.setUnderRepairVehicle(vehicleNumber);
            showAlert(Alert.AlertType.INFORMATION, "Vehicle marked as under repair successfully!");
            loadVehicleTable();
            txtVehicle.setText("");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to mark the vehicle as under repair.");
        }
    }


}
