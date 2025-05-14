package com.lankaice.project.controller;

import com.lankaice.project.dto.EmployeeDto;
import com.lankaice.project.dto.UserDto;
import com.lankaice.project.model.EmployeeModel;
import com.lankaice.project.model.UserModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminManagePageController implements Initializable {
    @FXML
    private Label textDetails;

    @FXML
    private ChoiceBox<String> txtRole;

    @FXML
    private Hyperlink textError;

    @FXML
    private TextField textEmail;

    @FXML
    private TextField textEmployeeId;

    @FXML
    private TextField textFristName;

    @FXML
    private TextField textPassword;

    @FXML
    private TextField textSecondName;

    @FXML
    private TextField textUsername;

    @FXML
    private TableView<UserDto> tableView;

    @FXML
    private TableColumn<UserDto, String> colUsername;

    @FXML
    private TableColumn<UserDto, String> colPassword;

    @FXML
    private TableColumn<UserDto, String> colName;

    @FXML
    private TableColumn<UserDto, String> colEmail;

    @FXML
    private TableColumn<UserDto, String> colRole;
    private final UserModel userModel = new UserModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtRole.setItems(FXCollections.observableArrayList("Admin", "User", "Manager", "Cashier"));
        loadAdminTable();

        textFristName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                textUsername.setText(newValue.trim());
            } else {
                textUsername.clear();
            }
            generateAutoPassword();
        });

        textSecondName.textProperty().addListener((observable, oldValue, newValue) -> generateAutoPassword());
    }


    private void loadAdminTable() {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
            UserModel userModel = new UserModel();
            ArrayList<UserDto> users = userModel.viewAllUsers();
            ObservableList<UserDto> observableList = FXCollections.observableArrayList(users);
            tableView.setItems(observableList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void SetData(MouseEvent mouseEvent) {
        UserDto selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            textUsername.setText(selectedUser.getUsername());
            textPassword.setText(selectedUser.getPassword());

            String[] nameParts = selectedUser.getName().split(" ", 2);
            textFristName.setText(nameParts[0]);
            textSecondName.setText(nameParts.length > 1 ? nameParts[1] : "");

            textEmail.setText(selectedUser.getEmail());
            txtRole.setValue(selectedUser.getRole());
        }
    }

    public void btnAddAdminOnAction(ActionEvent actionEvent) {
        try {
            String email = textEmail.getText().trim();

            if (!isValidEmail(email)) {
                showErrorMessage("Invalid email format.");
                return;
            }

            String fullName = textFristName.getText().trim() + " " + textSecondName.getText().trim();
            UserDto user = new UserDto(
                    textUsername.getText().trim(),
                    textPassword.getText().trim(),
                    fullName.trim(),
                    email,
                    txtRole.getValue()
            );

            boolean isAdded = new UserModel().addUser(user);
            if (isAdded) {
                loadAdminTable();
                clearFields();
                showSuccessMessage("User added successfully!");
            } else {
                showErrorMessage("Failed to add user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error occurred while adding.");
        }
    }

    public void btnUpdateAdminOnAction(ActionEvent actionEvent) {
        try {
            String email = textEmail.getText().trim();

            if (!isValidEmail(email)) {
                showErrorMessage("Invalid email format.");
                return;
            }

            String fullName = textFristName.getText().trim() + " " + textSecondName.getText().trim();
            UserDto user = new UserDto(
                    textUsername.getText().trim(),
                    textPassword.getText().trim(),
                    fullName.trim(),
                    email,
                    txtRole.getValue()
            );

            boolean isUpdated = new UserModel().updateUser(user);
            if (isUpdated) {
                loadAdminTable();
                clearFields();
                showSuccessMessage("User updated successfully!");
            } else {
                showErrorMessage("Failed to update user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error occurred while updating.");
        }
    }

    public void btnDeleteAdminOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (userModel.isOnlyOneUserExists()) {
            System.out.println("Only one user exists in the system.");
        }
        String userName = textUsername.getText().trim();
        if (userName.isEmpty()) {
            showErrorMessage("Please select a user to delete.");
            return;
        }

        try {
            boolean isDeleted = new UserModel().deleteUser(userName);
            if (isDeleted) {
                loadAdminTable();
                clearFields();
                showSuccessMessage("User deleted successfully!");
            } else {
                showErrorMessage("Failed to delete user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error occurred while deleting.");
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        String employeeId = textEmployeeId.getText().trim();

        if (employeeId.isEmpty()) {
            showErrorMessage("Please enter an Employee ID.");
            return;
        }

        try {
            EmployeeModel employeeModel = new EmployeeModel();
            EmployeeDto employee = employeeModel.searchbyId(employeeId);

            if (employee != null) {
                String employeeDetails =
                        "========= Employee Details =========\n" +
                                "Employee ID          : " + employee.getEmployeeId() + "\n" +
                                "Name                      : " + employee.getName() + "\n" +
                                "NIC                          : " + employee.getNic() + "\n" +
                                "Contact                   : " + employee.getContact() + "\n" +
                                "Email                       : " + employee.getEmail() + "\n" +
                                "Job Role                  : " + employee.getJobRole() + "\n" +
                                "Address                  : " + employee.getAddress() + "\n" +
                                "Join Date                : " + employee.getJoinDate() + "\n" +
                                "Bank Account No : " + employee.getBankAccountNo() + "\n" +
                                "Bank Branch         : " + employee.getBankBranch() + "\n" +
                                "License Number   : " + employee.getLicenseNumber();


                textDetails.setText(employeeDetails);
                textDetails.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 15px; -fx-text-fill: #2c3e50;");

            } else {
                textDetails.setText("No employee found with ID: " + employeeId);
                textDetails.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 15px; -fx-text-fill: red;");

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showErrorMessage("Error occurred while searching.");
        }
    }
    public void btnSearchOnAction1(ActionEvent actionEvent) {
        String name = textFristName.getText().trim();

        if (name.isEmpty()) {
            showErrorMessage("Please enter an employee name to search.");
            return;
        }

        try {
            EmployeeModel employeeModel = new EmployeeModel();
            ArrayList<EmployeeDto> matchedEmployees = employeeModel.searchByName(name);

            if (!matchedEmployees.isEmpty()) {
                EmployeeDto employee = matchedEmployees.get(0);
                String employeeDetails =
                        "========= Employee Details =========\n" +
                                "Employee ID          : " + employee.getEmployeeId() + "\n" +
                                "Name                      : " + employee.getName() + "\n" +
                                "NIC                          : " + employee.getNic() + "\n" +
                                "Contact                   : " + employee.getContact() + "\n" +
                                "Email                       : " + employee.getEmail() + "\n" +
                                "Job Role                  : " + employee.getJobRole() + "\n" +
                                "Address                  : " + employee.getAddress() + "\n" +
                                "Join Date                : " + employee.getJoinDate() + "\n" +
                                "Bank Account No : " + employee.getBankAccountNo() + "\n" +
                                "Bank Branch         : " + employee.getBankBranch() + "\n" +
                                "License Number   : " + employee.getLicenseNumber();


                textDetails.setText(employeeDetails);
                textDetails.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 15px; -fx-text-fill: #2c3e50;");
            } else {
                textDetails.setText("No employee found with the name \"" + name + "\".");
                textDetails.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 15px; -fx-text-fill: red;");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showErrorMessage("Error occurred while searching for employee.");
        }
    }



    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void generateAutoPassword() {
        String first = textFristName.getText().trim();
        String second = textSecondName.getText().trim();

        if (!first.isEmpty() && !second.isEmpty()) {
            String autoPassword = "#00" + Character.toUpperCase(first.charAt(0)) +
                    Character.toUpperCase(second.charAt(0)) + "pw";
            textPassword.setText(autoPassword);
        } else {
            textPassword.clear();
        }
    }

    private void clearFields() {
        textDetails.setText("");
        textUsername.clear();
        textPassword.clear();
        textFristName.clear();
        textSecondName.clear();
        textEmail.clear();
        txtRole.setValue(null);
        textEmployeeId.clear();
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
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }


}
