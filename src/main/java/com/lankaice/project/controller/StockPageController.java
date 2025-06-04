package com.lankaice.project.controller;

import com.lankaice.project.dto.StockDto;
import com.lankaice.project.model.ProductModel;
import com.lankaice.project.model.StockModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class StockPageController implements Initializable {

    @FXML
    private Label txtId;

    @FXML
    private TableColumn<StockDto, String> colDate;

    @FXML
    private TableColumn<StockDto, Integer> colStockId;

    @FXML
    private TableColumn<StockDto, String> colProductId;

    @FXML
    private TableColumn<StockDto, String> colProductName;

    @FXML
    private TableColumn<StockDto, Integer> colQty;

    @FXML
    private TableColumn<StockDto, String> colTime;

    @FXML
    private TableView<StockDto> tableView;

    @FXML
    private ChoiceBox<String> textMonth;

    @FXML
    private TextField textProductName;

    @FXML
    private TextField textQty;

    @FXML
    private ComboBox<String> txtProductId;

    @FXML
    private TextField txtSearch;

    @FXML
    private Label txtYear;

    private final StockModel stockModel = new StockModel();
    private final ProductModel productModel = new ProductModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colStockId.setCellValueFactory(new PropertyValueFactory<>("stockId"));
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        textMonth.setItems(FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));
        textMonth.setValue(textMonth.getItems().get(LocalDate.now().getMonthValue() - 1));
        txtYear.setText(String.valueOf(LocalDate.now().getYear()));

        try {
            txtProductId.setItems(FXCollections.observableArrayList(productModel.getAllProductIds()));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load product IDs.");
        }

        txtProductId.setOnAction(this::onProductId);
        clearFields();
        loadStockData();
        textMonth.setOnAction(e -> loadStockForSelectedMonth());
    }

    @FXML
    void btnAddEmployeeOnAction(ActionEvent event) {
        if (txtProductId.getValue() == null || textProductName.getText().isEmpty() || textQty.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please fill all fields!");
            return;
        }

        String productId = txtProductId.getValue();
        String productName = textProductName.getText();
        int quantity;

        try {
            quantity = Integer.parseInt(textQty.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid quantity.");
            return;
        }

        try {
            boolean isAdded = stockModel.addStock(productId, productName, quantity, LocalDate.now(), LocalTime.now());
            if (isAdded) {
                showAlert(Alert.AlertType.INFORMATION, "Stock Added!");
                clearFields();
                loadStockData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to add stock.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error!");
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Stock ID is required.");
            return;
        }

        try {
            int stockId = Integer.parseInt(txtId.getText());
            String productId = txtProductId.getValue();
            String productName = textProductName.getText();
            int qty = Integer.parseInt(textQty.getText());

            StockDto existing = stockModel.getStockById(stockId);
            if (existing == null) {
                showAlert(Alert.AlertType.ERROR, "No stock found for this ID.");
                return;
            }

            boolean isUpdated = stockModel.updateStock(new StockDto(
                    stockId, productId, productName, qty, existing.getDate(), existing.getTime()
            ));

            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Stock updated.");
                clearFields();
                loadStockData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Update error.");
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (txtId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Stock ID is required.");
            return;
        }

        int stockId;
        try {
            stockId = Integer.parseInt(txtId.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid stock ID.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this stock?", ButtonType.YES, ButtonType.NO);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    boolean deleted = stockModel.deleteStock(String.valueOf(stockId));
                    if (deleted) {
                        clearFields();
                        loadStockData();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Failed to delete stock.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Deletion error.");
                }
            }
        });
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void setData(MouseEvent event) {
        StockDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        txtId.setText(String.valueOf(selected.getStockId()));
        txtProductId.setValue(selected.getProductId());
        textProductName.setText(selected.getProductName());
        textQty.setText(String.valueOf(selected.getQty()));

        try {
            LocalDate date = LocalDate.parse(selected.getDate());
            textMonth.setValue(date.getMonth().name().substring(0, 1).toUpperCase()
                    + date.getMonth().name().substring(1).toLowerCase());
            txtYear.setText(String.valueOf(date.getYear()));
        } catch (Exception ignored) {}
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String search = txtSearch.getText().trim();
        if (search.isEmpty()) {
            loadStockData();
            return;
        }

        try {
            List<StockDto> results = stockModel.searchStock(search);
            tableView.setItems(FXCollections.observableArrayList(results));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Search failed.");
        }
    }

    @FXML
    void loadStockForSelectedMonth() {
        String month = textMonth.getValue();
        String year = txtYear.getText();

        if (month == null || year.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please select month and year.");
            return;
        }

        try {
            List<StockDto> list = stockModel.getStockByMonthAndYear(year, month);
            tableView.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load data.");
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtSearch.clear();
        textQty.clear();
        txtProductId.setValue(null);
        textProductName.clear();
        textMonth.setValue(textMonth.getItems().get(LocalDate.now().getMonthValue() - 1));
        txtYear.setText(String.valueOf(LocalDate.now().getYear()));
        tableView.getSelectionModel().clearSelection();
        loadStockForSelectedMonth();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle(
                type == Alert.AlertType.INFORMATION
                        ? "-fx-border-color: blue; -fx-border-width: 2px;"
                        : "-fx-border-color: red; -fx-border-width: 2px;"
        );
        alert.show();
    }

    public void onProductId(ActionEvent event) {
        String id = txtProductId.getValue();
        if (id == null) return;
        try {
            String name = productModel.findNameById(id);
            textProductName.setText(name != null ? name : "Not Found");
        } catch (Exception e) {
            textProductName.setText("Error");
        }
    }

    private void loadStockData() {
        try {
            List<StockDto> allStock = stockModel.getAllStock();
            ObservableList<StockDto> list = FXCollections.observableArrayList(allStock);
            tableView.setItems(list);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnReportOnAction(ActionEvent event) {
        // Future: Add report generation logic here
    }
}
