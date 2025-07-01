package com.lankaice.project.controller;

import com.lankaice.project.dto.SalaryDto;
import com.lankaice.project.model.SalaryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SalaryPageController implements Initializable {

    public TextField textBasicAmount;
    public TextField textBonus;
    public TextField textDeducation;
    public Label textNetAmount;
    public ChoiceBox<String> txtStatus;

    @FXML private Label txtYear;
    @FXML private ChoiceBox<String> textMonth;
    @FXML private TableColumn<SalaryDto, String> colStatus;
    @FXML private TableColumn<SalaryDto, Double> colBasicAmount;
    @FXML private TableColumn<SalaryDto, Double> colBonus;
    @FXML private TableColumn<SalaryDto, Double> colDeducation;
    @FXML private TableColumn<SalaryDto, String> colEmId;
    @FXML private TableColumn<SalaryDto, String> colName;
    @FXML private TableColumn<SalaryDto, Double> colNetAmount;
    @FXML private TableView<SalaryDto> tableView;
    @FXML private TextField txtSearch;

    private SalaryDto selectedSalary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textMonth.setItems(FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));
        txtStatus.setItems(FXCollections.observableArrayList("PENDING", "COMPLETED", "CANCELLED"));
        txtYear.setText(String.valueOf(LocalDate.now().getYear()));
        textMonth.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);

        clearFields();
        clearFields();
        try {
            boolean isDeleted = new SalaryModel().deleteAllSalary();
            if (isDeleted) {
                showAlert(Alert.AlertType.INFORMATION, "All salary records deleted successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "No salary records found to delete.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        btnGenerateSalaryOnAction();


    }

    public void btnGenerateSalaryOnAction() {
        int selectedMonth = textMonth.getSelectionModel().getSelectedIndex() + 1;
        int year = Integer.parseInt(txtYear.getText());

        try {
            boolean isGenerated = new SalaryModel().generateMonthlySalaries(selectedMonth, year);
            if (isGenerated) {
                showAlert(Alert.AlertType.INFORMATION, "Salary records generated successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Salary records already exist or no employees found.");
            }
            loadAllSalary();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error generating salary records.");
        }
    }


    private void loadAllSalary() {
        colEmId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colBasicAmount.setCellValueFactory(new PropertyValueFactory<>("basicSalary"));
        colBonus.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        colDeducation.setCellValueFactory(new PropertyValueFactory<>("deduction"));
        colNetAmount.setCellValueFactory(new PropertyValueFactory<>("netSalary"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        try {
            SalaryModel salaryModel = new SalaryModel();
            ArrayList<SalaryDto> salaryDtos = salaryModel.getAllSalaries(); // fixed method name
            ObservableList<SalaryDto> list = FXCollections.observableArrayList(salaryDtos);
            tableView.setItems(list);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load salary data.");
        }
    }

    @FXML
    void setData(MouseEvent event) {
        selectedSalary = tableView.getSelectionModel().getSelectedItem();
        if (selectedSalary != null) {
            textBasicAmount.setText(String.valueOf(selectedSalary.getBasicSalary()));
            textBonus.setText(String.valueOf(selectedSalary.getBonus()));
            textDeducation.setText(String.valueOf(selectedSalary.getDeduction()));
            textNetAmount.setText(String.valueOf(selectedSalary.getNetSalary()));
            txtStatus.setValue(selectedSalary.getStatus());
            textMonth.getSelectionModel().select(selectedSalary.getPayMonth() - 1);
            txtYear.setText(String.valueOf(selectedSalary.getPayYear()));
        }
    }

    @FXML
    public void btnDeleteEmployeeOnAction(ActionEvent event) {
        if (selectedSalary != null) {
            try {
                boolean isDeleted = new SalaryModel().deleteSalary(
                        selectedSalary.getEmployeeId(),
                        selectedSalary.getPayMonth(),
                        selectedSalary.getPayYear()
                );
                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Salary deleted successfully.");
                    loadAllSalary();
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Delete operation failed.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error during deletion.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Please select a salary record.");
        }
    }

    @FXML
    public void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtSearch.clear();
        textBasicAmount.clear();
        textBonus.clear();
        textDeducation.clear();
        textNetAmount.setText("");
        txtStatus.setValue(null);
        textMonth.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        txtYear.setText(String.valueOf(LocalDate.now().getYear()));
        selectedSalary = null;
        tableView.getSelectionModel().clearSelection();
        loadAllSalary();
    }

    @FXML
    public void btnUpdateEmployeeOnAction(ActionEvent event) {
        if (selectedSalary != null) {
            try {
                double basic = Double.parseDouble(textBasicAmount.getText());
                double bonus = Double.parseDouble(textBonus.getText());
                double deduction = Double.parseDouble(textDeducation.getText());
                double net = basic + bonus - deduction;

                String status = txtStatus.getValue();
                if (status == null || status.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Please select a valid status.");
                    return;
                }

                selectedSalary.setBasicSalary(basic);
                selectedSalary.setBonus(bonus);
                selectedSalary.setDeduction(deduction);
                selectedSalary.setNetSalary(net);
                selectedSalary.setStatus(status);

                boolean isUpdated = new SalaryModel().updateSalary(selectedSalary);
                if (isUpdated) {
                    textNetAmount.setText(String.valueOf(net));
                    showAlert(Alert.AlertType.INFORMATION, "Salary updated successfully.");
                    loadAllSalary();
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Update failed.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid input. Please enter numeric values.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "An unexpected error occurred.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Please select a record to update.");
        }
    }

    @FXML
    public void txtSearchOnAction(ActionEvent event) {
        loadAllSalary();
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (!keyword.isEmpty()) {
            ObservableList<SalaryDto> filteredList = FXCollections.observableArrayList();
            for (SalaryDto dto : tableView.getItems()) {
                if (dto.getEmployeeId().toLowerCase().contains(keyword) ||
                        dto.getEmployeeName().toLowerCase().contains(keyword)) {
                    filteredList.add(dto);
                }
            }
            tableView.setItems(filteredList);
        } else {
            loadAllSalary();
        }
    }

    @FXML
    public void btnViewAllOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(getClass().getResource("/view/ViewSalary.fxml"));
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(rootNode));
        stage.show();
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
