package com.lankaice.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

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
            new Alert(Alert.AlertType.ERROR, "Page not found", ButtonType.OK).show();
            e.printStackTrace();
        }
    }


    @FXML
    void superAdVerifyOnAction(ActionEvent actionEvent) {
        String pwd = pwdFieldVerify.getText();
        try{
            if(pwd.equals("2770")){
                navigateTo("/view/AdminManagePage.fxml");
            }else{
                new Alert(Alert.AlertType.ERROR, "It seems you are not super admin").show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
