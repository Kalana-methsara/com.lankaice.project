package com.lankaice.project.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductPageController implements Initializable {

    @FXML
    private ChoiceBox<String> choiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      choiceBox.setTooltip(new Tooltip("SELECT CREDITOR"));

    }
}
