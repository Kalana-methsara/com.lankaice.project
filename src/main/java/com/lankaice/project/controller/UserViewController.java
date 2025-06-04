package com.lankaice.project.controller;

import com.lankaice.project.dto.UserDto;
import com.lankaice.project.dto.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UserViewController implements Initializable {

    @FXML
    private AnchorPane ancMainContainer;

    @FXML
    private AnchorPane ancUserView;

    @FXML
    private Button btnAdminManage, btnBooking, btnDashboard, btnEmployee, btnPayment,
            btnProduct, btnRegister, btnReport, btnSalary, btnTransport,btnInventory,btnStock,btnLogout;

    @FXML
    private Label lblDate;

    @FXML
    private ImageView pngAdminManage, pngBooking, pngEmployee, pngPayment, pngProduct,
            pngRegister, pngReport, pngSalary, pngTransport, pnsDashbord,pngInventory,pngStock,pngLogout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        navigateTo("/view/DashboardPage.fxml");

        UserDto user = Session.getCurrentUser();
        String role = user.getRole();

        switch (role) {
            case "Admin":
                break;
            case "Manager":
                btnAdminManage.setDisable(true);
                break;
            case "Supervisor":
                btnAdminManage.setDisable(true);
                btnSalary.setDisable(true);
                btnRegister.setDisable(true);
                break;
            case "Cashier":
                btnAdminManage.setDisable(true);
                btnSalary.setDisable(true);
                break;
        }

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
        lblDate.setText(currentDate.format(formatter));
    }

    @FXML
    void onDashboard(ActionEvent event) {
        resetOtherPages();
        changePage1(btnDashboard, "/images/dashboard(1).png", pnsDashbord);
        navigateTo("/view/DashboardPage.fxml");
    }

    @FXML
    void onProduct(ActionEvent event) {
        resetOtherPages();
        changePage1(btnProduct, "/images/product(1).png", pngProduct);
        navigateTo("/view/ProductPage.fxml");
    }

    @FXML
    void onEmployee(ActionEvent event) {
        resetOtherPages();
        changePage1(btnEmployee, "/images/employee(1).png", pngEmployee);
        navigateTo("/view/EmployeeMenu.fxml");
    }

    @FXML
    void onTransport(ActionEvent event) {
        resetOtherPages();
        changePage1(btnTransport, "/images/deliver(1).png", pngTransport);
        navigateTo("/view/TransportPage.fxml");
    }

    @FXML
    void onBooking(ActionEvent event) {
        resetOtherPages();
        changePage1(btnBooking, "/images/booking(1).png", pngBooking);
        navigateTo("/view/BookingPage.fxml");
    }

    @FXML
    void onStock(ActionEvent event) {
        resetOtherPages();
        changePage1(btnStock, "/images/stock(1).png", pngStock);
        navigateTo("/view/StockPage.fxml");
    }

    @FXML
    void onSalary(ActionEvent event) {
        resetOtherPages();
        changePage1(btnSalary, "/images/salary(1).png", pngSalary);
        navigateTo("/view/SalaryPage.fxml");
    }

    @FXML
    void onReport(ActionEvent event) {
        resetOtherPages();
        changePage1(btnReport, "/images/report(1).png", pngReport);
        navigateTo("/view/ReportPage.fxml");
    }

    @FXML
    void onAdminManage(ActionEvent event) {
        resetOtherPages();
        changePage1(btnAdminManage, "/images/admin(1).png", pngAdminManage);
        navigateTo("/view/VerifySuperAdmin.fxml");
    }

    public void onPayment(ActionEvent actionEvent) {
        resetOtherPages();
        changePage1(btnPayment, "/images/payment(1).png", pngPayment);
        navigateTo("/view/PaymentPage.fxml");
    }

    public void onInventory(ActionEvent actionEvent) {
        resetOtherPages();
        changePage1(btnInventory, "/images/inventory(1).png", pngInventory);
        navigateTo("/view/InventoryPage.fxml");
    }

    @FXML
    void onRegister(ActionEvent event) {
        resetOtherPages();
        changePage1(btnRegister, "/images/key(1).png", pngRegister);
        navigateTo("/view/RegisterPage.fxml");
    }

    private void resetOtherPages() {
        resetButtonStyle(btnDashboard);
        resetButtonStyle(btnProduct);
        resetButtonStyle(btnEmployee);
        resetButtonStyle(btnTransport);
        resetButtonStyle(btnBooking);
        resetButtonStyle(btnPayment);
        resetButtonStyle(btnSalary);
        resetButtonStyle(btnStock);
        resetButtonStyle(btnInventory);
        resetButtonStyle(btnRegister);
        resetButtonStyle(btnAdminManage);
        resetButtonStyle(btnReport);

        changePage(btnDashboard, "/images/dashboard.png", pnsDashbord);
        changePage(btnProduct, "/images/product.png", pngProduct);
        changePage(btnEmployee, "/images/employee.png", pngEmployee);
        changePage(btnTransport, "/images/deliver.png", pngTransport);
        changePage(btnBooking, "/images/booking.png", pngBooking);
        changePage(btnPayment, "/images/payment.png", pngPayment);
        changePage(btnSalary, "/images/salary.png", pngSalary);
        changePage(btnStock, "/images/stock.png", pngStock);
        changePage(btnInventory, "/images/inventory.png", pngInventory);
        changePage(btnRegister, "/images/key.png", pngRegister);
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);
    }

    private void resetButtonStyle(Button button) {
        button.setStyle("-fx-text-fill: white; -fx-background-color: #023c73; -fx-border-color: #023c73;");
    }

    private void setActiveButtonStyle(Button button) {
        button.setStyle("-fx-text-fill: #e9c90e; -fx-background-color: #023c73; -fx-border-color: #e9c90e;");
    }

    private void changePage(Button button, String imagePath, ImageView imageView) {
        imageView.setImage(new Image(imagePath));
    }

    private void changePage1(Button button, String selectedImagePath, ImageView imageView) {
        imageView.setImage(new Image(selectedImagePath));
        setActiveButtonStyle(button);
    }

    public void navigateTo(String path) {
        try {
            ancMainContainer.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancMainContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMainContainer.heightProperty());
            ancMainContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found", ButtonType.OK).show();
            e.printStackTrace();
        }
    }

    public void onLoginUser(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(getClass().getResource("/view/LoginUserDetails.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();    }

    @FXML
    public void onLogout(ActionEvent actionEvent) {
        // Confirm logout
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);

        alert.initStyle(StageStyle.UNDECORATED);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // Clear the session
                Session.setCurrentUser(null);

                try {
                    // Load the login page
                    AnchorPane loginPane = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
                    ancMainContainer.getScene().setRoot(loginPane);
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Failed to load login page", ButtonType.OK).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
