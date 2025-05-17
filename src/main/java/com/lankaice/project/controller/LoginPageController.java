package com.lankaice.project.controller;

import com.lankaice.project.dto.UserDto;
import com.lankaice.project.model.UserModel;
import com.lankaice.project.dto.Session;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.lankaice.project.db.DBConnection;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;

public class LoginPageController {
    @FXML
    private Hyperlink lblError;

    @FXML
    private Label lblUsername, lblPassword;

    @FXML
    private AnchorPane ancLogin;

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField txtVisiblePassword;

    @FXML
    private Button btnSignIn;

    private boolean isPasswordVisible = false;

    @FXML
    private void initialize() {
        lblError.setVisible(false);
        javafx.application.Platform.runLater(() -> txtUserName.requestFocus());
    }
    public void onSignAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        signIn();
    }
    private void signIn() throws IOException, SQLException, ClassNotFoundException {
        String userName = txtUserName.getText();
        String password;
        if (!isPasswordVisible) {
            password = passwordField.getText();
        } else {
            password = txtVisiblePassword.getText();
        }

        if (userName.isEmpty() || password.isEmpty()) {
            showErrorWithTimeout();
            return;
        }
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM User WHERE BINARY userName = ? AND BINARY password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();
        UserDto user = UserModel.searchUser(userName, password);


        if (resultSet.next()) {
            Session.setCurrentUser(user);

            ancLogin.getChildren().clear();
            Parent parent = FXMLLoader.load(getClass().getResource("/view/UserView.fxml"));
            ancLogin.getChildren().add(parent);
        } else {
            // new Alert(Alert.AlertType.ERROR, "Invalid email or password!").show();
            showErrorWithTimeout();
        }
    }

    public void onForgotPasswordAction(ActionEvent actionEvent) {
        try {
            ancLogin.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/ForgetPassword.fxml"));
            anchorPane.prefWidthProperty().bind(ancLogin.widthProperty());
            anchorPane.prefHeightProperty().bind(ancLogin.heightProperty());
            ancLogin.getChildren().add(anchorPane);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,  "Page not found");
            new Alert(Alert.AlertType.ERROR, "Page not found", ButtonType.OK).show();
            e.printStackTrace();
        }

    }

    public void PasswordVisibility(MouseEvent mouseEvent) {
        if (isPasswordVisible) {
            passwordField.setText(txtVisiblePassword.getText());
            txtVisiblePassword.setVisible(false);
            txtVisiblePassword.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            isPasswordVisible = false;
        } else {
            txtVisiblePassword.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            txtVisiblePassword.setVisible(true);
            txtVisiblePassword.setManaged(true);
            isPasswordVisible = true;
        }

    }

    private void resetFieldStyles() {
        lblUsername.setStyle("-fx-background-color: #dfe4ea; -fx-border-color: RED; -fx-border-radius: 10; -fx-background-radius: 10;");
        lblPassword.setStyle("-fx-background-color: #dfe4ea; -fx-border-color: RED; -fx-border-radius: 10; -fx-background-radius: 10;");
    }

    private void resetFieldStyles1() {
        lblUsername.setStyle("-fx-background-color: #dfe4ea; -fx-border-color:  #023c73; -fx-border-radius: 10; -fx-background-radius: 10;");
        lblPassword.setStyle("-fx-background-color: #dfe4ea; -fx-border-color:  #023c73; -fx-border-radius: 10; -fx-background-radius: 10;");
    }

    private void showErrorWithTimeout() {
        lblError.setVisible(true);
        resetFieldStyles();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    lblError.setVisible(false);
                    resetFieldStyles1();
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void onKeyPassword(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            try {
                  passwordField.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWithTimeout();
            }
        }

    }

    public void onKeySingin(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            try {
                signIn();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWithTimeout();
            }
        }
    }
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.initStyle(StageStyle.UNDECORATED);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        alert.show();
    }

}
