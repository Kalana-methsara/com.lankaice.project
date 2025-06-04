package com.lankaice.project.controller;

import com.lankaice.project.db.DBConnection;
import com.lankaice.project.dto.CustomerDto;
import com.lankaice.project.dto.EmployeeDto;
import com.lankaice.project.dto.Session;
import com.lankaice.project.dto.UserDto;
import com.lankaice.project.model.CustomerModel;
import com.lankaice.project.model.EmployeeModel;
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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.*;

public class CustomerPageController implements Initializable {

    @FXML
    private TableColumn<CustomerDto, String> colAddress;

    @FXML
    private TableColumn<CustomerDto, String> colContact;

    @FXML
    private TableColumn<CustomerDto, String> colCustomerId;

    @FXML
    private TableColumn<CustomerDto, String> colDescription;

    @FXML
    private TableColumn<CustomerDto, String> colEmail;

    @FXML
    private TableColumn<CustomerDto, String> colNIC;

    @FXML
    private TableColumn<CustomerDto, String> colName;

    @FXML
    private Label noOfCustomer;

    @FXML
    private TableView<CustomerDto> tableView;

    @FXML
    private TextField textAddress;

    @FXML
    private TextField textContact;

    @FXML
    private TextField textCustomerId;

    @FXML
    private TextField textDescription;

    @FXML
    private TextField textEmail;

    @FXML
    private Hyperlink textError;

    @FXML
    private TextField textName;

    @FXML
    private TextField textNic;

    @FXML
    private TextField textSearchCustomer;

    @FXML
    void SetData(MouseEvent event) {
        CustomerDto customerDto = tableView.getSelectionModel().getSelectedItem();
        if (customerDto != null) {
            textCustomerId.setText(customerDto.getCustomerId());
            textName.setText(customerDto.getName());
            textNic.setText(customerDto.getNic());
            textEmail.setText(customerDto.getEmail());
            textContact.setText(customerDto.getContact());
            textAddress.setText(customerDto.getAddress());
            textDescription.setText(customerDto.getDescription());
        }

    }
    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        CustomerDto customerDto = createCustomerDtoFromInputs();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Are you sure you want to add this Customer?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isAdded = new CustomerModel().addCustomer(customerDto);
                if (isAdded) {
                    showSuccessMessage("Customer added successfully!");
                    loadCustomerTable();
                    clearFields();
                    setGeneretedCustomerId();
                } else {
                    showErrorMessage("Failed to add customer!");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                showErrorMessage("Integrity constraint violation: " + e.getMessage());
            } catch (SQLException e) {
                showErrorMessage("SQL Error: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error occurred while adding!");
            }
        }
    }

    private CustomerDto createCustomerDtoFromInputs() {
        return new CustomerDto(
                textCustomerId.getText(),
                textName.getText(),
                textNic.getText(),
                textEmail.getText(),
                textContact.getText(),
                textAddress.getText(),
                textDescription.getText()
                );
    }

    private boolean validateInputs() {
        if (textName.getText().isEmpty() || textNic.getText().isEmpty() || textContact.getText().isEmpty() ||
                textEmail.getText().isEmpty()  || textAddress.getText().isEmpty()) {
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

    private void clearFields() {
        textCustomerId.clear();
        textAddress.clear();
        textContact.clear();
        textDescription.clear();
        textEmail.clear();
        textNic.clear();
        textName.clear();
        setGeneretedCustomerId();
        setNoOfCustomer();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = textCustomerId.getText();
        if (id.isEmpty()) {
            showErrorMessage("Please select a customer to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Are you sure you want to delete this customer?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = new CustomerModel().deleteCustomer(id);
                if (isDeleted) {
                    showSuccessMessage("Customer deleted successfully!");
                    loadCustomerTable();
                    clearFields();
                    setGeneretedCustomerId();
                } else {
                    showErrorMessage("Failed to delete customer!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error occurred while deleting!");
            }
        }
    }


    @FXML
    void btnSearchOnAction(ActionEvent event) {
        searchOnAction();

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
       if (!validateInputs()) return;

        CustomerDto dto = createCustomerDtoFromInputs();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Are you sure you want to update this customer?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: green; -fx-border-width: 2px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isUpdated = new CustomerModel().updateCustomer(dto);
                if (isUpdated) {
                    showSuccessMessage("Customer updated successfully!");
                    loadCustomerTable();
                    clearFields();
                    setGeneretedCustomerId();
                } else {
                    showErrorMessage("Failed to update customer!");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                showErrorMessage("Integrity constraint violation: " + e.getMessage());
            } catch (SQLException e) {
                showErrorMessage("SQL Error: " + e.getMessage());
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

    private void searchOnAction() {
        String searchText = textSearchCustomer.getText().trim().toLowerCase();
        if(searchText.isEmpty()) {
            loadCustomerTable();
            return;
        }
        CustomerModel customerModel = new CustomerModel();
        ArrayList<CustomerDto>customerDtos=customerModel.searchCustomer(searchText,searchText,searchText,searchText,searchText,searchText,searchText);
        ObservableList<CustomerDto> filteredList = FXCollections.observableArrayList();

        for (CustomerDto customerDto : customerDtos) {
            if(customerDto.getCustomerId().toLowerCase().contains(searchText) ||
            customerDto.getName().toLowerCase().contains(searchText) ||
                    customerDto.getNic().toLowerCase().contains(searchText) ||
                    customerDto.getEmail().toLowerCase().contains(searchText) ||
                    customerDto.getContact().toLowerCase().contains(searchText) ||
                    customerDto.getAddress().toLowerCase().contains(searchText) ||
                    customerDto.getDescription().toLowerCase().contains(searchText)) {
                filteredList.add(customerDto);
            }
        }
        if (filteredList.isEmpty()) {
            showErrorMessage("No matching customer found.");
        } else {
            showSuccessMessage(filteredList.size() + " match(es) found.");
        }

        tableView.setItems(filteredList);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCustomerTable();
        setGeneretedCustomerId();
        setNoOfCustomer();

    }

    private void setNoOfCustomer() {
        try {
            int count =new CustomerModel().getCustomerCount();
            noOfCustomer.setText(String.valueOf(count));
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setGeneretedCustomerId() {
        try {
            String lastId = new CustomerModel().getLastCustomerId();
            int newIdNum = 1;
            if (lastId != null && lastId.startsWith("C")) {
                newIdNum = Integer.parseInt(lastId.substring(1)) + 1;
            }
            String formattedId = String.format("C%03d", newIdNum);
            textCustomerId.setText(formattedId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String message) {
        textError.setStyle("-fx-text-fill: red;");
        textError.setText(message);
        showErrorWithTimeout();
    }

    private void showSuccessMessage(String message) {
        textError.setStyle("-fx-text-fill: green;");
        textError.setText(message);
        showErrorWithTimeout();
    }

    private void showErrorWithTimeout() {
        textError.setVisible(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> textError.setVisible(false))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void loadCustomerTable() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        try{
            CustomerModel customerModel = new CustomerModel();
            ArrayList<CustomerDto> customerDtos = customerModel.getAllCustomers();
            if(customerDtos != null && !customerDtos.isEmpty()){
                showSuccessMessage("Customers loaded: " + customerDtos.size());
                ObservableList<CustomerDto> employeeObservableList = FXCollections.observableArrayList(customerDtos);
                tableView.setItems(employeeObservableList);
            }else {
                showErrorMessage("No customers found!");
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
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


    public void onReport(MouseEvent mouseEvent) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/CustomerReport.jrxml")
            );

            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_date", LocalDate.now().toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters, // null
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

