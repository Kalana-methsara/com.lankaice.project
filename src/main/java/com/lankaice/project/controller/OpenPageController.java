package com.lankaice.project.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class OpenPageController {

    @FXML
    private AnchorPane ancOpenPage;

    public void onLoadLogin(MouseEvent mouseEvent) throws IOException {
        ancOpenPage.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        ancOpenPage.getChildren().add(parent);
    }
}
