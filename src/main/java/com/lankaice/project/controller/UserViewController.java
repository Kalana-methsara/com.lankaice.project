package com.lankaice.project.controller;

import com.lankaice.project.dto.UserDto;
import com.lankaice.project.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

<<<<<<< HEAD
=======

>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

<<<<<<< HEAD
public class UserViewController implements Initializable {

=======

public class UserViewController implements Initializable {
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
    @FXML
    private AnchorPane ancMainContainer;

    @FXML
    private AnchorPane ancUserView;

    @FXML
    private Button btnAdminManage;

    @FXML
    private Button btnBooking;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnPayment;

    @FXML
    private Button btnProduct;

    @FXML
<<<<<<< HEAD
    private Button btnRegister;
=======
    private Button btnRejister;
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805

    @FXML
    private Button btnReport;

    @FXML
    private Button btnSalary;

    @FXML
    private Button btnTransport;

    @FXML
    private Label lblDate;

    @FXML
    private ImageView pngAdminManage;

    @FXML
    private ImageView pngBooking;

    @FXML
    private ImageView pngEmployee;

    @FXML
    private ImageView pngPayment;

    @FXML
    private ImageView pngProduct;

    @FXML
<<<<<<< HEAD
    private ImageView pngRegister;
=======
    private ImageView pngRejister;
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805

    @FXML
    private ImageView pngReport;

    @FXML
    private ImageView pngSalary;

    @FXML
    private ImageView pngTransport;

    @FXML
    private ImageView pnsDashbord;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        navigateTo("/view/DashboardPage.fxml");

        UserDto user = Session.getCurrentUser();
        String role = user.getRole();

        switch (role) {
            case "Admin":
                // Full access
                break;

            case "Manager":
                btnAdminManage.setDisable(true);
                break;

            case "Supervisor":
                btnAdminManage.setDisable(true);
                btnSalary.setDisable(true);
<<<<<<< HEAD
                btnRegister.setDisable(true);
=======
                btnRejister.setDisable(true);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
                break;

            case "Cashier":
                btnAdminManage.setDisable(true);
                btnSalary.setDisable(true);
                break;
<<<<<<< HEAD
        }

=======

        }
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        // Set current date
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
        lblDate.setText(currentDate.format(formatter));
    }

<<<<<<< HEAD
=======

>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
    @FXML
    void Transport(ActionEvent event) {
        changePage(btnDashboard, "/images/dashboard.png", pnsDashbord);
        changePage(btnProduct, "/images/product.png", pngProduct);
        changePage(btnEmployee, "/images/employee.png", pngEmployee);
        changePage1(btnTransport, "/images/deliver(1).png", pngTransport);
        changePage(btnBooking, "/images/booking.png", pngBooking);
        changePage(btnPayment, "/images/payment.png", pngPayment);
        changePage(btnSalary, "/images/salary.png", pngSalary);
<<<<<<< HEAD
        changePage(btnRegister, "/images/key.png", pngRegister);
=======
        changePage(btnRejister, "/images/key.png", pngRejister);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);

        navigateTo("/view/TransportPage.fxml");
    }

    @FXML
    void onAdminManage(ActionEvent event) {
        changePage(btnDashboard, "/images/dashboard.png", pnsDashbord);
        changePage(btnProduct, "/images/product.png", pngProduct);
        changePage(btnEmployee, "/images/employee.png", pngEmployee);
        changePage(btnTransport, "/images/deliver.png", pngTransport);
        changePage(btnBooking, "/images/booking.png", pngBooking);
        changePage(btnPayment, "/images/payment.png", pngPayment);
        changePage(btnSalary, "/images/salary.png", pngSalary);
<<<<<<< HEAD
        changePage(btnRegister, "/images/key.png", pngRegister);
=======
        changePage(btnRejister, "/images/key.png", pngRejister);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        changePage1(btnAdminManage, "/images/admin(1).png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);

        navigateTo("/view/VerifySuperAdmin.fxml");
    }

    @FXML
    void onBooking(ActionEvent event) {
        changePage(btnDashboard, "/images/dashboard.png", pnsDashbord);
        changePage(btnProduct, "/images/product.png", pngProduct);
        changePage(btnEmployee, "/images/employee.png", pngEmployee);
        changePage(btnTransport, "/images/deliver.png", pngTransport);
        changePage1(btnBooking, "/images/booking(1).png", pngBooking);
        changePage(btnPayment, "/images/payment.png", pngPayment);
        changePage(btnSalary, "/images/salary.png", pngSalary);
<<<<<<< HEAD
        changePage(btnRegister, "/images/key.png", pngRegister);
=======
        changePage(btnRejister, "/images/key.png", pngRejister);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);

        navigateTo("/view/BookingPage.fxml");
    }

    @FXML
    void onDashbord(ActionEvent event) {
        changePage1(btnDashboard, "/images/dashboard(1).png", pnsDashbord);
        changePage(btnProduct, "/images/product.png", pngProduct);
        changePage(btnEmployee, "/images/employee.png", pngEmployee);
        changePage(btnTransport, "/images/deliver.png", pngTransport);
        changePage(btnBooking, "/images/booking.png", pngBooking);
        changePage(btnPayment, "/images/payment.png", pngPayment);
        changePage(btnSalary, "/images/salary.png", pngSalary);
<<<<<<< HEAD
        changePage(btnRegister, "/images/key.png", pngRegister);
=======
        changePage(btnRejister, "/images/key.png", pngRejister);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);

        navigateTo("/view/DashboardPage.fxml");

    }

    @FXML
    void onEmployee(ActionEvent event) {
        changePage(btnDashboard, "/images/dashboard.png", pnsDashbord);
        changePage(btnProduct, "/images/product.png", pngProduct);
        changePage1(btnEmployee, "/images/employee(1).png", pngEmployee);
        changePage(btnTransport, "/images/deliver.png", pngTransport);
        changePage(btnBooking, "/images/booking.png", pngBooking);
        changePage(btnPayment, "/images/payment.png", pngPayment);
        changePage(btnSalary, "/images/salary.png", pngSalary);
<<<<<<< HEAD
        changePage(btnRegister, "/images/key.png", pngRegister);
=======
        changePage(btnRejister, "/images/key.png", pngRejister);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);

        navigateTo("/view/EmployeeMenu.fxml");
    }

    @FXML
    void onPayment(ActionEvent event) {
        changePage(btnDashboard, "/images/dashboard.png", pnsDashbord);
        changePage(btnProduct, "/images/product.png", pngProduct);
        changePage(btnEmployee, "/images/employee.png", pngEmployee);
        changePage(btnTransport, "/images/deliver.png", pngTransport);
        changePage(btnBooking, "/images/booking.png", pngBooking);
        changePage1(btnPayment, "/images/payment(1).png", pngPayment);
        changePage(btnSalary, "/images/salary.png", pngSalary);
<<<<<<< HEAD
        changePage(btnRegister, "/images/key.png", pngRegister);
=======
        changePage(btnRejister, "/images/key.png", pngRejister);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);

        navigateTo("/view/PaymentPage.fxml");
    }

    @FXML
    void onProduct(ActionEvent event) {
        changePage(btnDashboard, "/images/dashboard.png", pnsDashbord);
        changePage1(btnProduct, "/images/product(1).png", pngProduct);
        changePage(btnEmployee, "/images/employee.png", pngEmployee);
        changePage(btnTransport, "/images/deliver.png", pngTransport);
        changePage(btnBooking, "/images/booking.png", pngBooking);
        changePage(btnPayment, "/images/payment.png", pngPayment);
        changePage(btnSalary, "/images/salary.png", pngSalary);
<<<<<<< HEAD
        changePage(btnRegister, "/images/key.png", pngRegister);
=======
        changePage(btnRejister, "/images/key.png", pngRejister);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);

        navigateTo("/view/ProductPage.fxml");
    }

    @FXML
<<<<<<< HEAD
    void onRegister(ActionEvent event) {
=======
    void onRejister(ActionEvent event) {
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        changePage(btnDashboard, "/images/dashboard.png", pnsDashbord);
        changePage(btnProduct, "/images/product.png", pngProduct);
        changePage(btnEmployee, "/images/employee.png", pngEmployee);
        changePage(btnTransport, "/images/deliver.png", pngTransport);
        changePage(btnBooking, "/images/booking.png", pngBooking);
        changePage(btnPayment, "/images/payment.png", pngPayment);
        changePage(btnSalary, "/images/salary.png", pngSalary);
<<<<<<< HEAD
        changePage1(btnRegister, "/images/key(1).png", pngRegister);
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);

        navigateTo("/view/RegisterPage.fxml");
=======
        changePage1(btnRejister, "/images/key(1).png", pngRejister);
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);

        navigateTo("/view/RejisterPage.fxml");
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
    }

    @FXML
    void onReport(ActionEvent event) {
        changePage(btnDashboard, "/images/dashboard.png", pnsDashbord);
        changePage(btnProduct, "/images/product.png", pngProduct);
        changePage(btnEmployee, "/images/employee.png", pngEmployee);
        changePage(btnTransport, "/images/deliver.png", pngTransport);
        changePage(btnBooking, "/images/booking.png", pngBooking);
        changePage(btnPayment, "/images/payment.png", pngPayment);
        changePage(btnSalary, "/images/salary.png", pngSalary);
<<<<<<< HEAD
        changePage(btnRegister, "/images/key.png", pngRegister);
=======
        changePage(btnRejister, "/images/key.png", pngRejister);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage1(btnReport, "/images/report(1).png", pngReport);

        navigateTo("/view/ReportPage.fxml");
    }

    @FXML
    void onSalary(ActionEvent event) {
        changePage(btnDashboard, "/images/dashboard.png", pnsDashbord);
        changePage(btnProduct, "/images/product.png", pngProduct);
        changePage(btnEmployee, "/images/employee.png", pngEmployee);
        changePage(btnTransport, "/images/deliver.png", pngTransport);
        changePage(btnBooking, "/images/booking.png", pngBooking);
        changePage(btnPayment, "/images/payment.png", pngPayment);
        changePage1(btnSalary, "/images/salary(1).png", pngSalary);
<<<<<<< HEAD
        changePage(btnRegister, "/images/key.png", pngRegister);
=======
        changePage(btnRejister, "/images/key.png", pngRejister);
>>>>>>> 0374aef23ba0afa5e5cdf12288b3cbd0ed0f4805
        changePage(btnAdminManage, "/images/admin.png", pngAdminManage);
        changePage(btnReport, "/images/report.png", pngReport);

        navigateTo("/view/SalaryPage.fxml");
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
    public void changePage(Button button, String imagePath, ImageView imageView) {
        // Set text color
        button.setTextFill(Color.WHITE);

        // Set background and border color via style
        button.setStyle(
                "-fx-background-color: #023c73; " +
                        "-fx-border-color: #023c73;"
        );

        // Set image
        imageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
    }

    public void changePage1(Button button, String imagePath, ImageView imageView) {
        button.setTextFill(Color.YELLOW);
        button.setStyle(
                "-fx-background-color: #023c73; " +
                        "-fx-border-color: #e9c90e;"
        );

        imageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
    }


    public void onLogout(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Are you sure you want to logout?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ancUserView.getChildren().clear();
            Parent parent = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
            ancUserView.getChildren().add(parent);
        }

        /*Parent rootNode = FXMLLoader.load(getClass().getResource("/view/LogoutMessage.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();*/
    }

    public void onLoginUser(MouseEvent mouseEvent) throws IOException {
        UserDto user = Session.getCurrentUser();
        Parent rootNode = FXMLLoader.load(getClass().getResource("/view/LoginUserDetails.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }
}
