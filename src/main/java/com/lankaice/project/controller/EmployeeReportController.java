package com.lankaice.project.controller;

import com.lankaice.project.model.EmployeeModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeReportController implements Initializable {

    @FXML
    private AnchorPane employeeReport;

    @FXML
    private Label lblCashier;

    @FXML
    private Label lblDriver;

    @FXML
    private Label lblManager;

    @FXML
    private Label lblSupervisor;

    @FXML
    private Label lblWorker;

    @FXML
    void btnClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    loadEmployeeCounts();
    }
    private void loadEmployeeCounts() {
        try {
            lblManager.setText(String.valueOf(EmployeeModel.getEmployeeCountByRole("Manager")));
            lblCashier.setText(String.valueOf(EmployeeModel.getEmployeeCountByRole("Cashier")));
            lblDriver.setText(String.valueOf(EmployeeModel.getEmployeeCountByRole("Driver")));
            lblSupervisor.setText(String.valueOf(EmployeeModel.getEmployeeCountByRole("Supervisor")));
            lblWorker.setText(String.valueOf(EmployeeModel.getEmployeeCountByRole("Worker")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
