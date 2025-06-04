package com.lankaice.project.controller;

import com.lankaice.project.dto.*;
import com.lankaice.project.dto.tm.ProductTM;
import com.lankaice.project.model.*;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ProductPageController implements Initializable {

    @FXML
    private ImageView editPrice1, editPrice2, editPrice3, editPrice4;
    @FXML
    private Label Name1, Name2, Name3, Name4;
    @FXML
    private Label Price1, Price2, Price3, Price4;
    @FXML
    private Button btnAdd, btnClear, btnConfirm;
    @FXML
    private ChoiceBox<String> textCustomer, choiceBoxPay, txtVehicleNo;
    @FXML
    private TableColumn<ProductTM, String> colProduct;
    @FXML
    private TableColumn<ProductTM, Double> colPrice;
    @FXML
    private TableColumn<ProductTM, Integer> colQty;
    @FXML
    private TableColumn<ProductTM, Double> colDiscount;
    @FXML
    private Label discountLabel, itemsCountLabel, lblBalance, lblCode, lblProduct, subtotalLabel, totalLabel;
    @FXML
    private TextField txtDiscount, txtDiscription, txtPaid, txtQty, lblPrice;
    @FXML
    private TableView<ProductTM> tableProduct;
    @FXML
    private Label lblOrderId;

    private final ObservableList<ProductTM> productList = FXCollections.observableArrayList();
    private final CustomerModel customerModel = new CustomerModel();
    private final OrdersModel ordersModel = new OrdersModel();
    private final VehicleModel vehicleModel = new VehicleModel();
    private final ProductModel productModel = new ProductModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserDto user = Session.getCurrentUser();
        boolean isAdmin = user.getRole().equalsIgnoreCase("admin");

        editPrice1.setVisible(isAdmin);
        editPrice2.setVisible(isAdmin);
        editPrice3.setVisible(isAdmin);
        editPrice4.setVisible(isAdmin);
        lblPrice.setEditable(false);

        resetPage();
        initChoiceBoxes();
        setupTableColumns();
        loadProductLabels();
    }

    private void initChoiceBoxes() {
        choiceBoxPay.setItems(FXCollections.observableArrayList("Cash", "Card", "Check"));
        choiceBoxPay.getSelectionModel().select("Cash");

        try {
            List<CustomerDto> customers = customerModel.getAllCustomers();
            List<String> customerNames = new ArrayList<>();
            for (CustomerDto c : customers) {
                customerNames.add(c.toString()); // "C001 Kalana Methsara"
            }

            List<String> vehicles = vehicleModel.getActiveVehicle();

            textCustomer.setItems(FXCollections.observableArrayList(customerNames));
            if (!customerNames.isEmpty()) {
                textCustomer.getSelectionModel().selectFirst();
            }

            txtVehicleNo.setItems(FXCollections.observableArrayList(vehicles));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load customers or vehicles!");
        }
    }

    private void setupTableColumns() {
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
    }

    @FXML
    void onActionAdd(ActionEvent event) {
        if (lblCode.getText().equals("Code")) {
            showAlert(Alert.AlertType.WARNING, "Please select a product.");
            return;
        }

        try {
            String productId = lblCode.getText();  // ✅ Correct!
            String product = lblProduct.getText();
            double price = Double.parseDouble(lblPrice.getText());
            int qty = Integer.parseInt(txtQty.getText());
            double discount = txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText());

            // Check if item is already in the cart
            for (ProductTM productTM : productList) {
                if (productTM.getProductId().equals(productId)) {
                    int newQty = productTM.getQty() + qty;

                  /*  if (newQty > itemStockQty) {
                        new Alert(Alert.AlertType.ERROR, "Not Enough Item Quantity", ButtonType.OK).show();
                        return;
                    }*/
                    double discount1 = productTM.getDiscount() + discount;

                    productTM.setQty(newQty);
                    productTM.setDiscount(discount1);
                    tableProduct.refresh(); // Refresh the table after updating
                    txtQty.clear();
                    txtDiscount.clear();
                    clearFields();
                    updateSummaryLabels();
                    return;
                }
            }


            ProductTM item = new ProductTM(productId,product, price, qty, discount);
            productList.add(item);
            tableProduct.setItems(productList);

            clearFields();
            updateSummaryLabels();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid quantity or discount format.");
        }
    }

    @FXML
    void onActionClear(ActionEvent event) {
        resetPage();
    }

    private void clearFields() {
        lblCode.setText("Code");
        lblProduct.setText("Product");
        lblPrice.setText("0.00");
        txtQty.clear();
        txtDiscount.clear();
    }

    private void updateSummaryLabels() {
        Set<String> uniqueProducts = new HashSet<>();
        double subtotal = 0, discount = 0;

        for (ProductTM item : productList) {
            uniqueProducts.add(item.getProductName());
            subtotal += item.getPrice() * item.getQty();
            discount += item.getDiscount();
        }

        itemsCountLabel.setText(String.valueOf(uniqueProducts.size()));
        subtotalLabel.setText(String.format("%.2f", subtotal));
        discountLabel.setText(String.format("%.2f", discount));
        totalLabel.setText(String.format("%.2f", subtotal - discount));
    }

    @FXML
    void onKeyBalance(KeyEvent keyEvent) {
        try {
            double paid = txtPaid.getText().isEmpty() ? 0 : Double.parseDouble(txtPaid.getText());
            double total = Double.parseDouble(totalLabel.getText());
            lblBalance.setText(String.format("%.2f", paid - total));
        } catch (NumberFormatException e) {
            lblBalance.setText("0.00");
        }
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
        lblPrice.setText(priceText.replace("Rs", "").trim());
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        alert.show();
    }

    private void loadProductLabels() {
        try {
            List<ProductDto> products = productModel.getAllProducts();
            if (products.size() >= 4) {
                Name1.setText(products.get(0).getName());
                Price1.setText(String.valueOf(products.get(0).getPricePerUnit()));
                Name2.setText(products.get(1).getName());
                Price2.setText(String.valueOf(products.get(1).getPricePerUnit()));
                Name3.setText(products.get(2).getName());
                Price3.setText(String.valueOf(products.get(2).getPricePerUnit()));
                Name4.setText(products.get(3).getName());
                Price4.setText(String.valueOf(products.get(3).getPricePerUnit()));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load product labels!");
            e.printStackTrace();
        }
    }

    @FXML
    public void onActionComfirm(ActionEvent actionEvent) {
        if (productList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Add items to cart before placing order.");
            return;
        }

        if (textCustomer.getValue() == null || textCustomer.getValue().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please select a customer.");
            return;
        }

        try {

            int orderId = Integer.parseInt(lblOrderId.getText());
            String selectedCustomer = textCustomer.getValue();
            String customerId = selectedCustomer.substring(0, 4);
            String description = txtDiscription.getText();
            String vehicleNumber = txtVehicleNo.getValue();
            String paymentMethod = choiceBoxPay.getValue();
            String date = LocalDate.now().toString();
            String time = LocalTime.now().toString();
            double total = Double.parseDouble(totalLabel.getText());

            ArrayList<OrderDetailsDto> cartList = new ArrayList<>();
            for (ProductTM item : productList) {
                cartList.add(new OrderDetailsDto(orderId, item.getProductId(), item.getQty(), item.getPrice(), item.getDiscount()));
            }
            OrdersDto ordersDto = new OrdersDto(orderId, customerId, date, time, description, vehicleNumber, total, cartList);
            boolean isPlaced = ordersModel.placeOrder(ordersDto);

            if (isPlaced) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully!");
                alert.initStyle(StageStyle.UNDECORATED);
                alert.getDialogPane().setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
                alert.show();
                resetPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Order placement failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error placing order.");
        }
    }

    private void resetPage() {
        clearFields();
        productList.clear();
        tableProduct.getItems().clear();
        txtDiscription.clear();
        txtPaid.clear();
        lblBalance.setText("");

        itemsCountLabel.setText("0");
        subtotalLabel.setText("0.00");
        discountLabel.setText("0.00");
        totalLabel.setText("0.00");

        loadNextOrderId();
        initChoiceBoxes();
    }

    private void loadNextOrderId() {
        try {
            int nextId = ordersModel.getLastOrderId() + 1;
            lblOrderId.setText(String.valueOf(nextId));
        } catch (Exception e) {
            lblOrderId.setText("1001");
        }
    }

    public void onActionMode(ActionEvent actionEvent) {
    }

    public void onActionExtra(ActionEvent actionEvent) {
    }

    public void addLable() {
        try {
            List<ProductDto> products = productModel.getAllProducts();
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to change the price?", ButtonType.YES, ButtonType.NO);
            alert.initStyle(StageStyle.UNDECORATED);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setStyle("-fx-border-color: orange; -fx-border-width: 2px;");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                boolean isUpdated = productModel.updateProductPrice(code, newPrice);
                if (isUpdated) {
                    lblPrice.setEditable(false);
                    addLable(); // Refresh product labels
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to update product price.");
                }
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid price value!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error occurred while updating price.");
        }
    }


    public void onEditPrice1(MouseEvent mouseEvent) {
        lblPrice.setEditable(true);
        setProductDetails("I001", Name1.getText(), Price1.getText());
    }

    public void onEditPrice2(MouseEvent mouseEvent) {
        lblPrice.setEditable(true);
        setProductDetails("I002", Name2.getText(), Price2.getText());
    }

    public void onEditPrice3(MouseEvent mouseEvent) {
        lblPrice.setEditable(true);
        setProductDetails("I003", Name3.getText(), Price3.getText());
    }

    public void onEditPrice4(MouseEvent mouseEvent) {
        lblPrice.setEditable(true);
        setProductDetails("I004", Name4.getText(), Price4.getText());
    }

}
