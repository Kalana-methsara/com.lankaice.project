package com.lankaice.project.controller;

import com.lankaice.project.db.DBConnection;
import com.lankaice.project.util.SendMail;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.mail.MessagingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForgetPasswordController implements Initializable {

    @FXML
    private Label lblCount;
    @FXML
    private AnchorPane ancFoget;
    @FXML
    private ImageView lblCheck;
    @FXML
    private Button btnSendOPT;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblEmail;
    @FXML
    private TextField txtEmail;
    @FXML
    private Label lblOPT;
    @FXML
    private TextField txtOPT;
    @FXML
    private TextField txtUserName;
    @FXML
    private Hyperlink lblError;

    private String generatedOTP;
    private String storedPassword;
    private String storedRole;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblError.setVisible(false);
        lblOPT.setVisible(false);
        txtOPT.setVisible(false);
        lblCheck.setVisible(false);
        lblCount.setVisible(false);
        Platform.runLater(() -> txtUserName.requestFocus());
    }

    @FXML
    void closeOnAction(MouseEvent mouseEvent) {
        loadLoginPage();
    }

    @FXML
    void onSendOTP(ActionEvent event) {
        sendOTP();
    }

    private void sendOTP() {
        String username = txtUserName.getText().trim();
        String email = txtEmail.getText().trim();

        if (username.isEmpty() || email.isEmpty()) {
            showErrorWithTimeout("Username or Email cannot be empty!");
            return;
        }

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String sql = "SELECT password, role FROM User WHERE BINARY userName = ? AND BINARY email = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                setLblCount();

                storedPassword = rs.getString("password");
                storedRole = rs.getString("role");

                generatedOTP = String.valueOf((int) (Math.random() * 900000) + 100000);

                String message = String.format("""
                        Hello %s,
                        
                        Here is your OTP for resetting your password:
                        OTP: %s
                        
                        Please use this OTP to reset your password.
                        
                        Regards,
                        Lanka Ice Team
                        """, username, generatedOTP);

                new OTPSender(message, email).start();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "OTP has been sent to your email!", ButtonType.OK);
                alert.initStyle(StageStyle.UNDECORATED);

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setStyle("-fx-border-color: #023c73; -fx-border-width: 2px;");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        lblOPT.setVisible(true);
                        txtOPT.setVisible(true);
                        lblCheck.setVisible(true);
                    }
                });

                setFieldStyleSuccess();

            } else {
                showErrorWithTimeout("Invalid Username or Email!");
            }

        } catch (Exception e) {
            Logger.getLogger(ForgetPasswordController.class.getName()).log(Level.SEVERE, null, e);
            showAlert(Alert.AlertType.ERROR, "An error occurred while sending OTP. Please try again later.");
        }
    }

    @FXML
    void onVerifyOTP() {
        verifyOTP();
    }

    private void verifyOTP() {
        String enteredOTP = txtOPT.getText().trim();
        String username = txtUserName.getText().trim();
        String email = txtEmail.getText().trim();

        if (enteredOTP.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter the OTP!");
            return;
        }

        if (enteredOTP.equals(generatedOTP)) {
            String message = String.format("""
                    Dear %s,
                    
                    Your login details for the Dry Ice Management System are as follows:
                    Username: %s
                    Password: %s
                    Role: %s
                    
                    Please change your password after logging in for the first time.
                    
                    Regards,
                    System Admin
                    """, username, username, storedPassword, storedRole);

            try {
                new CredentialSender(message, email).start();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login credentials have been sent to your email!", ButtonType.OK);
                alert.initStyle(StageStyle.UNDECORATED);

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setStyle("-fx-border-color: #023c73; -fx-border-width: 2px;");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        loadLoginPage();
                    }
                });

            } catch (Exception e) {
                Logger.getLogger(ForgetPasswordController.class.getName()).log(Level.SEVERE, null, e);
                showAlert(Alert.AlertType.ERROR, "Failed to send email. Try again.");
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid OTP. Please try again!");
        }
    }

    private void loadLoginPage() {
        try {
            ancFoget.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
            anchorPane.prefWidthProperty().bind(ancFoget.widthProperty());
            anchorPane.prefHeightProperty().bind(ancFoget.heightProperty());
            ancFoget.getChildren().add(anchorPane);
        } catch (Exception e) {
            Logger.getLogger(ForgetPasswordController.class.getName()).log(Level.SEVERE, null, e);
            showAlert(Alert.AlertType.ERROR, "Unable to load login page!");
        }
    }

    private void showErrorWithTimeout(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
        setFieldStyleError();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    lblError.setVisible(false);
                    setFieldStyleSuccess();
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void setFieldStyleError() {
        String style = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-background-color:  #dfe4ea;";
        lblUsername.setStyle(style);
        lblEmail.setStyle(style);
    }

    private void setFieldStyleSuccess() {
        String style = "-fx-border-color: #3DAF3BFF; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-background-color:  #dfe4ea;";
        lblUsername.setStyle(style);
        lblEmail.setStyle(style);
    }

    @FXML
    public void onCheck(MouseEvent mouseEvent) {
        onVerifyOTP();
    }

    public void setLblCount() {
        lblCount.setVisible(true);
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                final int count = i;
                Platform.runLater(() -> lblCount.setText(String.valueOf(count)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> lblCount.setVisible(false));
        }).start();
    }

    public void onKeyCheck(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            try {
                verifyOTP();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWithTimeout("Error");
            }
        }
    }

    public void onKeyOTP(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            try {
                sendOTP();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWithTimeout("Error");
            }
        }
    }

    public void onKeyEmail(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            try {
                txtEmail.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWithTimeout("Error");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);

        DialogPane dialogPane = alert.getDialogPane();

        // Use red border for errors and warnings, blue for info:
        if (alertType == Alert.AlertType.ERROR || alertType == Alert.AlertType.WARNING) {
            dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        } else {
            dialogPane.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
        }

        alert.show();
    }

    class OTPSender extends Thread {
        private final String message;
        private final String email;

        public OTPSender(String message, String email) {
            this.message = message;
            this.email = email;
        }

        @Override
        public void run() {
            try {
                SendMail.outMail(message, email, "Password Reset OTP");
            } catch (MessagingException e) {
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Failed to send OTP. Please try again!"));
            }
        }
    }

    class CredentialSender extends Thread {
        private final String message;
        private final String email;

        public CredentialSender(String message, String email) {
            this.message = message;
            this.email = email;
        }

        @Override
        public void run() {
            try {
                SendMail.outMail(message, email, "Login Credentials - Dry Ice Management System");
            } catch (MessagingException e) {
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Failed to send login credentials. Please try again!"));
            }
        }
    }
}
