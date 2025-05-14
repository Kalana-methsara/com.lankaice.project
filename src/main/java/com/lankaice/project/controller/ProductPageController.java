package com.lankaice.project.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductPageController implements Initializable {

    @FXML
    private Label Name1;

    @FXML
    private Label Name2;

    @FXML
    private Label Name3;

    @FXML
    private Label Name4;

    @FXML
    private Label Price1;

    @FXML
    private Label Price2;

    @FXML
    private Label Price3;

    @FXML
    private Label Price4;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnComfirm;

    @FXML
    private ComboBox<String> choiceBox1;

    @FXML
    private ChoiceBox<String> choiceBoxPay;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colProduct;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private Label discountLabel;

    @FXML
    private Label itemsCountLabel;

    @FXML
    private Label lblBalance;

    @FXML
    private Label lblCode;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblProduct;

    @FXML
    private Label subtotalLabel;

    @FXML
    private TableView<?> tableProduct;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField txtDiscount;

    @FXML
    private TextField txtDiscription;

    @FXML
    private TextField txtPaid;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtVehicleNo;

    @FXML
    void onActionAdd(ActionEvent event) {

    }

    @FXML
    void onActionClear(ActionEvent event) {

    }

    @FXML
    void onActionComfirm(ActionEvent event) {

    }

    @FXML
    void onActionExtra(ActionEvent event) {

    }

    @FXML
    void onActionMode(ActionEvent event) {

    }

    @FXML
    void onAddItem1(ActionEvent event) {

    }

    @FXML
    void onAddItem2(ActionEvent event) {

    }

    @FXML
    void onAddItem3(ActionEvent event) {

    }

    @FXML
    void onAddItem4(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxPay.setItems(FXCollections.observableArrayList("Cash", "Card", "Check"));
        choiceBoxPay.getSelectionModel().select("Cash");

        choiceBox1.setItems(FXCollections.observableArrayList("Cash", "Card", "Check"));

    }

}
