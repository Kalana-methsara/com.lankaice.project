package com.lankaice.project.controller;

import com.lankaice.project.db.DBConnection;
import com.lankaice.project.dto.EmployeeDto;
import com.lankaice.project.dto.UserDto;
import com.lankaice.project.model.EmployeeModel;
import com.lankaice.project.dto.Session;
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
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class EmployeePageController implements Initializable {
    @FXML
    private TableColumn<EmployeeDto, String> colAddress;
    @FXML
    private TableColumn<EmployeeDto, String> colDateOfBirth;
    @FXML
    private TableColumn<EmployeeDto, String> colGender;
    @FXML
    private TableColumn<EmployeeDto, String> colBankAccNo;
    @FXML
    private TableColumn<EmployeeDto, String> colBankBranch;
    @FXML
    private TableColumn<EmployeeDto, String> colContact;
    @FXML
    private TableColumn<EmployeeDto, String> colEmail;
    @FXML
    private TableColumn<EmployeeDto, String> colEmployeeId;
    @FXML
    private TableColumn<EmployeeDto, String> colJoinDate;
    @FXML
    private TableColumn<EmployeeDto, String> colLicenseNo;
    @FXML
    private TableColumn<EmployeeDto, String> colNIC;
    @FXML
    private TableColumn<EmployeeDto, String> colName;
    @FXML
    private TableColumn<EmployeeDto, String> colRole;
    @FXML
    private TableView<EmployeeDto> tableView;
    @FXML
    private TextField textSearchEmployee;
    @FXML
    private TextField textAddress;
    @FXML
    private TextField textBankAcc;
    @FXML
    private TextField textBranch;
    @FXML
    private TextField textContact;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textEmployeeId;
    @FXML
    private Hyperlink textError;
    @FXML
    private DatePicker textJoinDate;
    @FXML
    private DatePicker textDateOfBirth;
    @FXML
    private TextField textLicenseNo;
    @FXML
    private TextField textName;
    @FXML
    private TextField textNic;
    @FXML
    private Label textDuration;
    @FXML
    private Label textAge;
    @FXML
    private Label noOfEmployee;
    @FXML
    private ChoiceBox<String> txtRole;
    @FXML
    private ChoiceBox<String> txtGender;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtRole.setItems(FXCollections.observableArrayList("Worker", "Supervisor", "Cashier", "Driver", "Manager"));
        txtGender.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        loadEmployeeTable();
        setGeneratedEmployeeId();
        textJoinDate.setValue(LocalDate.now());
        setNoOfEmployee();
        textJoinDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setServicePeriod();
            }
        });

        textDateOfBirth.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setAge();
            }
        });
    }

    public void btnAddEmployeeOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) return;

        EmployeeDto dto = createEmployeeDtoFromInputs();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("\"Are you sure you want to add this Employee?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                boolean isAdded = new EmployeeModel().addEmployee(dto);
                if (isAdded) {
                    showSuccessMessage("Employee added successfully!");
                    loadEmployeeTable();
                    clearFields();
                    setGeneratedEmployeeId();
                } else {
                    showErrorMessage("Failed to add employee!");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                showErrorMessage("Integrity constraint violation: " + e.getMessage());
                //  new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            } catch (SQLException e) {
                showErrorMessage("SQL Error: " + e.getMessage());
                // new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error occurred while adding!");
            }
        }
    }

    public void btnUpdateEmployeeOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) return;

        EmployeeDto dto = createEmployeeDtoFromInputs();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("\"Are you sure you want to update this Employee?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: green; -fx-border-width: 2px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                boolean isUpdated = new EmployeeModel().updateEmployee(dto);
                if (isUpdated) {
                    showSuccessMessage("Employee updated successfully!");
                    loadEmployeeTable();
                    clearFields();
                    setGeneratedEmployeeId();
                } else {
                    showErrorMessage("Failed to update employee!");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                showErrorMessage("Integrity constraint violation: " + e.getMessage());
                // new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            } catch (SQLException e) {
                showErrorMessage("SQL Error: " + e.getMessage());
                //  new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error occurred while adding!");
            }
        }
    }

    public void btnDeleteEmployeeOnAction(ActionEvent actionEvent) {
        String id = textEmployeeId.getText();
        if (id.isEmpty()) {
            showErrorMessage("Please select an employee to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("\"Are you sure you want to delete this Employee?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = new EmployeeModel().deleteEmployee(new EmployeeDto(id));
                if (isDeleted) {
                    showSuccessMessage("Employee deleted successfully!");
                    loadEmployeeTable();
                    clearFields();
                    setGeneratedEmployeeId();
                } else {
                    showErrorMessage("Failed to delete employee!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error occurred while deleting!");
            }
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields() {
        textAddress.clear();
        textBankAcc.clear();
        textBranch.clear();
        textContact.clear();
        textEmail.clear();
        textEmployeeId.clear();
        textJoinDate.setValue(null);
        textLicenseNo.clear();
        textName.clear();
        textNic.clear();
        textDuration.setText("");
        textAge.setText("");
        txtGender.setValue(null);
        textDateOfBirth.setValue(null);
        txtRole.setValue(null);
        textJoinDate.setValue(LocalDate.now());
        setGeneratedEmployeeId();
        setNoOfEmployee();
    }

    public void SetData(MouseEvent mouseEvent) {
        EmployeeDto employeeDto = tableView.getSelectionModel().getSelectedItem();
        if (employeeDto != null) {
            textEmployeeId.setText(employeeDto.getEmployeeId());
            textName.setText(employeeDto.getName());
            textNic.setText(employeeDto.getNic());
            textAddress.setText(employeeDto.getAddress());
            textEmail.setText(employeeDto.getEmail());
            textContact.setText(employeeDto.getContact());
            txtRole.setValue(employeeDto.getJobRole());
            textJoinDate.setValue(LocalDate.parse(employeeDto.getJoinDate()));
            textDateOfBirth.setValue(LocalDate.parse(employeeDto.getDateOfBirth()));
            txtGender.setValue(employeeDto.getGender());
            textBankAcc.setText(employeeDto.getBankAccountNo());
            textBranch.setText(employeeDto.getBankBranch());
            textLicenseNo.setText(employeeDto.getLicenseNumber());
        }
    }

    private void setNoOfEmployee() {
        try {
            int count = new EmployeeModel().getEmployeeCount();
            noOfEmployee.setText(String.valueOf(count));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setServicePeriod() {
        if (textJoinDate.getValue() == null) return;

        LocalDate joinDate = textJoinDate.getValue();
        LocalDate today = LocalDate.now();

        Period period = Period.between(joinDate, today);
        int years = period.getYears();
        int months = period.getMonths();

        textDuration.setText(years + " Years and " + months + " Months");
    }

    private void setAge() {
        if (textDateOfBirth.getValue() == null) return;

        LocalDate dob = textDateOfBirth.getValue();
        LocalDate today = LocalDate.now();

        Period period = Period.between(dob, today);
        int years = period.getYears();

        textAge.setText(" " + years);
    }


    private void setGeneratedEmployeeId() {
        try {
            String lastId = new EmployeeModel().getLastEmployeeId();
            int newIdNum = 1;

            if (lastId != null && lastId.startsWith("E")) {
                newIdNum = Integer.parseInt(lastId.substring(1)) + 1;
            }

            String formattedId = String.format("E%03d", newIdNum);
            textEmployeeId.setText(formattedId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmployeeTable() {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colJoinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colLicenseNo.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("jobRole"));
        colBankBranch.setCellValueFactory(new PropertyValueFactory<>("bankBranch"));
        colBankAccNo.setCellValueFactory(new PropertyValueFactory<>("bankAccountNo"));

        try {
            EmployeeModel employeeModel = new EmployeeModel();
            ArrayList<EmployeeDto> employees = employeeModel.viewAllEmployee();
            if (employees != null && !employees.isEmpty()) {
                showSuccessMessage("Employees found: " + employees.size());  // Log number of employees
                ObservableList<EmployeeDto> employeeObservableList = FXCollections.observableArrayList(employees);
                tableView.setItems(employeeObservableList);
            } else {
                showErrorMessage("No employees found.");
            }
        } catch (SQLException | ClassNotFoundException e) {
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

    private boolean validateInputs() {
        if (textName.getText().isEmpty() || textNic.getText().isEmpty() || textContact.getText().isEmpty() ||
                textEmail.getText().isEmpty() || txtRole.getValue() == null || textAddress.getText().isEmpty() ||
                textJoinDate.getValue() == null || textBankAcc.getText().isEmpty() || textBranch.getText().isEmpty() || textDateOfBirth.getValue() == null || txtGender.getValue() == null) {
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

        if (!isValidBankAccount(textBankAcc.getText())) {
            showErrorMessage("Invalid bank account number!");
            return false;
        }
        if (!validateLicenseField() && !isLicenseDuplicate()) {
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

    private boolean isValidLicense(String license) {
        return license != null && license.matches("^[A-Z0-9/-]{5,15}$");
    }

    private boolean isValidBankAccount(String account) {
        return account != null && account.matches("^\\d{10,12}$");
    }

    private boolean validateLicenseField() {
        String selectedRole = txtRole.getValue();
        String licenseText = textLicenseNo.getText();

        if ("Driver".equals(selectedRole)) {
            if (licenseText.isEmpty()) {
                showErrorMessage("License number is required for Drivers!");
                return false;
            }
            if (!isValidLicense(licenseText)) {
                showErrorMessage("Invalid license number format!");
                return false;
            }
        }
        if (!licenseText.isEmpty()) {
            if (!isValidLicense(licenseText)) {
                showErrorMessage("Invalid license number format!");
                return false;
            }
        }

        return true;
    }

    private boolean isLicenseDuplicate() {
        String licenseText = textLicenseNo.getText();

        try {
            return new EmployeeModel().isLicenseExists(licenseText);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    private EmployeeDto createEmployeeDtoFromInputs() {
        return new EmployeeDto(
                textEmployeeId.getText(),
                textName.getText(),
                textNic.getText(),
                textContact.getText(),
                textEmail.getText(),
                txtRole.getValue(),
                textAddress.getText(),
                textJoinDate.getValue().toString(),
                textDateOfBirth.getValue().toString(),
                txtGender.getValue(),
                textBankAcc.getText(),
                textBranch.getText(),
                textLicenseNo.getText()
        );
    }


    public void onEmployeeReport(MouseEvent mouseEvent) throws IOException {
        UserDto user = Session.getCurrentUser();
        Parent rootNode = FXMLLoader.load(getClass().getResource("/view/EmployeeReport.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        searchOnAction();
    }

    private void searchOnAction() {
        String searchText = textSearchEmployee.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadEmployeeTable(); // Show all employees if search is empty
            return;
        }

        try {
            EmployeeModel employeeModel = new EmployeeModel();
            ArrayList<EmployeeDto> employees = employeeModel.searchEmployee(searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText);
            ObservableList<EmployeeDto> filteredList = FXCollections.observableArrayList();

            for (EmployeeDto emp : employees) {
                if (emp.getName().toLowerCase().contains(searchText) ||
                        emp.getNic().toLowerCase().contains(searchText) ||
                        emp.getEmployeeId().toLowerCase().contains(searchText) ||
                        emp.getEmail().toLowerCase().contains(searchText) ||
                        emp.getJobRole().toLowerCase().contains(searchText) ||
                        emp.getJoinDate().toLowerCase().contains(searchText) ||
                        emp.getDateOfBirth().toLowerCase().contains(searchText) ||
                        emp.getAddress().toLowerCase().contains(searchText) ||
                        emp.getGender().toLowerCase().contains(searchText) ||
                        emp.getBankAccountNo().toLowerCase().contains(searchText) ||
                        emp.getBankBranch().toLowerCase().contains(searchText) ||
                        emp.getLicenseNumber().toLowerCase().contains(searchText) ||
                        emp.getContact().toLowerCase().contains(searchText)) {
                    filteredList.add(emp);
                }
            }

            if (filteredList.isEmpty()) {
                showErrorMessage("No matching employees found.");
            } else {
                showSuccessMessage(filteredList.size() + " match(es) found.");
            }

            tableView.setItems(filteredList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showErrorMessage("Error occurred during search.");
        }
    }

    public void onKeySearch(KeyEvent keyEvent) {
        try {
            searchOnAction();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorWithTimeout();
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
                    getClass().getResourceAsStream("/report/EmployeeReport.jrxml")
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