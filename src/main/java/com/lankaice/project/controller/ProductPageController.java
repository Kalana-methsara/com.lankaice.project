package com.lankaice.project.controller;

import com.lankaice.project.dto.ProductDto;
import com.lankaice.project.dto.Session;
import com.lankaice.project.dto.UserDto;
import com.lankaice.project.dto.tm.ProductTM;
import com.lankaice.project.model.ProductModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class ProductPageController implements Initializable {

    @FXML private ImageView editPrice1;
    @FXML private ImageView editPrice2;
    @FXML private ImageView editPrice3;
    @FXML private ImageView editPrice4;
    @FXML private Label Name1, Name2, Name3, Name4;
    @FXML private Label Price1, Price2, Price3, Price4;
    @FXML private Button btnAdd, btnClear, btnConfirm;
    @FXML private ChoiceBox<String> choiceBox1, choiceBoxPay;
    @FXML private TableColumn<ProductTM, String> colProduct;
    @FXML private TableColumn<ProductTM, Double> colPrice;
    @FXML private TableColumn<ProductTM, Integer> colQty;
    @FXML private TableColumn<ProductTM, Double> colDiscount;
    @FXML private Label discountLabel, itemsCountLabel, lblBalance, lblCode, lblProduct, subtotalLabel, totalLabel;
    @FXML private TextField txtDiscount, txtDiscription, txtPaid, txtQty, txtVehicleNo,lblPrice;
    @FXML private TableView<ProductTM> tableProduct;

    private final ObservableList<ProductTM> productList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        UserDto user = Session.getCurrentUser();
        String role = user.getRole();
        boolean isAdmin = role.equalsIgnoreCase("admin");

        editPrice1.setVisible(isAdmin);
        editPrice2.setVisible(isAdmin);
        editPrice3.setVisible(isAdmin);
        editPrice4.setVisible(isAdmin);
        lblPrice.setEditable(isAdmin);


        initChoiceBoxes();
        setupTableColumns();
        addLable();
    }

    private void initChoiceBoxes() {
        choiceBoxPay.setItems(FXCollections.observableArrayList("Cash", "Card", "Check"));
        choiceBoxPay.getSelectionModel().select("Cash");
        choiceBoxPay.setTooltip(new Tooltip("Select Payment Mode"));

        choiceBox1.setItems(FXCollections.observableArrayList("%001 New Customer", "%002 Kalana Methsara", "%003 Savindu Navanjana", "%004 Akila Abesekara"));
        choiceBox1.getSelectionModel().select("%001 New Customer");
        choiceBox1.setTooltip(new Tooltip("Select Customer"));
    }

    private void setupTableColumns() {
        colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
    }

    @FXML
    void onActionAdd(ActionEvent event) {
        String code = lblCode.getText();
        if (code.equals("Code")) {
            showAlert(Alert.AlertType.ERROR, "Please add an item first!");
            return;
        }
        try {
            String product = lblProduct.getText();
            double price = Double.parseDouble(lblPrice.getText());
            int qty = Integer.parseInt(txtQty.getText());
            double discount = txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText());

            ProductTM item = new ProductTM(product, price, qty, discount);
            productList.add(item);
            tableProduct.setItems(productList);

            clearField();
            updateSummaryLabels();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input! Check quantity or discount fields.");
        }
    }

    @FXML
    void onActionClear(ActionEvent event) {
        clearField();
        productList.clear();
        tableProduct.getItems().clear();
        txtDiscription.clear();
        txtVehicleNo.clear();
        txtPaid.clear();
        lblBalance.setText("");

        initChoiceBoxes();

        itemsCountLabel.setText("0");
        subtotalLabel.setText("0.00");
        totalLabel.setText("0.00");
        discountLabel.setText("0.00");
    }

    public void clearField() {
        lblCode.setText("Code");
        lblProduct.setText("Product");
        lblPrice.setText("0.00");
        txtQty.clear();
        txtDiscount.clear();
    }

    private void updateSummaryLabels() {
        Set<String> uniqueProducts = new HashSet<>();
        double subtotal = 0, totalDiscount = 0;

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
    public void onKeyBalance(KeyEvent keyEvent) {
        try {
            double paid = txtPaid.getText().isEmpty() ? 0 : Double.parseDouble(txtPaid.getText());
            double total = Double.parseDouble(totalLabel.getText());
            double balance = paid - total;
            lblBalance.setText(String.format("%.2f", balance));
        } catch (NumberFormatException e) {
            lblBalance.setText("0.00");
        }
    }

    @FXML
    void onActionConfirm(ActionEvent event) {
        // TODO: Implement billing confirmation logic
        showAlert(Alert.AlertType.INFORMATION, "Order confirmed!");
    }

    @FXML
    void onActionExtra(ActionEvent event) {
        // Optional extra logic
    }

    @FXML
    void onActionMode(ActionEvent event) {
        // Optional mode switch logic
    }

    @FXML
    void onAddItem1(ActionEvent event) {
        setProductDetails("I001", Name1.getText(), Price1.getText());
    }

    @FXML
    void onAddItem2(ActionEvent event) {
        setProductDetails("I002", Name2.getText(), Price2.getText());
    }

    @FXML
    void onAddItem3(ActionEvent event) {
        setProductDetails("I003", Name3.getText(), Price3.getText());
    }

    @FXML
    void onAddItem4(ActionEvent event) {
        setProductDetails("I004", Name4.getText(), Price4.getText());
    }

    private void setProductDetails(String code, String name, String priceText) {
        lblCode.setText(code);
        lblProduct.setText(name);
        lblPrice.setText(priceText.replace("Rs ", "").trim());
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        alert.show();
    }

    public void addLable() {
        try {
            List<ProductDto> products = ProductModel.getAllProducts();
            if (products.size() >= 4) {
                lblCode.setText("Code"); // Reset label

                Name1.setText(products.get(0).getName());
                Price1.setText(String.valueOf(products.get(0).getPricePerUnit()));

                Name2.setText(products.get(1).getName());
                Price2.setText(String.valueOf(products.get(1).getPricePerUnit()));

                Name3.setText(products.get(2).getName());
                Price3.setText(String.valueOf(products.get(2).getPricePerUnit()));

                Name4.setText(products.get(3).getName());
                Price4.setText(String.valueOf(products.get(3).getPricePerUnit()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load product data!");
            e.printStackTrace();
        }
    }

    public void onActionComfirm(ActionEvent actionEvent) {
    }

    public void onEditPrice1(MouseEvent mouseEvent) {
        setProductDetails("I001", Name1.getText(), Price1.getText());
    }
    public void onEditPrice2(MouseEvent mouseEvent) {
        setProductDetails("I002", Name2.getText(), Price2.getText());
    }
    public void onEditPrice3(MouseEvent mouseEvent) {
        setProductDetails("I003", Name3.getText(), Price3.getText());
    }
    public void onEditPrice4(MouseEvent mouseEvent) {
        setProductDetails("I004", Name4.getText(), Price4.getText());
    }
    @FXML
    void onSetPrice(ActionEvent event) {
            setPrice();

    }

    private void setPrice() {
        String code = lblCode.getText();
        if (code.equals("Code")) {
            showAlert(Alert.AlertType.ERROR, "Please select a product first!");
            return;
        }

        try {
            double newPrice = Double.parseDouble(lblPrice.getText());
            boolean updated = ProductModel.updateProductPrice(code, newPrice);

            if (updated) {
                switch (code) {
                    case "I001" -> Price1.setText(String.format("%.2f", newPrice));
                    case "I002" -> Price2.setText(String.format("%.2f", newPrice));
                    case "I003" -> Price3.setText(String.format("%.2f", newPrice));
                    case "I004" -> Price4.setText(String.format("%.2f", newPrice));
                }
                showAlert(Alert.AlertType.INFORMATION, "Price updated successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to update price in the database.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid price format!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database error while updating price!");
        }
    }



}
