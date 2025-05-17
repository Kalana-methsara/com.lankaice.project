package com.lankaice.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

public class VerifySuperAdminController {

    @FXML
    private PasswordField pwdFieldVerify;

    @FXML
    private AnchorPane subAnchorPane;

    public void navigateTo(String path) {
        try {
            subAnchorPane.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(subAnchorPane.widthProperty());
            anchorPane.prefHeightProperty().bind(subAnchorPane.heightProperty());
            subAnchorPane.getChildren().add(anchorPane);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Page not found");
            e.printStackTrace();
        }
    }

    @FXML
    void superAdVerifyOnAction(ActionEvent actionEvent) {
        String pwd = pwdFieldVerify.getText();
        try {
            if (pwd.equals("2770")) {
                navigateTo("/view/AdminManagePage.fxml");
            } else {
                showAlert(Alert.AlertType.ERROR, "It seems you are not super admin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        alert.show();
    }
}
