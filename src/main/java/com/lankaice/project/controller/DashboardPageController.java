package com.lankaice.project.controller;

import com.lankaice.project.dto.*;
import com.lankaice.project.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

// DTO classes (You need to create these)
/*import com.lankaice.project.dto.DriverDto;*/
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;

public class DashboardPageController implements Initializable {

    @FXML
    private TableColumn<VehicleDto, String> colStatus;

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
    private TableColumn<PendingOrderDto, Void> colAction;

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

    private final OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
    private final StockModel stockModel = new StockModel();
    private final TransportModel transportModel = new TransportModel();
    private final OrdersModel ordersModel = new OrdersModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
           // openAddAdminWindow();
            loadVehicleTable();
            loadDashboardStats();
            pendingOrderToday();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
 /*   public void openAddAdminWindow() throws IOException {
        UserDto user = Session.getCurrentUser();
        Parent rootNode = FXMLLoader.load(getClass().getResource("/view/AddAdmin.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
*/

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
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAction.setCellFactory(column -> new TableCell<>() {
            private final Button btnPending = new Button("COMPLETED");
            private final Button btnCompleted = new Button("CANCELLED");

            {
                btnPending.setStyle("-fx-background-color: white; -fx-text-fill: green; -fx-border-color: blue; -fx-font-weight: bold;");
                btnCompleted.setStyle("-fx-background-color: white; -fx-text-fill: red; -fx-border-color: blue; -fx-font-weight: bold;");

                btnPending.setOnAction(event -> {
                    PendingOrderDto order = getTableView().getItems().get(getIndex());
                    if (!"COMPLETED".equals(order.getStatus())) {
                        boolean updated = false;
                        try {
                            updated = ordersModel.updateOrderStatus(order.getOrderId(),order.getProductName(), "COMPLETED");
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        if (updated) {
                            order.setStatus("COMPLETED");
                            showAlert(Alert.AlertType.INFORMATION, "Order marked as COMPLETED");
                            tableOrder.refresh();
                        }
                    }
                });

                btnCompleted.setOnAction(event -> {
                    PendingOrderDto order = getTableView().getItems().get(getIndex());
                    if (!"CANCELLED".equals(order.getStatus())) {
                        boolean updated = false;
                        try {
                            updated = ordersModel.updateOrderStatus(order.getOrderId(),order.getProductName(), "CANCELLED");
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        if (updated) {
                            order.setStatus("CANCELLED");
                            showAlert(Alert.AlertType.INFORMATION, "Order marked as CANCELLED");
                            tableOrder.refresh();
                        }
                    }
                });

            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, btnPending, btnCompleted);
                    buttons.setStyle("-fx-alignment: CENTER;");
                    setGraphic(buttons);
                }
            }
        });

        try {
            OrdersModel ordersModel = new OrdersModel();
            ArrayList<PendingOrderDto> pendingOrders = ordersModel.getPendingOrdersByDate(LocalDate.now());
            ObservableList<PendingOrderDto> observableList = FXCollections.observableArrayList(pendingOrders);
            tableOrder.setItems(observableList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadDashboardStats() throws SQLException, ClassNotFoundException {
        int sale =orderDetailsModel.todaySale();
        lblSale.setText(String.valueOf(sale));
int production =stockModel.todayAddedStock();
        lblProduction.setText(String.valueOf(production));
        int transport=transportModel.getTodayTransportTotal();
        lblTransport.setText(String.valueOf(transport));
        int stock =stockModel.currentStock();
        lblCurrentStock.setText(String.valueOf(stock));
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
}
