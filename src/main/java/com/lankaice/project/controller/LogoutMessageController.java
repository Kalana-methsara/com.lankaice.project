package com.lankaice.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LogoutMessageController {
    public void onCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onConfirm(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();

        AnchorPane ancUserView= null; //= UserViewController.ancUserView;

        ancUserView.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        ancUserView.getChildren().add(parent);
    }
}
