package com.lankaice.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class RegisterPageController {

    @FXML
    private Pane ancRegister;

    public void navigateTo(String path) {
        try {
            ancRegister.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancRegister.widthProperty());
            anchorPane.prefHeightProperty().bind(ancRegister.heightProperty());
            ancRegister.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found", ButtonType.OK).show();
            e.printStackTrace();
        }
    }
    public void onSupplierReg(MouseEvent mouseEvent) {
        navigateTo("/view/SupplierPage.fxml");
    }


    public void onCustomerReg(MouseEvent mouseEvent) {
        navigateTo("/view/CustomerPage.fxml");

    }

    public void onEmployeeReg(MouseEvent mouseEvent) {
        navigateTo("/view/EmployeePage.fxml");

    }

    public void onAttendance(MouseEvent mouseEvent) {
        navigateTo("/view/AttendancePage.fxml");
    }
}
