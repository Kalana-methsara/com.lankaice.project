package com.lankaice.project.controller;

import com.lankaice.project.Validation.QRHttpServer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class EmployeeMenuController implements Initializable {
    @FXML
    private AnchorPane ancEmployeeMenu;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnMark;

    @FXML
    private Button btnStopServer;

    @FXML
    private Label serverResponseLabel;

    @FXML
    private Label serverStatusLabel;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverStatusLabel.setText("Please Start Server Process");
    }

    public void onMarkAttendance(ActionEvent actionEvent) throws IOException {
        btnMark.setDisable(true);
        btnStopServer.setDisable(false);
        QRHttpServer.setResponseListener(response -> {
            javafx.application.Platform.runLater(() -> {
                showServerResponse(response);
            });
        });

        QRHttpServer.startServer();
        updateServerStatusLabel();
    }

    private void showServerResponse(String response) {
        serverResponseLabel.setVisible(true);
        serverResponseLabel.setText(response);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {
                    serverResponseLabel.setText("");
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    void onEditAttendance(ActionEvent event) {

    }

    @FXML
    void onStopServer(ActionEvent event) throws IOException {
        btnStopServer.setDisable(true);
        btnMark.setDisable(false);
        QRHttpServer.stopServer();
        updateServerStatusLabel();
    }
    private void updateServerStatusLabel() {
        if (QRHttpServer.isServerRunning()) {
            serverStatusLabel.setText("🟢 Server is Running");
        } else {
            serverStatusLabel.setText("🟢 Server is Stopped");
        }
    }

    public void onEmployeeManage(ActionEvent actionEvent) {
        navigateTo("/view/EmployeePage.fxml");
    }
    public void navigateTo(String path) {
        try {
            ancEmployeeMenu.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancEmployeeMenu.widthProperty());
            anchorPane.prefHeightProperty().bind(ancEmployeeMenu.heightProperty());
            ancEmployeeMenu.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found", ButtonType.OK).show();
            e.printStackTrace();
        }
    }
}
