package com.lankaice.project.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class OpenPageController {

    @FXML
    private AnchorPane ancOpenPage;

    public void onLoadLogin(MouseEvent mouseEvent) throws IOException {
       navigateTo("/view/LoginPage.fxml");
    }
    public void navigateTo(String path) {
        try {
            ancOpenPage.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancOpenPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancOpenPage.heightProperty());
            ancOpenPage.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found", ButtonType.OK).show();
            e.printStackTrace();
        }
    }

}
