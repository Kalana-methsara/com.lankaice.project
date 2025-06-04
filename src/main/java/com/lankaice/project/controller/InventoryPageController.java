package com.lankaice.project.controller;

import com.lankaice.project.dto.RawMaterialsDto;
import com.lankaice.project.dto.Session;
import com.lankaice.project.dto.SupplierDto;
import com.lankaice.project.dto.UserDto;
import com.lankaice.project.dto.tm.CartItemTM;
import com.lankaice.project.model.InventoryCartModel;
import com.lankaice.project.model.RawMaterialModel;
import com.lankaice.project.model.SupplierModel;
import com.lankaice.project.util.SendMail;
import javafx.application.Platform;
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
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;

import javax.mail.MessagingException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class InventoryPageController implements Initializable {

    @FXML
    private  ImageView editPrice;
    @FXML
    private ComboBox<RawMaterialsDto> cmbItemId;
    @FXML
    private ComboBox<SupplierDto> cmbSupplierId;
    @FXML
    private TableColumn<CartItemTM, String> colAction;
    @FXML
    private TableColumn<CartItemTM, String> colItemId;
    @FXML
    private TableColumn<CartItemTM, String> colUniteType;
    @FXML
    private TableColumn<CartItemTM, String> colName;
    @FXML
    private TableColumn<CartItemTM, Double> colPrice;
    @FXML
    private TableColumn<CartItemTM, Integer> colQuantity;
    @FXML
    private TableColumn<CartItemTM, String> colSupplierId;
    @FXML
    private TableColumn<CartItemTM, Double> colTotal;
    @FXML
    private Label lblItemName;
    @FXML
    private Label lblItemNetTotles;
    @FXML
    private TextField lblItemPrice;
    @FXML
    private Label lblItemQty;
    @FXML
    private Label lblSupplierEmail;
    @FXML
    private Label lblSupplierName;
    @FXML
    private TableView<CartItemTM> tblCart;
    @FXML
    private TextField txtAddToCartQty;

    private final ObservableList<CartItemTM> cartItemList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserDto user = Session.getCurrentUser();
        boolean isAdmin = user.getRole().equalsIgnoreCase("admin");
        editPrice.setVisible(isAdmin);
        lblItemPrice.setEditable(false);
        clearAll();
        loadSuppliers();
        initTable();
        tblCart.setItems(cartItemList);
        loadTable();
    }

    private void initTable() {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("materialId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUniteType.setCellValueFactory(new PropertyValueFactory<>("unitType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellFactory(column -> new TableCell<>() {
            private final Button btnCompleted = new Button("COMPLETED");
            private final Button btnUpdated = new Button("UPDATED");
            private final Button btnCancelled = new Button("CANCELLED");
            private final HBox box = new HBox(5, btnCompleted, btnUpdated, btnCancelled);

            {
                box.setStyle("-fx-alignment: center;");

                btnCancelled.setStyle("-fx-background-color: #f8d7da; -fx-text-fill: red; -fx-border-color: darkred;");
                btnUpdated.setStyle("-fx-background-color: #fff3cd; -fx-text-fill: orange; -fx-border-color: darkorange;");
                btnCompleted.setStyle("-fx-background-color: #d4edda; -fx-text-fill: green; -fx-border-color: darkgreen;");

                btnCompleted.setOnAction(e -> {
                    CartItemTM item = getTableView().getItems().get(getIndex());
                    try {
                        boolean isCompleted = RawMaterialModel.updateMaterialQtyAfterPurchase(String.valueOf(item.getMaterialId()), item.getQuantity());
                        boolean isCancelled = InventoryCartModel.removeCartItem(item.getCartId());
                        if (isCancelled && isCompleted) {
                            showAlert(Alert.AlertType.INFORMATION, "Cart item completed successfully.");
                            clearAll();
                        } else {
                            showAlert(Alert.AlertType.WARNING, "Failed to complete cart item.");
                        }
                    } catch (SQLException | ClassNotFoundException ex) {
                        showAlert(Alert.AlertType.ERROR, "Error completing cart item: " + ex.getMessage());
                    }
                });

                btnUpdated.setOnAction(e -> {
                    CartItemTM item = getTableView().getItems().get(getIndex());
                    String qtyText = txtAddToCartQty.getText();
                    if (qtyText == null || qtyText.trim().isEmpty()) {
                        showAlert(Alert.AlertType.WARNING, "Please enter a valid quantity.");
                        return;
                    }
                    try {
                        double newQty = Double.parseDouble(qtyText);
                        if (newQty <= 0) {
                            showAlert(Alert.AlertType.WARNING, "Quantity must be greater than zero.");
                            return;
                        }

                        boolean isUpdated = InventoryCartModel.updateCartItem(item,newQty);
                        if (isUpdated) {
                            showAlert(Alert.AlertType.INFORMATION, "Cart item updated successfully.");
                            loadTable();
                        } else {
                            showAlert(Alert.AlertType.WARNING, "Failed to update cart item.");
                        }
                    } catch (NumberFormatException ex) {
                        showAlert(Alert.AlertType.WARNING, "Please enter a valid number for quantity.");
                    } catch (SQLException | ClassNotFoundException ex) {
                        showAlert(Alert.AlertType.ERROR, "Error updating cart item: " + ex.getMessage());
                    }
                });

                btnCancelled.setOnAction(e -> {
                    CartItemTM item = getTableView().getItems().get(getIndex());
                    try {
                        boolean isCancelled = InventoryCartModel.removeCartItem(item.getCartId());
                        if (isCancelled) {
                            showAlert(Alert.AlertType.INFORMATION, "Cart item cancelled successfully.");
                            loadTable();
                        } else {
                            showAlert(Alert.AlertType.WARNING, "Failed to cancel cart item.");
                        }
                    } catch (SQLException | ClassNotFoundException ex) {
                        showAlert(Alert.AlertType.ERROR, "Error canceling cart item: " + ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    public void clearAll() {
        lblSupplierName.setText("");
        lblSupplierEmail.setText("");
        clearFields();
        updateNetTotalLabel();
        loadTable();
        cmbSupplierId.setValue(null);
        loadSuppliers();
    }

    public void loadTable() {
        try {
            ArrayList<CartItemTM> cartItems = new InventoryCartModel().getAll();
            tblCart.setItems(FXCollections.observableArrayList(cartItems));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error loading table data: " + e.getMessage());
        }
    }

    public void clearFields() {
        cmbItemId.setValue(null);
        lblItemName.setText("");
        lblItemPrice.setText("");
        lblItemQty.setText("");
        txtAddToCartQty.setText("");
        lblItemNetTotles.setText("");
    }

    private void loadSuppliers() {
        clearFields();
        try {
            List<SupplierDto> supplierList = SupplierModel.getAllSupplier();
            cmbSupplierId.setItems(FXCollections.observableArrayList(supplierList));
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading suppliers: " + e.getMessage());
        }
    }

    private void loadItemsForSupplier(String supplierId) {
        try {
            clearFields();
            List<RawMaterialsDto> itemList = RawMaterialModel.getItemsBySupplier(supplierId);
            cmbItemId.setItems(FXCollections.observableArrayList(itemList));
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading items: " + e.getMessage());
        }
    }

    @FXML
    void cmbSupplierOnAction(ActionEvent event) {
        clearFields();
        SupplierDto selectedSupplier = cmbSupplierId.getValue();
        if (selectedSupplier != null) {
            lblSupplierName.setText(selectedSupplier.getName());
            lblSupplierEmail.setText(selectedSupplier.getEmail());
            loadItemsForSupplier(selectedSupplier.getSupplierId());
        }
    }

    @FXML
    void cmbItemOnAction(ActionEvent event) {
        txtAddToCartQty.setText("");
        lblItemNetTotles.setText("");
        RawMaterialsDto selectedItem = cmbItemId.getValue();
        if (selectedItem != null) {
            lblItemName.setText(selectedItem.getName());
            lblItemPrice.setText(String.valueOf(selectedItem.getUnitCost()));
            lblItemQty.setText(String.valueOf(selectedItem.getQuantityAvailable()));
        }
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        RawMaterialsDto selectedItem = cmbItemId.getValue();
        SupplierDto selectedSupplier = cmbSupplierId.getValue();
        String qtyText = txtAddToCartQty.getText();

        if (selectedItem == null || selectedSupplier == null || qtyText == null || qtyText.isEmpty() || !qtyText.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Please select valid supplier, item, and quantity.");
            return;
        }

        int qty = Integer.parseInt(qtyText);
        double unitPrice = selectedItem.getUnitCost();
        double total = unitPrice * qty;

        CartItemTM cartItem = new CartItemTM(
                selectedSupplier.getSupplierId(),
                selectedItem.getMaterialId(),
                selectedItem.getName(),
                selectedItem.getUnitType(),
                unitPrice,
                qty,
                total
        );

        try {
            if (InventoryCartModel.saveCartItem(cartItem)) {
                cartItemList.add(cartItem);
                updateNetTotalLabel();
                new SendEmailSupplier(
                        lblSupplierEmail.getText(),
                        lblSupplierName.getText(),
                        lblItemName.getText(),
                        selectedItem.getMaterialId(),
                        qtyText
                ).start();
                clearAll();
                loadSuppliers();
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to save cart item: " + e.getMessage());
        }
    }

    public void onEditPrice(MouseEvent mouseEvent) {
        lblItemPrice.setEditable(true);

    }

    public void onSetPrice(ActionEvent actionEvent) {
        setPrice();
    }

    private void setPrice() {
        RawMaterialsDto selectedItem = cmbItemId.getValue();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.ERROR, "Please select an item first!");
            return;
        }

        try {
            double newPrice = Double.parseDouble(lblItemPrice.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to change the price?", ButtonType.YES, ButtonType.NO);
            alert.initStyle(StageStyle.UNDECORATED);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setStyle("-fx-border-color: orange; -fx-border-width: 2px;");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                boolean isUpdated = RawMaterialModel.updateMaterialPrice(selectedItem.getMaterialId(), newPrice);
                if (isUpdated) {
                    lblItemPrice.setEditable(false);
                    clearAll();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to update item price.");
                }
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid price value!");
        } catch (Exception e) {
            e.printStackTrace(); 
            showAlert(Alert.AlertType.ERROR, "Error occurred while updating price.");
        }
    }

    public class SendEmailSupplier extends Thread {
        private final String to;
        private final String supplierName;
        private final String itemName;
        private final int materialId;
        private final int quantity;

        public SendEmailSupplier(String to, String supplierName, String itemName, int materialId, String quantity) {
            this.to = to;
            this.supplierName = supplierName;
            this.itemName = itemName;
            this.materialId = materialId;
            this.quantity = Integer.parseInt(quantity);
        }

        @Override
        public void run() {
            String date = LocalDate.now().toString();
            String subject = "New Inventory Request - Lanka Ice Factory";
            String description = String.format(
                    "Dear %s,\n\n" +
                            "We kindly request the following raw materials for our production requirements at Lanka Ice Factory:\n\n" +
                            "• Material: %s\n" +
                            "• Item Name: %s\n" +
                            "• Quantity Required: %s\n" +
                            "• Preferred Delivery Date: %s\n\n" +
                            "Please confirm the availability and expected delivery timeline. If you require further details, feel free to contact us.\n\n" +
                            "Thank you for your continued support.\n\n" +
                            "Best regards,\n" +
                            "Kalana Methsara\n" +
                            "Inventory Manager – Lanka Ice Factory\n" +
                            "📞 0772133073\n" +
                            "📧 lankaice1@gmail.com",
                    supplierName, materialId, itemName, quantity, date
            );

            if (to.isEmpty() || subject.isEmpty() || description.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "All fields are required!");
                return;
            }

            if (!isValidEmail(to)) {
                showAlert(Alert.AlertType.ERROR, "Invalid email address format!");
                return;
            }

            try {
                SendMail.outMail(description, to, subject);
                showAlert(Alert.AlertType.INFORMATION, "Email sent successfully to " + to);
            } catch (MessagingException e) {
                showAlert(Alert.AlertType.ERROR, "Failed to send email: " + e.getMessage());
            }
        }

        private boolean isValidEmail(String email) {
            return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        }
    }

    private void updateNetTotalLabel() {
        double netTotal = cartItemList.stream()
                .mapToDouble(CartItemTM::getTotal)
                .sum();
        lblItemNetTotles.setText(String.format("%.2f", netTotal));
    }

    @FXML
    public void setNetTotal(KeyEvent keyEvent) {
        String qtyText = txtAddToCartQty.getText();
        String priceText = lblItemPrice.getText();

        if (qtyText == null || qtyText.isEmpty() || !qtyText.matches("\\d+")) {
            lblItemNetTotles.setText("0.00");
            return;
        }

        try {
            int cartQty = Integer.parseInt(qtyText);
            double itemPrice = Double.parseDouble(priceText);
            double netTotal = cartQty * itemPrice;
            lblItemNetTotles.setText(String.format("%.2f", netTotal));
        } catch (NumberFormatException e) {
            lblItemNetTotles.setText("0.00");
        }
    }


    private void showAlert(Alert.AlertType type, String message) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showAlert(type, message));
            return;
        }
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle(
                type == Alert.AlertType.INFORMATION
                        ? "-fx-border-color: blue; -fx-border-width: 2px;"
                        : "-fx-border-color: red; -fx-border-width: 2px;"
        );
        alert.show();
    }

    public void setData(MouseEvent mouseEvent) {
        CartItemTM selectedItem = tblCart.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cmbSupplierId.setValue(new SupplierDto(selectedItem.getSupplierId(), "", ""));
            cmbItemId.setValue(new RawMaterialsDto(selectedItem.getMaterialId(), selectedItem.getName(), "", 0.0, 0));
            lblItemName.setText(selectedItem.getName());
            lblItemPrice.setText(String.valueOf(selectedItem.getUnitPrice()));
            txtAddToCartQty.setText(String.valueOf(selectedItem.getQuantity()));
            lblItemNetTotles.setText(String.format("Rs. %.2f", selectedItem.getTotal()));
        }
    }

    public void onRefesh(MouseEvent mouseEvent) {
        clearAll();
    }


}