package com.lankaice.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
<<<<<<< HEAD
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
=======
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805

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
<<<<<<< HEAD
            showAlert(Alert.AlertType.ERROR, "Page not found");
=======
            new Alert(Alert.AlertType.ERROR, "Page not found", ButtonType.OK).show();
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
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
<<<<<<< HEAD
                showAlert(Alert.AlertType.ERROR, "It seems you are not super admin");
=======
                new Alert(Alert.AlertType.ERROR, "It seems you are not super admin").show();
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
<<<<<<< HEAD
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);

        DialogPane dialogPane = alert.getDialogPane();
       dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        alert.show();
    }

=======
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
}
