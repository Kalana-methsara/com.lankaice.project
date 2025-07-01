package com.lankaice.project.controller;

import com.lankaice.project.dto.TransportDto;
import com.lankaice.project.model.ProductModel;
import com.lankaice.project.model.StockModel;
import com.lankaice.project.model.TransportModel;
import com.lankaice.project.model.VehicleModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class DistributionController implements Initializable {

    @FXML
    private AnchorPane ancDistribution;

    @FXML
    private ChoiceBox<String> cmdProductId;

    @FXML
    private ChoiceBox<String> cmdStatus;

    @FXML
    private ChoiceBox<String> cmdVehicalNumber;

    @FXML
    private TableColumn<TransportDto, String> colDate;

    @FXML
    private TableColumn<TransportDto, String> colInTime;

    @FXML
    private TableColumn<TransportDto, String> colLocation;

    @FXML
    private TableColumn<TransportDto, String> colOutTime;

    @FXML
    private TableColumn<TransportDto, String> colProductId;

    @FXML
    private TableColumn<TransportDto, Integer> colQty;

    @FXML
    private TableColumn<TransportDto, String> colStatus;

    @FXML
    private TableColumn<TransportDto, String> colTransportId;

    @FXML
    private TableColumn<TransportDto, String> colVehicalNumber;

    @FXML
    private TableView<TransportDto> tableView;

    @FXML
    private DatePicker textDate;

    @FXML
    private Hyperlink textError;

    @FXML
    private ChoiceBox<String> textHours;

    @FXML
    private ChoiceBox<String> textHours1;

    @FXML
    private Label textInTime;

    @FXML
    private TextField textLocation;

    @FXML
    private ChoiceBox<String> textMinutes;

    @FXML
    private ChoiceBox<String> textMinutes1;

    @FXML
    private Label textOutTime;

    @FXML
    private Label textDeliveryDuration;

    @FXML
    private TextField textQty;

    @FXML
    private TextField textTransportId;

    private final TransportModel transportModel = new TransportModel();
    private final VehicleModel vehicleModel = new VehicleModel();
    private final ProductModel productModel = new ProductModel();
    private final StockModel stockModel = new StockModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i <= 23; i++) {
            String hour = String.format("%02d", i);
            textHours.getItems().add(hour);
            textHours1.getItems().add(hour);
        }
        try {
            loadNextId();
            cmdStatus.setItems(FXCollections.observableArrayList("Pending","Delivered", "Cancelled"));
            List<String> vehicles = vehicleModel.getActiveVehicle();
            cmdVehicalNumber.setItems(FXCollections.observableArrayList(vehicles));
            List<String> productIds = productModel.getAllProductIds();
            cmdProductId.setItems(FXCollections.observableArrayList(productIds));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i <= 59; i++) {
            String minute = String.format("%02d", i);
            textMinutes.getItems().add(minute);
            textMinutes1.getItems().add(minute);
        }

        textDate.setValue(LocalDate.now());
        loadTable();

        textDate.valueProperty().addListener((obs, oldDate, newDate) -> loadTable());
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = transportModel.getNextId();
        textTransportId.setText(nextId);
    }

    private void loadTable() {
        LocalDate selectedDate = textDate.getValue();
        if (selectedDate == null) return;

        colTransportId.setCellValueFactory(new PropertyValueFactory<>("transportId"));
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colVehicalNumber.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("transportDate"));
        colInTime.setCellValueFactory(new PropertyValueFactory<>("deliveryBeginTime"));
        colOutTime.setCellValueFactory(new PropertyValueFactory<>("deliveryEndTime"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        try {
            List<TransportDto> transportList = transportModel.getTransportByDate(selectedDate.toString());

            transportList.sort((a, b) -> {
                if (a.getDeliveryBeginTime() == null) return 1;
                if (b.getDeliveryBeginTime() == null) return -1;
                return a.getDeliveryBeginTime().compareTo(b.getDeliveryBeginTime());
            });

            tableView.getItems().setAll(transportList);
        } catch (Exception e) {
            e.printStackTrace();
            tableView.getItems().clear();
        }
    }

    @FXML
    void SetData(MouseEvent event) {
        TransportDto dto = tableView.getSelectionModel().getSelectedItem();
        if (dto != null) {
            textTransportId.setText(String.valueOf(dto.getTransportId()));
            cmdProductId.setValue(String.valueOf(dto.getProductId()));
            cmdVehicalNumber.setValue(dto.getVehicleNumber());
            cmdStatus.setValue(dto.getStatus());
            textLocation.setText(dto.getLocation());
            textQty.setText(String.valueOf(dto.getQuantity()));
            textDate.setValue(LocalDate.parse(dto.getTransportDate()));
            textInTime.setText(dto.getDeliveryBeginTime() != null ? dto.getDeliveryBeginTime().toString() : "");
            textOutTime.setText(dto.getDeliveryEndTime() != null ? dto.getDeliveryEndTime().toString() : "");


            if (dto.getDeliveryBeginTime() != null && dto.getDeliveryEndTime() != null) {
                try {
                    LocalTime inTime = dto.getDeliveryBeginTime();
                    LocalTime outTime = dto.getDeliveryEndTime();
                    textDeliveryDuration.setText(calculateWorkingHours(inTime, outTime));
                } catch (Exception e) {
                    textDeliveryDuration.setText("Invalid Time");
                }
            } else {
                textDeliveryDuration.setText("N/A");
            }
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        try {
            String transportId = textTransportId.getText().trim();
            String productId = cmdProductId.getValue();
            String vehicleNumber = cmdVehicalNumber.getValue();
            String status = cmdStatus.getValue();
            String location = textLocation.getText().trim();
            String quantityStr = textQty.getText().trim();
            LocalDate date = textDate.getValue();
            LocalTime inTime = textInTime.getText().isEmpty() ? null : LocalTime.parse(textInTime.getText());
            LocalTime outTime = textOutTime.getText().isEmpty() ? null : LocalTime.parse(textOutTime.getText());

            if (transportId.isEmpty() || productId == null || vehicleNumber == null || status == null ||
                    location.isEmpty() || quantityStr.isEmpty() || date == null) {
                showErrorMessage("Please fill all required fields.");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                showErrorMessage("Quantity must be a valid number.");
                return;
            }

            TransportDto dto = new TransportDto(
                    transportId,
                    productId,
                    vehicleNumber,
                    date.toString(),
                    inTime,
                    outTime,
                    quantity,
                    location,
                    status
            );
            if(status.equals("Delivered")){
                boolean isQtyUpdated = stockModel.reduceQty(productId,quantity);
                if(isQtyUpdated){
                    showSuccessMessage("Successfully updated stock record.");
                }else {
                    showErrorMessage("Failed to update stock record. Failed to add transport.");
                    return;
                }
            }

            boolean isSaved = transportModel.saveTransport(dto);
            if (isSaved) {
                showSuccessMessage("Transport added successfully.");
                loadTable();
                btnClearOnAction(null);
            } else {
                showErrorMessage("Failed to add transport.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error occurred while adding transport.");
        }
    }



    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        textTransportId.clear();
        textQty.clear();
        textLocation.clear();
        cmdProductId.setValue(null);
        cmdVehicalNumber.setValue(null);
        cmdStatus.setValue(null);
        textDate.setValue(LocalDate.now());
        textHours.setValue(null);
        textMinutes.setValue(null);
        textHours1.setValue(null);
        textMinutes1.setValue(null);
        textInTime.setText("");
        textOutTime.setText("");
        loadNextId();
        showSuccessMessage("Cleared all fields.");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String transportId = textTransportId.getText().trim();

        if (transportId.isEmpty()) {
            showErrorMessage("Select a transport record to delete.");
            return;
        }

        try {
            boolean isDeleted = transportModel.deleteTransport(transportId);
            if (isDeleted) {
                showSuccessMessage("Transport deleted successfully.");
                loadTable();
                btnClearOnAction(null);
            } else {
                showErrorMessage("Failed to delete transport.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error occurred while deleting.");
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            String transportId = textTransportId.getText().trim();
            String productId = cmdProductId.getValue();
            String vehicleNumber = cmdVehicalNumber.getValue();
            String status = cmdStatus.getValue();
            String location = textLocation.getText().trim();
            String quantityStr = textQty.getText().trim();
            LocalDate date = textDate.getValue();
            LocalTime inTime = textInTime.getText().isEmpty() ? null : LocalTime.parse(textInTime.getText());
            LocalTime outTime = textOutTime.getText().isEmpty() ? null : LocalTime.parse(textOutTime.getText());

            if (transportId.isEmpty() || productId == null || vehicleNumber == null || status == null ||
                    location.isEmpty() || quantityStr.isEmpty() || date == null) {
                showErrorMessage("Please fill all required fields.");
                return;
            }

            int quantity = Integer.parseInt(quantityStr);

            TransportDto dto = new TransportDto(
                    transportId,
                    productId,
                    vehicleNumber,
                    date.toString(),
                    inTime,
                    outTime,
                    quantity,
                    location,
                    status
            );
            if(status.equals("Delivered")){
                boolean isQtyUpdated = stockModel.reduceQty(productId,quantity);
                if(isQtyUpdated){
                    showSuccessMessage("Successfully updated stock record.");
                }else {
                    showErrorMessage("Failed to update stock record. Failed to update transport.");
                    return;
                }
            }

            boolean isUpdated = transportModel.updateTransport(dto);
            if (isUpdated) {
                showSuccessMessage("Transport updated successfully.");
                loadTable();
                btnClearOnAction(null);
            } else {
                showErrorMessage("Failed to update transport.");
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Quantity must be numeric.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error occurred while updating transport.");
        }
    }


    @FXML
    public void onKeyHours(KeyEvent keyEvent) {
        textMinutes.requestFocus();

    }

    @FXML
    public void onKeyMinutes(KeyEvent keyEvent) {
        if (textHours.getValue() != null && textMinutes.getValue() != null) {
            textInTime.setText(textHours.getValue() + ":" + textMinutes.getValue());
        }
    }

    @FXML
    public void onKeyHours1(KeyEvent keyEvent) {
        textMinutes1.requestFocus();
    }

    @FXML
    public void onKeyMinutes1(KeyEvent keyEvent) {
        if (textHours1.getValue() != null && textMinutes1.getValue() != null) {
            textOutTime.setText(textHours1.getValue() + ":" + textMinutes1.getValue());
        }
    }

    private String calculateWorkingHours(LocalTime inTime, LocalTime outTime) {
        try {
            Duration duration = Duration.between(inTime, outTime);
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            return String.format("%02d:%02d", hours, minutes);
        } catch (Exception e) {
            e.printStackTrace();
            return "00:00";
        }
    }

    private void showErrorMessage(String message) {
        textError.setStyle("-fx-text-fill: red;");
        textError.setText(message);
    }

    private void showSuccessMessage(String message) {
        textError.setStyle("-fx-text-fill: green;");
        textError.setText(message);
    }


}
