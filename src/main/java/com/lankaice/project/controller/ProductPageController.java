package com.lankaice.project.controller;

<<<<<<< HEAD
import com.lankaice.project.dto.tm.ProductTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
=======
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;

import java.net.URL;
import java.util.ResourceBundle;
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805

public class ProductPageController implements Initializable {

    @FXML
<<<<<<< HEAD
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
    private ChoiceBox<String> choiceBox1;

    @FXML
    private ChoiceBox<String> choiceBoxPay;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colProduct;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colDiscount;

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
    private TableView<ProductTM> tableProduct;

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

    private final ObservableList<ProductTM> productList = FXCollections.observableArrayList();


    @FXML
    void onActionAdd(ActionEvent event) {
        String code = lblCode.getText();
        if(code.equals("Code")){
            showAlert(Alert.AlertType.ERROR,"Please Add item!");
return;
        }
        try {
            String product = lblProduct.getText();
            double price = Double.parseDouble(lblPrice.getText());
            int qty = Integer.parseInt(txtQty.getText());
            double discount = txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText());

            ProductTM item = new ProductTM(product, price, qty, discount);
            productList.add(item);

            clearField();

            colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
            colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

            tableProduct.setItems(productList);

            updateSummaryLabels();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input!");
        }
    }
    private void updateSummaryLabels() {
        Set<String> uniqueProducts = new HashSet<>();
        double subtotal = 0;
        double totalDiscount = 0;

        for (ProductTM item : productList) {
            uniqueProducts.add(item.getProduct());
            subtotal += item.getPrice() * item.getQty();
            totalDiscount += item.getDiscount();
        }

        double total = subtotal - totalDiscount;

        itemsCountLabel.setText(String.valueOf(uniqueProducts.size()));
        subtotalLabel.setText(String.format("%.2f", subtotal));
        discountLabel.setText(String.format("%.2f", totalDiscount));
        totalLabel.setText(String.format("%.2f", total));
    }



    @FXML
    void onActionClear(ActionEvent event) {
        productList.clear();
        updateSummaryLabels();
        txtDiscription.clear();
        txtVehicleNo.clear();
        choiceBoxPay.setItems(FXCollections.observableArrayList("Cash", "Card", "Check"));
        choiceBoxPay.getSelectionModel().select("Cash");
        txtPaid.clear();
        lblBalance.setText("");
        choiceBox1.setItems(FXCollections.observableArrayList("%002  Kalana methsara", "%003  Savindu Navanjana", "%004 Akila Abesekara"));
        choiceBox1.getSelectionModel().select("%001  New Customer");
        tableProduct.getItems().clear();
        itemsCountLabel.setText("0");
        subtotalLabel.setText("0.00");
        totalLabel.setText("0.00");
        discountLabel.setText("0.00");


    }
public void clearField(){
    lblCode.setText("Code");
    lblProduct.setText("Product");
    lblPrice.setText("560.00");
    txtQty.clear();
    txtDiscount.clear();
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
        lblCode.setText("I001");
        lblProduct.setText(Name1.getText());
        String priceText = Price1.getText();

        String numberOnly = priceText.replaceAll("[^0-9.]", "");
      double  price = Double.parseDouble(numberOnly)*1000;

        lblPrice.setText(String.format("%.2f", price));
    }


    @FXML
    void onAddItem2(ActionEvent event) {
        lblCode.setText("I002");
        lblProduct.setText(Name2.getText());
        String priceText = Price2.getText();

        String numberOnly = priceText.replaceAll("[^0-9.]", "");
        double  price = Double.parseDouble(numberOnly)*1000;

        lblPrice.setText(String.format("%.2f", price));
    }

    @FXML
    void onAddItem3(ActionEvent event) {
        lblCode.setText("I001");
        lblProduct.setText(Name3.getText());
        String priceText = Price3.getText();

        String numberOnly = priceText.replaceAll("[^0-9.]", "");
        double  price = Double.parseDouble(numberOnly)*1000;

        lblPrice.setText(String.format("%.2f", price));
    }

    @FXML
    void onAddItem4(ActionEvent event) {
        lblCode.setText("I001");
        lblProduct.setText(Name4.getText());
        String priceText = Price4.getText();

        String numberOnly = priceText.replaceAll("[^0-9.]", "");
        double  price = Double.parseDouble(numberOnly)*1000;

        lblPrice.setText(String.format("%.2f", price));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxPay.setItems(FXCollections.observableArrayList("Cash", "Card", "Check"));
        choiceBoxPay.getSelectionModel().select("Cash");

        choiceBox1.setItems(FXCollections.observableArrayList("%002  Kalana methsara", "%003  Savindu Navanjana", "%004 Akila Abesekara"));
        choiceBox1.getSelectionModel().select("%001  New Customer");

    }

    public void onKeyBalance(KeyEvent keyEvent) {
        try {
            double paidAmount = txtPaid.getText().isEmpty() ? 0 : Double.parseDouble(txtPaid.getText());
            double total = Double.parseDouble(totalLabel.getText());

            double balance = paidAmount - total;
            lblBalance.setText(String.format("%.2f", balance));
        } catch (NumberFormatException e) {
            lblBalance.setText("0.00");
        }
    }
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        alert.show();
    }

=======
    private ChoiceBox<String> choiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      choiceBox.setTooltip(new Tooltip("SELECT CREDITOR"));

    }
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
}
