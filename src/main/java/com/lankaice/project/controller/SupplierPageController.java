package com.lankaice.project.controller;

import com.lankaice.project.dto.CustomerDto;
import com.lankaice.project.dto.Session;
import com.lankaice.project.dto.SupplierDto;
import com.lankaice.project.dto.UserDto;
import com.lankaice.project.model.CustomerModel;
import com.lankaice.project.model.SupplierModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierPageController implements Initializable {

    @FXML
    private TableColumn<SupplierDto, String> colAddress;

    @FXML
    private TableColumn<SupplierDto, String> colContact;

    @FXML
    private TableColumn<SupplierDto, String> colEmail;

    @FXML
    private TableColumn<SupplierDto, String> colNIC;

    @FXML
    private TableColumn<SupplierDto, String> colName;

    @FXML
    private TableColumn<SupplierDto, String> colSupplierId;

    @FXML
    private Label noOfSupplier;

    @FXML
    private TableView<SupplierDto> tableView;

    @FXML
    private TextField textAddress;

    @FXML
    private TextField textContact;

    @FXML
    private TextField textEmail;

    @FXML
    private Hyperlink textError;

    @FXML
    private TextField textName;

    @FXML
    private TextField textNic;

    @FXML
    private TextField textSearchSupplier;

    @FXML
    private TextField textSupplierId;

    @FXML
    void SetData(MouseEvent event) {
        SupplierDto supplierDto = tableView.getSelectionModel().getSelectedItem();
        if (supplierDto != null) {
            textSupplierId.setText(supplierDto.getSupplierId());
            textName.setText(supplierDto.getName());
            textNic.setText(supplierDto.getNic());
            textContact.setText(supplierDto.getContact());
            textEmail.setText(supplierDto.getEmail());
            textAddress.setText(supplierDto.getAddress());
        }

    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        SupplierDto supplierDto = createSupplierDtoFromInputs();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Are you sure you want to add this Supplier?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isAdded = new SupplierModel().addSupplier(supplierDto);
                if (isAdded) {
                    showSuccessMessage("Supplier added successfully!");
                    loadSupplierTable();
                    clearFields();
                    setGeneretedSupplierId();
                } else {
                    showErrorMessage("Failed to add supplier!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error occurred while adding!");
            }
        }
    }

    private boolean validateInputs() {
        if (textName.getText().isEmpty() || textNic.getText().isEmpty() || textContact.getText().isEmpty() ||
                textEmail.getText().isEmpty() || textAddress.getText().isEmpty()) {
            showErrorMessage("All fields must be filled!");
            return false;
        }

        if (!isValidEmail(textEmail.getText())) {
            showErrorMessage("Invalid email format!");
            return false;
        }
        if (!isValidContact(textContact.getText())) {
            showErrorMessage("Contact number must be 10 digits!");
            return false;
        }
        if (!isValidNIC(textNic.getText())) {
            showErrorMessage("Invalid NIC format!");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidContact(String contact) {
        return contact != null && contact.matches("^\\d{10}$");
    }

    private boolean isValidNIC(String nic) {
        return nic != null && (nic.matches("^\\d{9}[vVxX]$") || nic.matches("^\\d{12}$"));
    }

    private void showErrorMessage(String message) {
        textError.setStyle("-fx-text-fill: red;");
        textError.setText(message);
        showErrorWithTimeout();
    }

    private void setGeneretedSupplierId() {
        try {
            String lastId = new SupplierModel().getLastSupplierId();
            int newIdNum = 1;
            if (lastId != null && lastId.startsWith("S")) {
                newIdNum = Integer.parseInt(lastId.substring(1)) + 1;
            }
            String formattedId = String.format("S%03d", newIdNum);
            textSupplierId.setText(formattedId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void clearFields() throws SQLException, ClassNotFoundException {
        textSupplierId.clear();
        textAddress.clear();
        textContact.clear();
        textEmail.clear();
        textNic.clear();
        textName.clear();
        setGeneretedSupplierId();
        setNoOfSupplier();
    }

    private void setNoOfSupplier() throws SQLException, ClassNotFoundException {
        int count = new SupplierModel().getSupplierCount();
        noOfSupplier.setText(String.valueOf(count));
    }

    private void loadSupplierTable() throws SQLException, ClassNotFoundException {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        SupplierModel supplierModel = new SupplierModel();
        ArrayList<SupplierDto> supplierDtos = supplierModel.getAllSuppliers();
        if (supplierDtos != null && !supplierDtos.isEmpty()) {
            showSuccessMessage("Suppliers loaded: " + supplierDtos.size());
            ObservableList<SupplierDto> supplierObservableList = FXCollections.observableArrayList(supplierDtos);
            tableView.setItems(supplierObservableList);
        } else {
            showErrorMessage("No suppliers found!");
        }
    }

    private void showSuccessMessage(String message) {
        textError.setStyle("-fx-text-fill: green;");
        textError.setText(message);
        showErrorWithTimeout();
    }

    private SupplierDto createSupplierDtoFromInputs() {
        return new SupplierDto(
                textSupplierId.getText(),
                textName.getText(),
                textNic.getText(),
                textContact.getText(),
                textEmail.getText(),
                textAddress.getText()
        );
    }

    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = textSupplierId.getText();
        if (id.isEmpty()) {
            showErrorMessage("Please select a supplier to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Are you sure you want to delete this supplier?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = new SupplierModel().deleteSupplier(id);
                if (isDeleted) {
                    showSuccessMessage("Supplier deleted successfully!");
                    loadSupplierTable();
                    clearFields();
                    setGeneretedSupplierId();
                } else {
                    showErrorMessage("Failed to delete supplier!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error occurred while deleting!");
            }
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        searchOnAction();
    }

    private void searchOnAction() throws SQLException, ClassNotFoundException {
        String searchText = textSearchSupplier.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadSupplierTable();
            return;
        }
        SupplierModel supplierModel = new SupplierModel();
        ArrayList<SupplierDto> supplierDtos = supplierModel.searchSupplier(searchText, searchText, searchText, searchText, searchText, searchText);
        ObservableList<SupplierDto> filteredList = FXCollections.observableArrayList();

        for (SupplierDto supplierDto : supplierDtos) {
            if (supplierDto.getSupplierId().toLowerCase().contains(searchText) ||
                    supplierDto.getName().toLowerCase().contains(searchText) ||
                    supplierDto.getNic().toLowerCase().contains(searchText) ||
                    supplierDto.getContact().toLowerCase().contains(searchText) ||
                    supplierDto.getEmail().toLowerCase().contains(searchText) ||
                    supplierDto.getAddress().toLowerCase().contains(searchText)) {
                filteredList.add(supplierDto);
            }
        }
        if (filteredList.isEmpty()) {
            showErrorMessage("No matching supplier found.");
        } else {
            showSuccessMessage(filteredList.size() + " match(es) found.");
        }

        tableView.setItems(filteredList);


    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        SupplierDto dto = createSupplierDtoFromInputs();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Are you sure you want to update this supplier?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: green; -fx-border-width: 2px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isUpdated = new SupplierModel().updateSupplier(dto);
                if (isUpdated) {
                    showSuccessMessage("Supplier updated successfully!");
                    loadSupplierTable();
                    clearFields();
                    setGeneretedSupplierId();
                } else {
                    showErrorMessage("Failed to update supplier!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error occurred while updating!");
            }
        }
    }


    @FXML
    void onKeySearch(KeyEvent event) {
        try {
            searchOnAction();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorWithTimeout();
        }
    }

    private void showErrorWithTimeout() {
        textError.setVisible(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> textError.setVisible(false))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadSupplierTable();
            setGeneretedSupplierId();
            setNoOfSupplier();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showErrorMessage("Initialization failed!");
        }
    }

    public void onSendMail(MouseEvent mouseEvent) throws IOException {
        String mail = textEmail.getText();
        if (mail.isEmpty()) {
            showErrorMessage("Email is required!");
            return;
        }

        // Get current user (if you need it later)
        UserDto user = Session.getCurrentUser();

        // Load FXML and get the controller
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SendMailPage.fxml"));
        Parent rootNode = fxmlLoader.load();
        SendMailPageController controller = fxmlLoader.getController();

        // Pass email to the next controller
        controller.setMail(mail);

        // Create and show new stage
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
